package geovista.satscan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.readers.FileIO;
import geovista.readers.example.GeoData48States;

/**
 * ConditioningAnimator is used to send out indication signals that corrispond
 * to current classifications.
 * 
 */
public class SaTScan extends JPanel implements ActionListener, DataSetListener {

    private final String saTScanFile = "C:/Program Files/SaTScan/SaTScanBatch.exe";

    private final String saTScanDir = "C:/Program Files/SaTScan/";

    DataSetForApps dataSet;

    JComboBox caseBox;

    JComboBox popBox;

    JButton runButt;

    JButton chooseButt;

    JTable resultsTable;

    ArrayList<Integer> idList;

    ArrayList<Integer> clusterList;

    ArrayList<Double> pValueList;

    static final int ID_COLUMN = 0;

    static final int CLUSTER_COLUMN = 1;

    static final int PVALUE_COLUMN = 2;

    private int caseVariable;

    private int popVariable;

    private final JButton sendClustersButton;

    private final JButton sendPValuesButton;

    protected static final Logger logger = Logger.getLogger(SaTScan.class.getName());

    /**
	 * null ctr
	 */
    public SaTScan() {
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.add(constructVariablePanel());
        middlePanel.add(constructExecutablePanel());
        middlePanel.setPreferredSize(new Dimension(500, 150));
        setLayout(new BorderLayout());
        this.add(middlePanel, BorderLayout.CENTER);
        JLabel overallLabel = new JLabel("SaTScan Poisson Analysis");
        this.add(overallLabel, BorderLayout.NORTH);
        runButt = new JButton("Run!");
        runButt.addActionListener(this);
        JPanel southPanel = new JPanel();
        resultsTable = new JTable(4, 3);
        resultsTable.setValueAt("ID", 0, 0);
        resultsTable.setValueAt("Cluster", 0, 1);
        resultsTable.setValueAt("P value", 0, 2);
        southPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        southPanel.add(runButt);
        southPanel.add(new JLabel("Sample Results:"));
        southPanel.add(resultsTable);
        JPanel sendBPanel = new JPanel();
        sendBPanel.setLayout(new BoxLayout(sendBPanel, BoxLayout.Y_AXIS));
        sendClustersButton = new JButton("Send Clusters");
        sendClustersButton.addActionListener(this);
        sendPValuesButton = new JButton("Send P-Values");
        sendPValuesButton.addActionListener(this);
        sendBPanel.add(sendClustersButton);
        sendBPanel.add(sendPValuesButton);
        southPanel.add(sendBPanel);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runButt) {
            findCluster(dataSet);
        } else if (e.getSource() == chooseButt) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(saTScanFile));
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                logger.finest("You chose to use this file: " + fileChooser.getSelectedFile().getName());
            }
        } else if (e.getSource() == popBox) {
            popVariable = popBox.getSelectedIndex();
        } else if (e.getSource() == caseBox) {
            caseVariable = caseBox.getSelectedIndex();
        } else if (e.getSource() == sendClustersButton) {
            double[] newData = new double[dataSet.getNumObservations()];
            int counter = 0;
            for (Integer i : idList) {
                newData[i] = clusterList.get(counter);
                counter++;
            }
        } else if (e.getSource() == sendPValuesButton) {
            double[] newData = new double[dataSet.getNumObservations()];
            int counter = 0;
            for (Integer i : idList) {
                newData[i] = pValueList.get(counter);
                counter++;
            }
        }
    }

    private JPanel constructVariablePanel() {
        JPanel varPanel = new JPanel();
        varPanel.setBorder(BorderFactory.createTitledBorder("Select Variables"));
        JLabel casLabel = new JLabel("Choose case variable:");
        JLabel popLabel = new JLabel("Choose pop. variable:");
        caseBox = new JComboBox();
        caseBox.addActionListener(this);
        popBox = new JComboBox();
        popBox.addActionListener(this);
        JPanel casPanel = new JPanel();
        casPanel.add(casLabel);
        casPanel.add(caseBox);
        JPanel popPanel = new JPanel();
        popPanel.add(popLabel);
        popPanel.add(popBox);
        varPanel.setLayout(new BoxLayout(varPanel, BoxLayout.Y_AXIS));
        varPanel.add(casPanel);
        varPanel.add(popPanel);
        return varPanel;
    }

    private JPanel constructExecutablePanel() {
        JPanel exePanel = new JPanel();
        exePanel.setBorder(BorderFactory.createTitledBorder("SaTScan Executable"));
        JLabel firstLabel = new JLabel("Location of SaTScan program:");
        JLabel secondLabel = new JLabel("C:\\Program Files\\SaTScan");
        chooseButt = new JButton("Choose new location");
        chooseButt.addActionListener(this);
        JLabel thirdLabel = new JLabel("Status: OK");
        exePanel.setLayout(new BoxLayout(exePanel, BoxLayout.Y_AXIS));
        exePanel.add(firstLabel);
        exePanel.add(secondLabel);
        exePanel.add(chooseButt);
        exePanel.add(thirdLabel);
        return exePanel;
    }

    private void findCluster(DataSetForApps data) {
        File exe = new File(saTScanFile);
        if (!exe.exists()) {
            logger.finest("couldn't find SaTScan exe");
            return;
        }
        dataSet = data;
        Shape[] shapes = data.getShapeData();
        double[] xLocs = new double[shapes.length];
        double[] yLocs = new double[shapes.length];
        String[] ids = new String[data.getNumObservations()];
        for (int i = 0; i < shapes.length; i++) {
            Rectangle2D rect = shapes[i].getBounds2D();
            xLocs[i] = rect.getCenterX();
            yLocs[i] = rect.getCenterY();
            ids[i] = String.valueOf(i);
        }
        double[] nCases = data.getNumericDataAsDouble(caseVariable);
        double[] covariate = data.getNumericDataAsDouble(popVariable);
        writePrmFile();
        File resultsFile = new File(saTScanDir + "test.gis.txt");
        if (resultsFile.exists()) {
        }
        SaTScan.writeGeoFile(saTScanDir + "test.geo", ids, yLocs, xLocs);
        SaTScan.writeCasFile(saTScanDir + "test.cas", ids, nCases, covariate);
        String[] commands = { "cmd", "/c", "start", "SaTScanBatch.exe", "test.prm" };
        try {
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.directory(new File(saTScanDir));
            pb.redirectErrorStream(true);
            pb.start();
            logger.finest(pb.directory().getPath());
            readResultsFile(saTScanDir + "test.gis.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePrmFile() {
        InputStream inStream = this.getClass().getResourceAsStream("resources/test.prm");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saTScanDir + "test.prm");
            int oneChar, count = 0;
            while ((oneChar = inStream.read()) != -1) {
                fos.write(oneChar);
                count++;
            }
            inStream.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeGeoFile(String fileName, String[] obsNames, double[] xLocs, double[] yLocs) {
        FileIO fio = null;
        try {
            fio = new FileIO(fileName, "w");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < obsNames.length; i++) {
            String line = obsNames[i] + "\t" + xLocs[i] + "\t" + yLocs[i] + "\n";
            fio.write(line);
        }
        try {
            fio.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeCasFile(String fileName, String[] obsNames, double[] nCases, double[] covariate) {
        FileIO fio = null;
        try {
            fio = new FileIO(fileName, "w");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < obsNames.length; i++) {
            String line = obsNames[i] + "\t" + nCases[i] + "\t" + covariate[i] + "\n";
            fio.write(line);
        }
        try {
            fio.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readResultsFile(String fileName) {
        boolean foundFile = false;
        long waitTime = 1000;
        while (!foundFile) {
            File file = new File(fileName);
            if (file.exists()) {
                foundFile = true;
            } else {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waitTime = waitTime + 1000;
                logger.finest("looking for file...." + (waitTime / 1000));
            }
        }
        FileIO fio = null;
        try {
            fio = new FileIO(fileName, "r");
        } catch (Exception e) {
            e.printStackTrace();
        }
        idList = new ArrayList();
        clusterList = new ArrayList();
        pValueList = new ArrayList();
        int lineNum = 1;
        while (!fio.hasReachedEOF()) {
            String line = null;
            try {
                line = fio.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line != null) {
                StringTokenizer toker = new StringTokenizer(line);
                Integer id = Integer.valueOf(toker.nextToken());
                idList.add(id);
                Integer cluster = Integer.valueOf(toker.nextToken());
                clusterList.add(cluster);
                Double pValue = Double.valueOf(toker.nextToken());
                pValueList.add(pValue);
                if (lineNum < resultsTable.getRowCount()) {
                    resultsTable.setValueAt(id, lineNum, SaTScan.ID_COLUMN);
                    resultsTable.setValueAt(cluster, lineNum, SaTScan.CLUSTER_COLUMN);
                    resultsTable.setValueAt(pValue, lineNum, SaTScan.PVALUE_COLUMN);
                }
                lineNum++;
            }
        }
        try {
            fio.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GeoData48States states = new GeoData48States();
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("n num atts" + states.getDataForApps().getNumberNumericAttributes());
        }
        SaTScan scanner = new SaTScan();
        JFrame app = new JFrame("testing SaTScan");
        app.add(scanner);
        app.pack();
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void dataSetChanged(DataSetEvent e) {
        dataSet = e.getDataSetForApps();
        String[] varNames = dataSet.getAttributeNamesNumeric();
        for (String name : varNames) {
            caseBox.addItem(name);
            popBox.addItem(name);
        }
    }
}
