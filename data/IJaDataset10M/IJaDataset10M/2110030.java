package audimus.eartrainer;

import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.io.*;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.border.*;

public class MainFrame extends JFrame implements ActionListener {

    protected Scores topscore = new Scores();

    private JPanel contentPane;

    private NavigationBar navigation;

    private BorderLayout borderLayout1 = new BorderLayout();

    private JMenuBar jMenuBar1 = new JMenuBar();

    private JMenu jMenuAudix = new JMenu();

    private JMenu jMenuFile = new JMenu();

    private JMenuItem jMenuFileOpen = new JMenuItem();

    private JMenuItem jMenuFileSave = new JMenuItem();

    private JMenuItem jMenuFileDefault = new JMenuItem();

    private JMenuItem jMenuFileResults = new JMenuItem();

    private JMenuItem jMenuAudixExit = new JMenuItem();

    private JMenu jMenuHelp = new JMenu();

    private JMenuItem jMenuHelpAbout = new JMenuItem();

    private JMenuItem jMenuHelpInfo = new JMenuItem();

    private JMenuItem jMenuHelpWeb = new JMenuItem();

    private JMenuItem jMenuSettings = new JMenuItem();

    private JMenuItem jMenuAudixStop = new JMenuItem();

    protected Midi midi;

    private JPanel workingArea;

    private SettingsFrame dlg;

    private Results res;

    int[] nTest;

    int[] pressedTest;

    int pos;

    boolean test = false;

    boolean result[][];

    private MusicStrings mstrings;

    private void buttonsOff() {
        navigation.navOff();
        jMenuFileDefault.setEnabled(false);
        jMenuFileOpen.setEnabled(false);
        jMenuFileSave.setEnabled(false);
        jMenuFileResults.setEnabled(false);
        jMenuSettings.setEnabled(false);
    }

    private void buttonsOn() {
        navigation.navOn();
        jMenuFileDefault.setEnabled(true);
        jMenuFileOpen.setEnabled(true);
        jMenuFileSave.setEnabled(true);
        jMenuFileResults.setEnabled(true);
        jMenuSettings.setEnabled(true);
    }

    public void setTest(int[] aant) {
        buttonsOff();
        test = true;
        nTest = aant;
        pos = 0;
        result = new boolean[4][50];
        pressedTest = new int[50];
        jMenuAudixStop.setEnabled(true);
        if (nTest[0] != 0) {
            loadNotes();
        } else {
            if (nTest[1] != 0) {
                loadInterval();
                pos = 1;
            } else {
                if (nTest[2] != 0) {
                    loadScales();
                    pos = 2;
                } else {
                    loadChords();
                    pos = 3;
                }
            }
        }
    }

    public boolean getTest() {
        return test;
    }

    public void stopTest() {
        buttonsOn();
        jMenuAudixStop.setEnabled(false);
        test = false;
        result = null;
        pressedTest = null;
    }

    public void setResult(boolean resultaat) {
        if (test) {
            result[pos][pressedTest[pos]] = resultaat;
        }
    }

