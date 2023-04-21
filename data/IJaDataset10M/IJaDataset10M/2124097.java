package com.endfocus.projectbuilder;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.filechooser.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.plaf.metal.*;
import javax.help.*;
import gnu.regexp.*;
import com.endfocus.parser.java.*;
import com.endfocus.layout.*;
import com.endfocus.utilities.*;
import com.endfocus.projectbuilder.commands.*;
import com.endfocus.projectbuilder.members.*;
import com.endfocus.projectbuilder.console.*;

public class BuildPanel extends JPanel implements Runnable, ActionListener {

    private ProjectFrame mainApp;

    private EnvPanel envPanel;

    private JLabel icon;

    private Thread buildThread;

    private JToggleButton buildButton;

    private JButton timerButton, logButton;

    private JComboBox type;

    private String sourceRoot, targetRoot;

    private boolean runThread = true;

    private boolean buildActive = false;

    private boolean timerActive = false;

    private Date buildTimerTime = new Date();

    private JLabel timeLabel;

    private EmbeddedPythonInterpreter ip;

    private ProjectHolder buildRoot;

    private Member currentMember;

    private String targetDir;

    private Vector dirtyMembers, buildMembers;

    private boolean javaTargetDirSet = false;

    private boolean checkForDirty = false;

    private PrintStream outStream, errStream, msgStream;

    private JList logList;

    private Icon stop, pause, go, clock, clockgo, log, loggo;

    private String logFile;

    private PrintWriter logWriter = null;

    private boolean run = false;

    private boolean debug = false;

    private LogOutputStream errOut, msgOut, outOut;

