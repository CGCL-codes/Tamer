package parity;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import util.net.DataGramCommunication;
import util.net.PacketBuffer;

public class Parity4Simulator extends JFrame implements ActionListener, Runnable {

    private JTextField portInput = new JTextField("7010", 10);

    private JTextField filenameInput = new JTextField("txt/parity4.txt", 10);

    private JTextField loadDataCountInput = new JTextField("16", 10);

    private JTextField testDataCountInput = new JTextField("10", 10);

    private JProgressBar progressBar = new JProgressBar(0, 5000);

    private JButton runButton = new JButton("run");

    private JButton exitButton = new JButton("exit");

    private JLabel progressLabel = new JLabel(" ");

    private JLabel randomiseDataLabel = new JLabel("randomise");

    private JComboBox randomiseDataComboBox = new JComboBox();

    private double[][] dataArray = null;

    private int[] randomIndices = null;

    private int nextRandomRow = 0;

    private Random random = new Random();

    private int dataCount = 0;

    private int columns = 0;

    private DataGramCommunication com = new DataGramCommunication(DataGramCommunication.SERVER);

    private DataInputStream in = null;

    private DataOutputStream out = null;

    private PacketBuffer pbuf = new PacketBuffer();

    private static final int COMMAND_GET_DATA = 0;

    private static final int COMMAND_RESET = 1;

    private static final int COMMAND_SET_POSITION = 2;

    private static final int COMMAND_ROBOT_OK = 3;

    private static final int COMMAND_ROBOT_BUMPED = 4;

    private static final int COMMAND_NEXT_TRY = 5;