    public void testPressed() {
        if (test) {
            if (pressedTest[pos] >= nTest[pos] - 1) {
                pos++;
                String strRapport;
                switch(pos) {
                    case 1:
                        strRapport = mstrings.getResult(0) + mstrings.getTitle(0) + ":\n";
                        for (int i = 0; i < nTest[0]; i++) {
                            if (result[0][i]) {
                                strRapport += mstrings.getResult(1) + String.valueOf(i + 1);
                                strRapport += ": " + mstrings.getResult(2) + "\n";
                            } else {
                                strRapport += mstrings.getResult(1) + String.valueOf(i + 1);
                                strRapport += ": " + mstrings.getResult(3) + "\n";
                            }
                        }
                        JOptionPane.showMessageDialog(getContentPane(), strRapport);
                        if (nTest[1] != 0) {
                            loadInterval();
                        } else {
                            pos++;
                            if (nTest[2] != 0) {
                                loadScales();
                            } else {
                                pos++;
                                if (nTest[3] != 0) {
                                    loadChords();
                                } else {
                                    loadTest(result);
                                }
                            }
                        }
                        break;
                    case 2:
                        strRapport = mstrings.getResult(0) + mstrings.getTitle(1) + ":\n";
                        for (int i = 0; i < nTest[1]; i++) {
                            if (result[1][i]) {
                                strRapport += mstrings.getResult(1) + String.valueOf(i + 1);
                                strRapport += ": " + mstrings.getResult(2) + "\n";
                            } else {
                                strRapport += mstrings.getResult(1) + String.valueOf(i + 1);
                                strRapport += ": " + mstrings.getResult(3) + "\n";
                            }
                        }
                        JOptionPane.showMessageDialog(getContentPane(), strRapport);
                        if (nTest[2] != 0) {
                            loadScales();
                        } else {
                            pos++;
                            if (nTest[3] != 0) {
                                loadChords();
                            } else {
                                loadTest(result);
                            }
                        }
                        break;
                    case 3:
                        strRapport = mstrings.getResult(0) + mstrings.getTitle(2) + ":\n";
                        for (int i = 0; i < nTest[2]; i++) {
                            if (result[2][i]) {
                                strRapport += mstrings.getResult(1) + String.valueOf(i + 1);
                                strRapport += ": " + mstrings.getResult(2) + "\n";
                            } else {
                                strRapport += mstrings.getResult(1) + String.valueOf(i + 1);
                                strRapport += ": " + mstrings.getResult(3) + "\n";
                            }
                        }
                        JOptionPane.showMessageDialog(getContentPane(), strRapport);
                        if (nTest[3] != 0) {
                            loadChords();
                        } else {
                            loadTest(result);
                        }
                        break;
                    case 4:
                        strRapport = mstrings.getResult(0) + mstrings.getTitle(3) + ":\n";
                        for (int i = 0; i < nTest[3]; i++) {
                            if (result[3][i]) {
                                strRapport += mstrings.getResult(1) + String.valueOf(i + 1);
                                strRapport += ": " + mstrings.getResult(2) + "\n";
                            } else {
                                strRapport += mstrings.getResult(1) + String.valueOf(i + 1);
                                strRapport += ": " + mstrings.getResult(3) + "\n";
                            }
                        }
                        JOptionPane.showMessageDialog(getContentPane(), strRapport);
                        loadTest(result);
                    default:
                        break;
                }
            } else {
                pressedTest[pos]++;
                System.out.println(pressedTest[pos]);
            }
        }
    }

    public void reload() {
        Class klasse = workingArea.getClass();
        if (klasse == Notes.class) {
            loadNotes();
        }
        if (klasse == Intervals.class) {
            loadInterval();
        }
        if (klasse == Chords.class) {
            loadChords();
        }
        if (klasse == Scales.class) {
            loadScales();
        }
        if (klasse == Test.class) {
            loadTest();
        }
        mstrings = midi.settings.getMuziekStrings();
        setTitle(mstrings.getTitle(5));
        jMenuAudix.setText(mstrings.getMenu(0));
        jMenuAudixExit.setText(mstrings.getMenu(1));
        jMenuSettings.setText(mstrings.getMenu(2));
        jMenuFile.setText(mstrings.getMenu(3));
        jMenuFileSave.setText(mstrings.getMenu(4));
        jMenuFileOpen.setText(mstrings.getMenu(5));
        jMenuFileResults.setText(mstrings.getMenu(6));
        jMenuFileDefault.setText(mstrings.getMenu(7));
        jMenuAudixStop.setText(mstrings.getMenu(8));
        jMenuHelp.setText(mstrings.getMenu(9));
        jMenuHelpAbout.setText(mstrings.getMenu(10));
        jMenuHelpInfo.setText(mstrings.getMenu(11));
        jMenuHelpWeb.setText(mstrings.getMenu(14));
        navigation.reload();
    }