    public BuildPanel(ProjectFrame app, EnvPanel aEnvPanel) {
        mainApp = app;
        envPanel = aEnvPanel;
        logFile = System.getProperty("install.root") + System.getProperty("file.separator") + "build.log";
        setMinimumSize(new Dimension(50, 50));
        setLayout(new BorderLayout());
        logList = new JList();
        logList.setModel(new DefaultListModel());
        logList.setCellRenderer(new LineRenderer());
        logList.setOpaque(true);
        logList.setBackground(new Color(255, 255, 204));
        MouseListener mouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = logList.locationToIndex(e.getPoint());
                    TaggedString taggedString = (TaggedString) logList.getModel().getElementAt(index);
                    if (taggedString.getTag() == TaggedString.ERROR) {
                        mainApp.openEditorForError(taggedString.toString());
                    }
                }
            }
        };
        logList.addMouseListener(mouseListener);
        outOut = new LogOutputStream(logList, TaggedString.TEXT);
        errOut = new LogOutputStream(logList, TaggedString.ALERT);
        msgOut = new LogOutputStream(logList, TaggedString.MESSAGE);
        outStream = new PrintStream(outOut);
        errStream = new PrintStream(errOut);
        msgStream = new PrintStream(msgOut);
        JPanel aPanel = new JPanel();
        aPanel.setLayout(new SpringLayout());
        icon = new JLabel("");
        icon.setBounds(2, 2, 60, 80);
        aPanel.add(icon);
        stop = com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/stop.gif");
        pause = com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/pause.gif");
        go = com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/play.gif");
        clock = com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/clock.gif");
        clockgo = com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/clockgo.gif");
        log = com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/document.gif");
        loggo = com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/documentgo.gif");
        type = new JComboBox();
        type.addItem("Build & Run");
        type.addItem("Build All");
        type.addItem("Build Dirty");
        type.addItem("Build & Debug");
        type.setToolTipText("Set Build Type");
        type.setBounds(70, 20, 100, 30);
        aPanel.add(type);
        buildButton = new JToggleButton(go);
        CSH.setHelpIDString(buildButton, "build.build");
        buildButton.setSelectedIcon(stop);
        buildButton.setToolTipText("Start/Stop Build");
        buildButton.setActionCommand("StartBuild");
        buildButton.addActionListener(this);
        buildButton.setBounds(180, 20, 60, 30);
        aPanel.add(buildButton);
        timerButton = new JButton(clock);
        CSH.setHelpIDString(timerButton, "build.timer");
        timerButton.setToolTipText("Set Timer Build");
        timerButton.setActionCommand("SetTimer");
        timerButton.addActionListener(this);
        timerButton.setBounds(240, 20, 60, 30);
        aPanel.add(timerButton);
        logButton = new JButton(log);
        CSH.setHelpIDString(logButton, "build.log");
        logButton.setToolTipText("Set Log File");
        logButton.setActionCommand("SetLog");
        logButton.addActionListener(this);
        logButton.setBounds(300, 20, 60, 30);
        aPanel.add(logButton);
        timeLabel = new JLabel(" ");
        timeLabel.setBounds(100, 55, 250, 20);
        aPanel.add(timeLabel);
        aPanel.setBounds(0, 0, 250, 70);
        add(aPanel, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(logList);
        sp.getViewport().setBackingStoreEnabled(true);
        add(sp, BorderLayout.CENTER);
    }

    public void logOuput(String output) {
        outStream.print(output + "\n");
    }

    public void logMessage(String aMessage) {
        msgStream.print(aMessage + "\n");
    }

    public void logAlert(String anAlert) {
        errStream.print(anAlert + "\n");
    }

    public PrintStream getOutStream() {
        return outStream;
    }

    public PrintStream getLogStream() {
        return msgStream;
    }

    public PrintStream getAlertStream() {
        return errStream;
    }

    public void compileMembers(Vector members) {
        buildRoot = null;
        buildMembers = members;
        checkForDirty = false;
        run = false;
        startBuild();
    }

    public void buildDirty() {
        buildRoot = mainApp.getRoot();
        buildMembers = new Vector();
        checkForDirty = true;
        run = false;
        debug = false;
        startBuild();
    }

    public void buildAll() {
        buildProject(mainApp.getRoot());
        checkForDirty = false;
        run = false;
        debug = false;
    }

    public void buildAndRun() {
        buildRoot = mainApp.getRoot();
        buildMembers = new Vector();
        checkForDirty = true;
        run = true;
        debug = false;
        startBuild();
    }

    public void buildAndDebug() {
        buildRoot = mainApp.getRoot();
        buildMembers = new Vector();
        checkForDirty = true;
        run = false;
        debug = true;
        startBuild();
    }

    public void buildProject(ProjectHolder aHolder) {
        buildRoot = aHolder;
        buildMembers = new Vector();
        checkForDirty = false;
        startBuild();
    }

    protected void startBuild() {
        ip = new EmbeddedPythonInterpreter();
        ((DefaultListModel) logList.getModel()).removeAllElements();
        Date current = new Date();
        if (current.after(buildTimerTime)) {
            timerActive = false;
            timerButton.setIcon(clock);
        }
        buildButton.setActionCommand("StopBuild");
        buildButton.setSelected(true);
        buildActive = true;
        buildThread = new Thread(this);
        buildThread.setPriority(Thread.MIN_PRIORITY);
        buildThread.start();
    }

    protected void stopBuild() {
        buildButton.setIcon(pause);
        timeLabel.setText("Waiting for current task to finish");
        Environment aEnv = ip.getEnvironment();
        aEnv.setContinueBuild(false);
        buildButton.setActionCommand("");
        buildActive = false;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("StartBuild") == 0) {
            switch(type.getSelectedIndex()) {
                case 0:
                    buildAndRun();
                    break;
                case 1:
                    buildAll();
                    break;
                case 2:
                    buildDirty();
                    break;
                case 3:
                    buildAndDebug();
                    break;
            }
            return;
        }
        if (e.getActionCommand().compareTo("StopBuild") == 0) {
            stopBuild();
            buildButton.setEnabled(false);
            return;
        }
        if (e.getActionCommand().compareTo("SetLog") == 0) {
            File aFile = new File(logFile);
            JFileChooser chooser = mainApp.getFileChooser("Set Log File");
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (aFile != null) chooser.setCurrentDirectory(aFile);
            int rc = chooser.showDialog(mainApp.getProjectFrame(), "Set Log");
            if (rc == JFileChooser.APPROVE_OPTION) {
                logFile = chooser.getSelectedFile().getAbsolutePath();
            }
            return;
        }
        if (e.getActionCommand().compareTo("SetTimer") == 0) {
            String buildTime = JOptionPane.showInputDialog(this, "Enter Build Time (dd/mm/yyyy HH:MM)");
            try {
                if (buildTime.length() == 0) {
                    timerButton.setIcon(clock);
                    return;
                }
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy H:mm");
                df.setTimeZone(TimeZone.getDefault());
                buildTimerTime = df.parse(buildTime);
                Calendar c = Calendar.getInstance();
                c.setTime(buildTimerTime);
                buildTimerTime = c.getTime();
                timeLabel.setText("Build Start : " + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG, Locale.getDefault()).format(buildTimerTime));
                timerButton.setIcon(clockgo);
                timerActive = true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Time Entered");
                timerButton.setIcon(clock);
                timerActive = false;
                timeLabel.setText(" ");
            }
            return;
        }
    }

    public void run() {
        mainApp.showStatus("Setting Build Environment");
        ip.newEnvironment();
        Environment aEnv = ip.getEnvironment();
        aEnv.setPanel(this);
        envPanel.fillEnvironment(aEnv.getBuildEnv());
        envPanel.fillJavaTools(aEnv.getJavaTools(), outStream);
        envPanel.fillOtherTools(aEnv.getOtherTools());
        String outDir = (String) aEnv.getBuildEnv().get("OutputDirectory");
        if (outDir.length() > 0) {
            File outFile = new File(outDir);
            if (!outFile.exists()) {
                if (JOptionPane.showConfirmDialog(mainApp.getProjectFrame(), "Output Directory do not exist\n" + outDir + "\nDo you want to create it?'", "Create Output Directory", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) outFile.mkdirs();
            }
        }
        if (timerActive) {
            logMessage("Waiting...");
            buildButton.setSelectedIcon(pause);
        }
        Calendar cc = Calendar.getInstance();
        while (buildActive && timerActive) {
            Date current = new Date();
            cc.setTime(current);
            current = cc.getTime();
            if (current.after(buildTimerTime)) break;
            try {
                Thread.currentThread().sleep(5 * 1000);
            } catch (Exception ex) {
                break;
            }
        }
        buildButton.setSelectedIcon(stop);
        if (buildActive) {
            try {
                logWriter = new PrintWriter(new FileWriter(logFile));
                outOut.setLogWriter(logWriter);
                errOut.setLogWriter(logWriter);
                msgOut.setLogWriter(logWriter);
            } catch (Exception ex) {
                logWriter = null;
            }
            logMessage("Begin Build Process");
            icon.setIcon(com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/runner.gif"));
            icon.repaint();
            javaTargetDirSet = false;
            targetDir = (String) aEnv.getBuildEnv().get("OutputDirectory");
            try {
                if (targetDir.length() > 0) {
                    File aFile = new File(targetDir);
                    if (aFile.isDirectory()) {
                        targetDir = aFile.getAbsolutePath();
                        javaTargetDirSet = true;
                    } else javaTargetDirSet = false;
                } else javaTargetDirSet = false;
            } catch (Exception ex) {
                targetDir = buildRoot.getPath();
            }
            logMessage("Target Dir : " + targetDir);
            if (checkForDirty) logMessage("Scanning for dirty files"); else logMessage("Scanning Project");
            mainApp.showStatus("Scanning Project");
            dirtyMembers = aEnv.getDirtyMembers();
            if (buildRoot != null) {
                checkMembers(buildRoot, aEnv.getAllMembers());
                checkProjectMembers(buildRoot, aEnv.getProjectMembers());
            } else {
                Vector allMembers = aEnv.getAllMembers();
                for (Enumeration e = buildMembers.elements(); e.hasMoreElements(); ) allMembers.addElement(e.nextElement());
            }
            for (Enumeration e = buildMembers.elements(); e.hasMoreElements(); ) dirtyMembers.addElement(e.nextElement());
            try {
                StringBuffer aScript = new StringBuffer();
                BufferedReader aReader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("scripts/rules.py")));
                while (true) {
                    String aLine = aReader.readLine();
                    if (aLine == null) break;
                    aScript.append(aLine + "\n");
                }
                logMessage("Loading Build Environment");
                ip.run(aScript.toString());
            } catch (Exception ex) {
                mainApp.showStatus("Problem loading Build Rules : " + ex.toString());
                System.err.println();
            }
            Enumeration e = envPanel.getBuildScripts();
            if (e.hasMoreElements() == false) {
                logAlert("No Build Script selected");
            } else {
                for (; e.hasMoreElements(); ) {
                    if (!buildActive) break;
                    String currentScript = (String) e.nextElement();
                    mainApp.showStatus("Executing Build Script : " + currentScript);
                    ip.runScript(currentScript);
                }
            }
            mainApp.showStatus("Build Process End");
            logMessage("Build Process End");
            icon.setIcon(null);
            if (run) {
                Enumeration e2 = envPanel.getRunScripts();
                for (; e2.hasMoreElements(); ) {
                    if (!buildActive) break;
                    String currentScript = (String) e2.nextElement();
                    mainApp.showStatus("Executing Run Script : " + currentScript);
                    ip.runScript(currentScript);
                }
            }
            if (debug) {
                Hashtable table = new Hashtable();
                Hashtable table2 = new Hashtable();
                envPanel.fillEnvironment(table2);
                table.put("options", "-classpath \"" + table2.get("Classpath") + "\" " + table2.get("JavaOptions"));
                table.put("main", table2.get("MainClass"));
            }
            buildButton.setSelected(false);
            buildButton.setActionCommand("StartBuild");
            if (logWriter != null) logWriter.close();
        }
        icon.setIcon(null);
        buildActive = false;
        run = false;
        timeLabel.setText(" ");
        timerButton.setIcon(com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/clock.gif"));
        buildButton.setIcon(go);
        aEnv.getJavaTools().clear();
        ip = null;
        aEnv = null;
        System.gc();
        buildButton.setActionCommand("StartBuild");
        buildButton.setEnabled(true);
    }

    protected void setupEnv() {
    }

    /**
	 * Parser callback to determine the package name of the java source file.
	 *
	 * @param   args  
	 */
    public void packageDeclaration(Object args[]) {
        StringBuffer aPath;
        if (javaTargetDirSet) {
            aPath = new StringBuffer(targetDir + System.getProperty("file.separator"));
            for (int n = 0; n < args.length; n++) aPath.append(args[n] + System.getProperty("file.separator"));
            aPath.append(currentMember.getName().substring(0, currentMember.getName().length() - 5) + ".class");
        } else aPath = new StringBuffer(currentMember.getPath().substring(0, currentMember.getPath().length() - 5) + ".class");
        File targetFile = new File(aPath.toString());
        if (targetFile.exists() == false) {
            dirtyMembers.addElement(currentMember);
            return;
        }
        File sourceFile = new File(currentMember.getPath());
        if (sourceFile.exists() == false) {
            return;
        }
        if (targetFile.lastModified() < sourceFile.lastModified()) dirtyMembers.addElement(currentMember); else {
        }
    }

    /**
	 * Check for dirty members
	 *
	 * @param   aHolder  
	 * @param   allMembers  
	 */
    private void checkMembers(ProjectHolder aHolder, Vector allMembers) {
        Vector members = aHolder.getMembers();
        String sourcePath = aHolder.getPath();
        JavaParser aParser = null;
        for (Enumeration e = members.elements(); e.hasMoreElements(); ) {
            Member aMember = (Member) e.nextElement();
            if (aMember.getStatus() == Member.notFound) continue;
            aMember.setStatus(Member.none);
            if (!(aMember instanceof ProjectHolder)) {
                allMembers.addElement(aMember);
                if (!checkForDirty) {
                    dirtyMembers.addElement(aMember);
                    continue;
                }
                if (aMember.getPath().endsWith(".java")) {
                    currentMember = aMember;
                    mainApp.showStatus("Scanning Project: " + currentMember.getPath());
                    if (aParser == null) {
                        try {
                            aParser = new JavaParser(new FileReader(aMember.getPath()));
                            aParser.setDelegate(this);
                            aParser.parse();
                        } catch (Throwable ex) {
                            System.out.println(ex);
                            aParser = null;
                            dirtyMembers.addElement(currentMember);
                            continue;
                        }
                    } else {
                        try {
                            aParser.parse(new FileReader(aMember.getPath()));
                        } catch (Throwable ex) {
                            System.out.println(ex);
                            aParser = null;
                            dirtyMembers.addElement(currentMember);
                            continue;
                        }
                    }
                } else dirtyMembers.addElement(aMember);
            }
        }
        Vector subProjects = aHolder.getSubprojects();
        for (Enumeration e = subProjects.elements(); e.hasMoreElements(); ) {
            checkMembers((ProjectHolder) e.nextElement(), allMembers);
        }
    }

    /**
	 * Check for project members
	 *
	 * @param   aHolder  
	 * @param   allMembers  
	 */
    private void checkProjectMembers(ProjectHolder aHolder, Vector allMembers) {
        allMembers.addElement(aHolder);
        for (Enumeration e = aHolder.getSubprojects().elements(); e.hasMoreElements(); ) {
            checkProjectMembers((ProjectHolder) e.nextElement(), allMembers);
        }
    }
}