    public Parity4Simulator() {
        super("Parity4Simulator");
        int row = -1;
        progressBar.setMinimumSize(new Dimension(200, 20));
        JLabel portLabel = new JLabel("Port");
        JLabel filenameLabel = new JLabel("Filename");
        JLabel loadDataCountLabel = new JLabel("# of data");
        JLabel testDataCountLabel = new JLabel("# of tests");
        randomiseDataComboBox.addItem("yes");
        randomiseDataComboBox.addItem("no");
        GridBagLayout gblInput = new GridBagLayout();
        GridBagLayout gblProgress = new GridBagLayout();
        JPanel inputPanel = new JPanel();
        JPanel progressPanel = new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 0));
        inputPanel.setLayout(gblInput);
        progressPanel.setLayout(gblProgress);
        panel.add(inputPanel);
        panel.add(progressPanel);
        GridBagConstraints portLabelConstraints = new GridBagConstraints(0, ++row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        GridBagConstraints portInputConstraints = new GridBagConstraints(1, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        gblInput.setConstraints(portInput, portInputConstraints);
        gblInput.setConstraints(portLabel, portLabelConstraints);
        inputPanel.add(portLabel);
        inputPanel.add(portInput);
        GridBagConstraints filenameLabelConstraints = new GridBagConstraints(0, ++row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        GridBagConstraints filenameInputConstraints = new GridBagConstraints(1, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        gblInput.setConstraints(filenameInput, filenameInputConstraints);
        gblInput.setConstraints(filenameLabel, filenameLabelConstraints);
        inputPanel.add(filenameLabel);
        inputPanel.add(filenameInput);
        GridBagConstraints loadDataCountLabelConstraints = new GridBagConstraints(0, ++row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        GridBagConstraints loadDataCountInputConstraints = new GridBagConstraints(1, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        gblInput.setConstraints(loadDataCountInput, loadDataCountInputConstraints);
        gblInput.setConstraints(loadDataCountLabel, loadDataCountLabelConstraints);
        inputPanel.add(loadDataCountLabel);
        inputPanel.add(loadDataCountInput);
        GridBagConstraints testDataCountLabelConstraints = new GridBagConstraints(0, ++row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        GridBagConstraints testDataCountInputConstraints = new GridBagConstraints(1, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        gblInput.setConstraints(testDataCountInput, testDataCountInputConstraints);
        gblInput.setConstraints(testDataCountLabel, testDataCountLabelConstraints);
        inputPanel.add(testDataCountLabel);
        inputPanel.add(testDataCountInput);
        GridBagConstraints randomiseDataLabelConstraints = new GridBagConstraints(0, ++row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        GridBagConstraints randomiseDataComboBoxConstraints = new GridBagConstraints(1, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        gblInput.setConstraints(randomiseDataComboBox, randomiseDataComboBoxConstraints);
        gblInput.setConstraints(randomiseDataLabel, randomiseDataLabelConstraints);
        inputPanel.add(randomiseDataLabel);
        inputPanel.add(randomiseDataComboBox);
        GridBagConstraints progressLabelConstraints = new GridBagConstraints(0, ++row, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        gblProgress.setConstraints(progressLabel, progressLabelConstraints);
        progressPanel.add(progressLabel);
        GridBagConstraints progressBarConstraints = new GridBagConstraints(0, ++row, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        gblProgress.setConstraints(progressBar, progressBarConstraints);
        progressPanel.add(progressBar);
        GridBagConstraints exitButtonConstraints = new GridBagConstraints(1, ++row, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        GridBagConstraints runButtonConstraints = new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
        gblProgress.setConstraints(runButton, runButtonConstraints);
        gblProgress.setConstraints(exitButton, exitButtonConstraints);
        runButton.addActionListener(this);
        exitButton.addActionListener(this);
        progressPanel.add(runButton);
        progressPanel.add(exitButton);
        setContentPane(panel);
        setSize(getMinimumSize());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runButton) {
            progressLabel.setText("Loading file ...");
            (new Thread(this)).start();
        }
        if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    public void connect() {
        com.setPort(Integer.parseInt(portInput.getText()));
        progressLabel.setText("Waiting for Hinton...");
        com.initConnection();
        progressLabel.setText("Done ...");
    }

    public void setRandomNumbers() {
        int randomNumber = Integer.parseInt(testDataCountInput.getText());
        int randomMaxNumber = Integer.parseInt(loadDataCountInput.getText());
        randomIndices = new int[randomNumber];
        if (randomiseDataComboBox.getSelectedIndex() == 0) {
            for (int i = 0; i < randomNumber; i++) {
                randomIndices[i] = random.nextInt(randomMaxNumber);
                System.out.println("Random number " + i + ": " + randomIndices[i]);
            }
        } else {
            for (int i = 0; i < randomNumber; i++) {
                randomIndices[i] = i % randomMaxNumber;
                System.out.println("Random number " + i + ": " + randomIndices[i]);
            }
        }
    }

    public double[] getNextRow() {
        return dataArray[randomIndices[nextRandomRow++]];
    }

    public void evaluate() {
        nextRandomRow = 0;
        progressBar.setMaximum(randomIndices.length);
        progressLabel.setText("Evaluating ...");
        progressBar.setStringPainted(true);
        for (int i = 0; i < randomIndices.length; i++) {
            progressBar.setValue(i);
            double[] tmp = getNextRow();
        }
    }

    public void communicate() {
        int command = 0;
        progressLabel.setText("Communication running ...");
        try {
            pbuf.resetBuf();
            com.readPacketBuffer(pbuf);
            command = pbuf.readInt();
            switch(command) {
                case COMMAND_SET_POSITION:
                    setRandomNumbers();
                    nextRandomRow = 0;
                    break;
                case COMMAND_RESET:
                    nextRandomRow = 0;
                    break;
                case COMMAND_GET_DATA:
                    double row[] = null;
                    try {
                        row = getNextRow();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("nextRandomRow " + nextRandomRow);
                    }
                    pbuf.resetBuf();
                    pbuf.writeFloat((float) row[0]);
                    pbuf.writeFloat((float) row[1]);
                    pbuf.writeFloat((float) row[2]);
                    pbuf.writeFloat((float) row[3]);
                    com.writePacketBuffer(pbuf);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        loadFile(filenameInput.getText(), Integer.parseInt(loadDataCountInput.getText()));
        setRandomNumbers();
        connect();
        boolean keepOnRunning = true;
        while (keepOnRunning) {
            communicate();
        }
    }

    public void printTestLine(int index) {
        System.out.println("********************");
        for (int i = 0; i < columns; i++) {
            System.out.println(dataArray[index][i]);
        }
        System.out.println("********************");
    }

    public void loadFile(String filename, int lines) {
        progressBar.setStringPainted(true);
        dataCount = Integer.parseInt(loadDataCountInput.getText());
        double value = 0;
        int lineCount = 0;
        progressLabel.setText("Loading file ... ");
        progressBar.setMinimum(0);
        progressBar.setMaximum(dataCount);
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
            String firstLine = in.readLine();
            StringTokenizer st = new StringTokenizer(firstLine, "\t");
            columns = st.countTokens();
            dataArray = new double[dataCount][columns];
            for (int i = 0; i < columns; i++) {
                value = Double.parseDouble(st.nextToken());
                dataArray[lineCount][i] = value;
            }
            String lineString = null;
            for (int i = 1; i < dataCount; i++) {
                lineString = in.readLine();
                st = new StringTokenizer(lineString, "\t");
                for (int j = 0; j < columns; j++) {
                    value = Double.parseDouble(st.nextToken());
                    dataArray[i][j] = value;
                }
                progressBar.setValue(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String argv[]) {
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");
        try {
            javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme());
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Metal Look & Feel not supported on this platform. \nProgram Terminated");
            System.exit(0);
        } catch (IllegalAccessException e) {
            System.out.println("Metal Look & Feel could not be accessed. \nProgram Terminated");
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("Metal Look & Feel could not found. \nProgram Terminated");
            System.exit(0);
        } catch (InstantiationException e) {
            System.out.println("Metal Look & Feel could not be instantiated. \nProgram Terminated");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Unexpected error. \nProgram Terminated");
            e.printStackTrace();
            System.exit(0);
        }
        Parity4Simulator es = new Parity4Simulator();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension esSize = es.getSize();
        int x = (int) (screenSize.getWidth() / 2.0d);
        int y = (int) (screenSize.getHeight() / 2.0d);
        x = x - (int) (esSize.getWidth() / 2.0d);
        y = y - (int) (esSize.getHeight() / 2.0d);
        es.setLocation(x, y);
        es.setVisible(true);
    }
}
