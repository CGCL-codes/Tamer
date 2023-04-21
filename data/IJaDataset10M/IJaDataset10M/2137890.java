package shu.thesis.applet.psychophysics;

import java.io.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.sun.media.jai.widget.*;
import flanagan.analysis.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public class PsychophysicsExperimentFrame extends JFrame {

    JPanel contentPane;

    BorderLayout borderLayout1 = new BorderLayout();

    JMenuBar jMenuBar1 = new JMenuBar();

    JMenu jMenuFile = new JMenu();

    JMenuItem jMenuFileExit = new JMenuItem();

    JMenu jMenuHelp = new JMenu();

    JMenuItem jMenuHelpAbout = new JMenuItem();

    JPanel jPanel1 = new JPanel();

    DisplayJAI jPanel_left = new DisplayJAI();

    DisplayJAI jPanel_right = new DisplayJAI();

    JPanel jPanel4 = new JPanel();

    JButton jButton_left = new JButton();

    JButton jButton_right = new JButton();

    TitledBorder titledBorder1 = new TitledBorder("");

    ButtonGroup buttonGroup1 = new ButtonGroup();

    public PsychophysicsExperimentFrame() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
            init();
            initMethodArray(MAX_METHOD);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    JMenu jMenu_situation = new JMenu();

    JMenuItem jMenuItem_start = new JMenuItem();

    protected int getSituation() {
        File dir = new File(PsychophysicsDir);
        int dirCount = 0;
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                dirCount++;
            }
        }
        return dirCount;
    }

    protected String[] getSituationStringArray() {
        File dir = new File(PsychophysicsDir);
        ArrayList<String> list = new ArrayList<String>();
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                list.add(f.getName());
            }
        }
        int size = list.size();
        String[] stringArray = new String[size];
        for (int x = 0; x < size; x++) {
            stringArray[x] = list.get(x);
        }
        return stringArray;
    }

    protected void initMenuItemArray(String[] situationStringArray) {
        int size = situationStringArray.length;
        JRadioButtonMenuItem[] array = new JRadioButtonMenuItem[size];
        for (int x = 0; x < size; x++) {
            String label = situationStringArray[x];
            array[x] = new JRadioButtonMenuItem(label);
            array[x].setActionCommand(label);
            buttonGroup1.add(array[x]);
            jMenu_situation.add(array[x]);
        }
    }

    protected void initMenuItemArray(int situation) {
        JRadioButtonMenuItem[] array = new JRadioButtonMenuItem[situation];
        for (int x = 0; x < situation; x++) {
            array[x] = new JRadioButtonMenuItem(String.valueOf(x));
            array[x].setMnemonic(x);
            buttonGroup1.add(array[x]);
            jMenu_situation.add(array[x]);
        }
    }

    protected void init() throws Exception {
        String[] situation = this.getSituationStringArray();
        initMenuItemArray(situation);
    }

    /**
   * Component initialization.
   *
   * @throws java.lang.Exception
   */
    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout1);
        setSize(new Dimension(300, 200));
        setTitle("�߲z���z����");
        jMenuFile.setBackground(new Color(123, 123, 123));
        jMenuFile.setText("File");
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new PsychophysicsExperimentFrame_jMenuFileExit_ActionAdapter(this));
        jMenuHelp.setBackground(new Color(123, 123, 123));
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new PsychophysicsExperimentFrame_jMenuHelpAbout_ActionAdapter(this));
        contentPane.setBackground(new Color(123, 123, 123));
        jButton_left.setBackground(new Color(123, 123, 123));
        jButton_left.setEnabled(false);
        jButton_left.setText("��");
        jButton_left.addActionListener(new PsychophysicsExperimentFrame_jButton1_actionAdapter(this));
        jButton_right.setEnabled(false);
        jButton_right.setText("�k");
        jButton_right.addActionListener(new PsychophysicsExperimentFrame_jButton2_actionAdapter(this));
        jPanel_left.setBackground(new Color(123, 123, 123));
        jPanel_left.setBorder(border3);
        jPanel_right.setBackground(new Color(123, 123, 123));
        jPanel_right.setBorder(border3);
        jPanel1.setBackground(new Color(123, 123, 123));
        jPanel1.setLayout(gridLayout1);
        jPanel4.setBackground(new Color(123, 123, 123));
        jMenuBar1.setBackground(new Color(123, 123, 123));
        jMenu_situation.setBackground(new Color(123, 123, 123));
        jMenu_situation.setText("Situation");
        jMenuItem_start.setText("Start");
        jMenuItem_start.addActionListener(new PsychophysicsExperimentFrame_jMenuItem1_actionAdapter(this));
        jPanel2.setLayout(borderLayout2);
        jPanel3.setLayout(borderLayout3);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenu_situation);
        jMenuFile.add(jMenuItem_start);
        jMenuFile.add(jMenuFileExit);
        jMenuBar1.add(jMenuHelp);
        jMenuHelp.add(jMenuHelpAbout);
        contentPane.add(jPanel1, java.awt.BorderLayout.CENTER);
        contentPane.add(jPanel4, java.awt.BorderLayout.SOUTH);
        jPanel4.add(jButton_left);
        jPanel4.add(jLabel1);
        jPanel4.add(jButton_right);
        jPanel1.add(jPanel3);
        jPanel3.add(jPanel_left, java.awt.BorderLayout.CENTER);
        jPanel1.add(jPanel2);
        jPanel2.add(jPanel_right, java.awt.BorderLayout.CENTER);
        setJMenuBar(jMenuBar1);
    }

    /**
   * File | Exit action performed.
   *
   * @param actionEvent ActionEvent
   */
    void jMenuFileExit_actionPerformed(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
   * Help | About action performed.
   *
   * @param actionEvent ActionEvent
   */
    void jMenuHelpAbout_actionPerformed(ActionEvent actionEvent) {
        PsychophysicsExperimentFrame_AboutBox dlg = new PsychophysicsExperimentFrame_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    protected void disableButton() {
        jButton_left.setEnabled(false);
        jButton_right.setEnabled(false);
    }

    public void jButton_left_actionPerformed(ActionEvent e) {
        try {
            fileWriter.write(df.format(pic) + "_" + df.format(leftMethod) + "/" + df.format(pic) + "_" + df.format(rightMethod) + "/0\r\n");
            fileWriter.flush();
            if (!nextPicture()) {
                disableButton();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            disableButton();
        }
    }

    public void jButton_right_actionPerformed(ActionEvent e) {
        try {
            fileWriter.write(df.format(pic) + "_" + df.format(leftMethod) + "/" + df.format(pic) + "_" + df.format(rightMethod) + "/1\r\n");
            fileWriter.flush();
            if (!nextPicture()) {
                disableButton();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            disableButton();
        }
    }

    protected String situation;

    protected int pic;

    protected DecimalFormat df = new DecimalFormat("00");

    protected boolean start;

    protected int leftMethod = 0;

    protected int rightMethod = 0;

    protected String outputFilename;

    protected FileWriter fileWriter;

    protected int count = 0;

    protected static final int MAX_PIC = 7;

    protected static final int MAX_METHOD = 3;

    protected static final String PsychophysicsDir = "psychophysics";

    JLabel jLabel1 = new JLabel();

    GridLayout gridLayout1 = new GridLayout();

    JPanel jPanel2 = new JPanel();

    JPanel jPanel3 = new JPanel();

    BorderLayout borderLayout2 = new BorderLayout();

    BorderLayout borderLayout3 = new BorderLayout();

    Border border3 = BorderFactory.createEmptyBorder(100, 100, 100, 100);

    JOptionPane jOptionPane1 = new JOptionPane();

    protected String getLeftPictureFilename() {
        return PsychophysicsDir + "/" + situation + "/" + df.format(pic) + "_" + df.format(leftMethod) + ".tif";
    }

    protected String getRightPictureFilename() {
        return PsychophysicsDir + "/" + situation + "/" + df.format(pic) + "_" + df.format(rightMethod) + ".tif";
    }

    protected int[][] methodArray;

    protected int remainderMethod = MAX_METHOD;

    protected void initMethodArray(int methods) {
        int total = (int) Stat.binomialCoeff(methods, 2);
        methodArray = new int[total][2];
        int index = 0;
        for (int x = 0; x < methods - 1; x++) {
            for (int y = x + 1; y < methods; y++) {
                methodArray[index][0] = x;
                methodArray[index][1] = y;
                index++;
            }
        }
    }

    public static void main(String[] args) {
        PsychophysicsExperimentFrame f = new PsychophysicsExperimentFrame();
        f.initMethodArray(3);
        System.out.println(Arrays.deepToString(f.methodArray));
    }

    protected boolean nextPicture() throws IOException {
        if (!start) {
            return false;
        }
        if (remainderMethod == 0) {
            if (pic + 1 >= MAX_PIC) {
                start = false;
                return false;
            } else {
                pic++;
                remainderMethod = MAX_METHOD;
                initMethodArray(MAX_METHOD);
            }
        }
        double r = Math.random() * remainderMethod;
        int selected = (int) r;
        if (Math.random() > .5) {
            leftMethod = methodArray[selected][0];
            rightMethod = methodArray[selected][1];
        } else {
            leftMethod = methodArray[selected][1];
            rightMethod = methodArray[selected][0];
        }
        System.arraycopy(methodArray[remainderMethod - 1], 0, methodArray[selected], 0, 2);
        remainderMethod--;
        jPanel_left.set(JAIUtils.getRenderedOp(getLeftPictureFilename()));
        jPanel_right.set(JAIUtils.getRenderedOp(getRightPictureFilename()));
        jLabel1.setText(++count + "/" + (int) (MAX_PIC * Stat.binomialCoeff(MAX_METHOD, 2)));
        return true;
    }

    public void jMenuItem1_actionPerformed(ActionEvent e) {
        ButtonModel selection = buttonGroup1.getSelection();
        if (selection != null) {
            situation = selection.getActionCommand();
            String name = this.jOptionPane1.showInputDialog(this, "�п�J�A���m�W", "");
            Calendar rightNow = Calendar.getInstance();
            String filename = rightNow.get(Calendar.YEAR) + df.format(rightNow.get(Calendar.MONTH) + 1) + df.format(rightNow.get(Calendar.DAY_OF_MONTH)) + "-" + df.format(rightNow.get(Calendar.HOUR_OF_DAY)) + "-" + df.format(rightNow.get(Calendar.MINUTE)) + "-" + df.format(rightNow.get(Calendar.SECOND)) + "_" + name;
            outputFilename = PsychophysicsDir + "/" + situation + "/result_" + filename + ".txt";
            try {
                fileWriter = new FileWriter(new File(outputFilename), false);
                start = true;
                nextPicture();
                jMenuItem_start.setEnabled(false);
                jButton_left.setEnabled(true);
                jButton_right.setEnabled(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
        }
    }
}

class PsychophysicsExperimentFrame_jMenuItem1_actionAdapter implements ActionListener {

    private PsychophysicsExperimentFrame adaptee;

    PsychophysicsExperimentFrame_jMenuItem1_actionAdapter(PsychophysicsExperimentFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jMenuItem1_actionPerformed(e);
    }
}

class PsychophysicsExperimentFrame_jButton2_actionAdapter implements ActionListener {

    private PsychophysicsExperimentFrame adaptee;

    PsychophysicsExperimentFrame_jButton2_actionAdapter(PsychophysicsExperimentFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton_right_actionPerformed(e);
    }
}

class PsychophysicsExperimentFrame_jButton1_actionAdapter implements ActionListener {

    private PsychophysicsExperimentFrame adaptee;

    PsychophysicsExperimentFrame_jButton1_actionAdapter(PsychophysicsExperimentFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton_left_actionPerformed(e);
    }
}

class PsychophysicsExperimentFrame_jMenuFileExit_ActionAdapter implements ActionListener {

    PsychophysicsExperimentFrame adaptee;

    PsychophysicsExperimentFrame_jMenuFileExit_ActionAdapter(PsychophysicsExperimentFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        adaptee.jMenuFileExit_actionPerformed(actionEvent);
    }
}

class PsychophysicsExperimentFrame_jMenuHelpAbout_ActionAdapter implements ActionListener {

    PsychophysicsExperimentFrame adaptee;

    PsychophysicsExperimentFrame_jMenuHelpAbout_ActionAdapter(PsychophysicsExperimentFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        adaptee.jMenuHelpAbout_actionPerformed(actionEvent);
    }
}