    public MainFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        midi = new Midi();
        mstrings = midi.settings.getMuziekStrings();
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout1);
        setSize(new Dimension(800, 600));
        setResizable(false);
        setTitle(mstrings.getTitle(5));
        contentPane.setBackground(Color.WHITE);
        navigation = new NavigationBar(this);
        workingArea = new Notes(this);
        contentPane.add(navigation, BorderLayout.NORTH);
        contentPane.add(workingArea, BorderLayout.CENTER);
        workingArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.WHITE));
        loadScores();
        WindowListener listener = new WindowListener() {

            public void windowClosing(WindowEvent e) {
                midi.closeDevice();
                writeScore();
                System.exit(0);
            }

            public void windowActivated(WindowEvent w) {
            }

            public void windowClosed(WindowEvent w) {
                midi.closeDevice();
            }

            public void windowDeactivated(WindowEvent w) {
            }

            public void windowDeiconified(WindowEvent w) {
            }

            public void windowIconified(WindowEvent w) {
            }

            public void windowOpened(WindowEvent w) {
            }
        };
        addWindowListener(listener);
        jMenuAudix.setText(mstrings.getMenu(0));
        jMenuAudixExit.setText(mstrings.getMenu(1));
        jMenuSettings.setText(mstrings.getMenu(2));
        jMenuSettings.addActionListener(new settingsAdapter(this));
        jMenuFile.setText(mstrings.getMenu(3));
        jMenuFileSave.setText(mstrings.getMenu(4));
        jMenuFileOpen.setText(mstrings.getMenu(5));
        jMenuFileResults.setText(mstrings.getMenu(6));
        jMenuFileDefault.setText(mstrings.getMenu(7));
        jMenuAudixStop.setText(mstrings.getMenu(8));
        jMenuHelp.setText(mstrings.getMenu(9));
        jMenuHelpAbout.setText(mstrings.getMenu(10));
        jMenuHelpInfo.setText(mstrings.getMenu(13));
        jMenuHelpWeb.setText(mstrings.getMenu(14));
        jMenuFile.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuAudix.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuAudixExit.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuSettings.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuFile.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuFileSave.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuFileOpen.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuFileResults.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuFileDefault.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuAudixStop.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuHelp.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuHelpAbout.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuHelpInfo.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuHelpWeb.setFont(new Font("Verdana", Font.PLAIN, 10));
        jMenuAudixStop.setEnabled(false);
        jMenuFileSave.addActionListener(new Hoofdvenster_jMenuFileSave_ActionAdapter(this));
        jMenuFileOpen.addActionListener(new Hoofdvenster_jMenuFileOpen_ActionAdapter(this));
        jMenuFileResults.addActionListener(new Hoofdvenster_jMenuFileResults_ActionAdapter(this));
        jMenuFileDefault.addActionListener(new Hoofdvenster_jMenuFileDefault_ActionAdapter(this));
        jMenuAudixExit.addActionListener(new Hoofdvenster_jMenuAudixExit_ActionAdapter(this));
        jMenuAudixStop.addActionListener(new Hoofdvenster_jMenuAudixStop_ActionAdapter(this));
        jMenuHelpAbout.addActionListener(new Hoofdvenster_jMenuHelpAbout_ActionAdapter(this));
        jMenuHelpInfo.addActionListener(new Hoofdvenster_jMenuHelpInfo_ActionAdapter(this));
        jMenuHelpWeb.addActionListener(new Hoofdvenster_jMenuHelpWeb_ActionAdapter(this));
        jMenuBar1.add(jMenuAudix);
        jMenuAudix.add(jMenuSettings);
        jMenuAudix.add(jMenuAudixStop);
        jMenuAudix.add(jMenuFileResults);
        jMenuAudix.add(jMenuAudixExit);
        jMenuFile.add(jMenuFileOpen);
        jMenuFile.add(jMenuFileSave);
        jMenuFile.add(jMenuFileDefault);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenuHelp);
        jMenuHelp.add(jMenuHelpWeb);
        jMenuHelp.add(jMenuHelpAbout);
        setJMenuBar(jMenuBar1);
    }

    void clearScreen() {
        contentPane.remove(workingArea);
        contentPane.repaint();
        workingArea = null;
    }

    void fillScreen() {
        contentPane.add(workingArea, BorderLayout.CENTER);
        contentPane.validate();
    }

    void loadNotes() {
        clearScreen();
        workingArea = new Notes(this);
        fillScreen();
        workingArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.WHITE));
    }

    void loadInterval() {
        clearScreen();
        workingArea = new Intervals(this);
        fillScreen();
        workingArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.WHITE));
    }

    void loadScales() {
        clearScreen();
        workingArea = new Scales(this);
        fillScreen();
        workingArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.WHITE));
    }

    void loadChords() {
        clearScreen();
        workingArea = new Chords(this);
        fillScreen();
        workingArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.WHITE));
    }

    void loadTest() {
        System.out.println(workingArea.getClass());
        clearScreen();
        workingArea = new Test(this);
        fillScreen();
        workingArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.WHITE));
    }

    void loadTest(boolean[][] result) {
        clearScreen();
        workingArea = new Test(this, result);
        fillScreen();
        workingArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.WHITE));
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    void settingsPerformed(ActionEvent actionEvent) {
        dlg = new SettingsFrame(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    void jMenuFileOpen_actionPerformed(ActionEvent actionEvent) {
        FileDialog filedialog = new FileDialog(this, mstrings.getMenu(11), FileDialog.LOAD);
        filedialog.setVisible(true);
        String filenaam = filedialog.getFile();
        String directorynaam = filedialog.getDirectory();
        if (filenaam != null) {
            try {
                ObjectInputStream in;
                Object temp;
                in = new ObjectInputStream(new FileInputStream(directorynaam + filenaam));
                temp = in.readObject();
                in.close();
                midi.settings = (Settings) temp;
                reload();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    void jMenuFileSave_actionPerformed(ActionEvent actionEvent) {
        FileDialog filedialog = new FileDialog(this, mstrings.getMenu(12), FileDialog.SAVE);
        filedialog.setVisible(true);
        String filenaam = filedialog.getFile();
        String directorynaam = filedialog.getDirectory();
        if (filenaam != null) {
            try {
                ObjectOutputStream uit;
                uit = new ObjectOutputStream(new FileOutputStream(directorynaam + filenaam));
                uit.writeObject(midi.settings);
                uit.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void jMenuFileResults_actionPerformed(ActionEvent actionEvent) {
        res = new Results(this);
        Dimension dlgSize = res.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        res.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        res.setModal(true);
        res.pack();
        res.setVisible(true);
    }

    void jMenuFileDefault_actionPerformed(ActionEvent actionEvent) {
        String filenaam = System.getProperty("user.dir") + "/default";
        if (filenaam != null) {
            try {
                ObjectOutputStream uit;
                uit = new ObjectOutputStream(new FileOutputStream(filenaam));
                uit.writeObject(midi.settings);
                uit.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void jMenuAudimusStop_actionPerformed(ActionEvent actionEvent) {
        stopTest();
        loadTest();
    }

    void writeScore() {
        String filenaam = System.getProperty("user.dir") + "/top";
        if (filenaam != null) {
            try {
                ObjectOutputStream uit;
                uit = new ObjectOutputStream(new FileOutputStream(filenaam));
                uit.writeObject(topscore);
                uit.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void jMenuAudimusExit_actionPerformed(ActionEvent actionEvent) {
        dispose();
        writeScore();
    }

    void jMenuHelpAbout_actionPerformed(ActionEvent actionEvent) {
        AboutBox dlg = new AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    void jMenuHelpInfo_actionPerformed(ActionEvent actionEvent) {
        URI uri = null;
        try {
            uri = new URI("help.html");
            Desktop.getDesktop().browse(uri);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (URISyntaxException ioe) {
            ioe.printStackTrace();
        }
    }

    void jMenuHelpWeb_actionPerformed(ActionEvent actionEvent) {
        URI uri = null;
        try {
            uri = new URI("http://www.audimus.be");
            Desktop.getDesktop().browse(uri);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (URISyntaxException ioe) {
            ioe.printStackTrace();
        }
    }

    void loadScores() {
        String filenaam = System.getProperty("user.dir") + "/top";
        if (filenaam != null) {
            ObjectInputStream in;
            try {
                in = new ObjectInputStream(new FileInputStream(filenaam));
                topscore = (Scores) in.readObject();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                String strMessage = mstrings.getBegin();
                JOptionPane.showMessageDialog(getContentPane(), "This is the first time you run Audimus. You can select your language in the settings screen.");
                dlg = new SettingsFrame(this);
                Dimension dlgSize = dlg.getPreferredSize();
                Dimension frmSize = getSize();
                Point loc = getLocation();
                dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
                dlg.setModal(true);
                dlg.pack();
                dlg.setVisible(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    class Hoofdvenster_jMenuAudixExit_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuAudixExit_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuAudimusExit_actionPerformed(actionEvent);
        }
    }

    class Hoofdvenster_jMenuAudixStop_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuAudixStop_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuAudimusStop_actionPerformed(actionEvent);
        }
    }

    class Hoofdvenster_jMenuHelpAbout_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuHelpAbout_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuHelpAbout_actionPerformed(actionEvent);
        }
    }

    class Hoofdvenster_jMenuHelpInfo_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuHelpInfo_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuHelpInfo_actionPerformed(actionEvent);
        }
    }

    class Hoofdvenster_jMenuHelpWeb_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuHelpWeb_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuHelpWeb_actionPerformed(actionEvent);
        }
    }

    class Hoofdvenster_jMenuFileOpen_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuFileOpen_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuFileOpen_actionPerformed(actionEvent);
        }
    }

    class Hoofdvenster_jMenuFileSave_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuFileSave_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuFileSave_actionPerformed(actionEvent);
        }
    }

    class Hoofdvenster_jMenuFileResults_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuFileResults_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuFileResults_actionPerformed(actionEvent);
        }
    }

    class Hoofdvenster_jMenuFileDefault_ActionAdapter implements ActionListener {

        MainFrame adaptee;

        Hoofdvenster_jMenuFileDefault_ActionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.jMenuFileDefault_actionPerformed(actionEvent);
        }
    }

    class settingsAdapter implements ActionListener {

        MainFrame adaptee;

        settingsAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            adaptee.settingsPerformed(actionEvent);
        }
    }
}
