package ds.nfcip.tests.me;

import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import ds.nfcip.NFCIPException;
import ds.nfcip.NFCIPInterface;
import ds.nfcip.tests.NFCIPTest;
import ds.nfcip.NFCIPUtils;
import ds.nfcip.me.NFCIPConnection;

public class TestMIDlet extends MIDlet implements Runnable, CommandListener {

    private Display display;

    private Form form;

    private Thread thread;

    private List menu;

    private List choose;

    private TextField numberOfRunsField;

    private TextField minDataLengthField;

    private TextField maxDataLengthField;

    private TextField statusField;

    private static final Command backCommand = new Command("Back", Command.BACK, 0);

    private static final Command exitCommand = new Command("Exit", Command.STOP, 2);

    private String currentMenu;

    private int mode;

    private int numberOfRuns;

    private int minDataLength;

    private int maxDataLength;

    private int logLevel;

    private static final int S_MODE = 1;

    private static final int S_NUMBER_OF_RUNS = 2;

    private static final int S_MIN_DATA_LENGTH = 3;

    private static final int S_MAX_DATA_LENGTH = 4;

    private static final int S_LOG_LEVEL = 5;

    PersistentSettings ps = null;

    private static PrintStream printStream = null;

    private NFCIPConnection m = null;

    public TestMIDlet() {
        ps = new PersistentSettings();
        if (ps.getNumberOfSettings() == 0) {
            ps.addSetting(NFCIPInterface.FAKE_TARGET);
            ps.addSetting(1);
            ps.addSetting(200);
            ps.addSetting(300);
            ps.addSetting(0);
        }
        mode = ps.getSetting(S_MODE);
        numberOfRuns = ps.getSetting(S_NUMBER_OF_RUNS);
        minDataLength = ps.getSetting(S_MIN_DATA_LENGTH);
        maxDataLength = ps.getSetting(S_MAX_DATA_LENGTH);
        logLevel = ps.getSetting(S_LOG_LEVEL);
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        notifyDestroyed();
    }

    protected void pauseApp() {
        display = null;
        choose = null;
        menu = null;
        form = null;
    }

    protected void startApp() throws MIDletStateChangeException {
        display = Display.getDisplay(this);
        menu = new List("NFCIP MIDlet Parameters", Choice.IMPLICIT);
        menu.append("Set Mode", null);
        menu.append("Set Number Of Runs", null);
        menu.append("Set Test Range", null);
        menu.append("Set Logging", null);
        menu.append("Start", null);
        menu.addCommand(exitCommand);
        menu.setCommandListener(this);
        mainMenu();
    }

    private void mainMenu() {
        display.setCurrent(menu);
        currentMenu = "main";
    }

    private void chooseMode() {
        choose = new List("Set Mode", Choice.EXCLUSIVE);
        choose.addCommand(backCommand);
        choose.setCommandListener(this);
        choose.append("Initiator", null);
        choose.append("Target", null);
        choose.append("Fake Initiator", null);
        choose.append("Fake Target", null);
        choose.setSelectedIndex(mode, true);
        display.setCurrent(choose);
        currentMenu = "setMode";
    }

    private void chooseNumberOfRuns() {
        numberOfRunsField = new TextField("Number Of Runs:", Integer.toString(numberOfRuns), 3, TextField.DECIMAL);
        form = new Form("Set Number Of Runs");
        form.append(numberOfRunsField);
        form.addCommand(backCommand);
        form.setCommandListener(this);
        display.setCurrent(form);
        currentMenu = "setNumberOfRuns";
    }

    private void chooseTestRange() {
        minDataLengthField = new TextField("Minimum Data Length:", Integer.toString(minDataLength), 5, TextField.DECIMAL);
        maxDataLengthField = new TextField("Maximum Data Length:", Integer.toString(maxDataLength), 5, TextField.DECIMAL);
        form = new Form("Set Test Range");
        form.append(minDataLengthField);
        form.append(maxDataLengthField);
        form.addCommand(backCommand);
        form.setCommandListener(this);
        display.setCurrent(form);
        currentMenu = "setTestRange";
    }

    private void chooseLogging() {
        choose = new List("Set Log Level", Choice.EXCLUSIVE);
        choose.addCommand(backCommand);
        choose.setCommandListener(this);
        choose.append("Disabled", null);
        choose.append("Level 1", null);
        choose.append("Level 2", null);
        choose.append("Level 3", null);
        choose.append("Level 4", null);
        choose.append("Level 5", null);
        choose.setSelectedIndex(logLevel, true);
        display.setCurrent(choose);
        currentMenu = "setLogging";
    }

    private void startConnection() {
        form = new Form("NFCIPTest MIDlet");
        TextField t = new TextField("Configuration", "(numberOfRuns = " + numberOfRuns + ", minDataLength = " + minDataLength + ", maxDataLength = " + maxDataLength + ", mode = " + NFCIPUtils.modeToString(mode) + ")", 100, TextField.ANY | TextField.UNEDITABLE);
        statusField = new TextField("Status", "Waiting...", 50, TextField.ANY | TextField.UNEDITABLE);
        form.append(t);
        form.append(statusField);
        form.addCommand(backCommand);
        form.setCommandListener(this);
        display.setCurrent(form);
        currentMenu = "runningTest";
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        if (logLevel > 0) {
            try {
                FileConnection filecon = (FileConnection) Connector.open("file:///E:/NFCIP-logfile.txt");
                if (!filecon.exists()) {
                    filecon.create();
                } else {
                    filecon.delete();
                    filecon.create();
                }
                printStream = new PrintStream(filecon.openOutputStream());
            } catch (Exception e) {
            }
        }
        try {
            statusField.setString("Waiting...");
            m = new NFCIPConnection();
            m.setLogging(printStream, logLevel);
            m.setMode(mode);
            statusField.setString("Running...");
            NFCIPTest t = new NFCIPTest(m, printStream, false);
            t.runTest(numberOfRuns, minDataLength, maxDataLength);
            statusField.setString("Finished! (#resets = " + m.getNumberOfResets() + ")");
            m.close();
        } catch (NFCIPException e) {
            statusField.setString("Error: " + e.getMessage());
        }
        if (printStream != null) printStream.close();
    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Exit")) {
            try {
                destroyApp(true);
            } catch (MIDletStateChangeException e) {
            }
        } else if (label.equals("Back")) {
            if (currentMenu.equals("setMode")) {
                mode = choose.getSelectedIndex();
                ps.updateSetting(S_MODE, mode);
            }
            if (currentMenu.equals("setNumberOfRuns")) {
                numberOfRuns = Integer.parseInt(numberOfRunsField.getString());
                ps.updateSetting(S_NUMBER_OF_RUNS, numberOfRuns);
            }
            if (currentMenu.equals("setTestRange")) {
                minDataLength = Integer.parseInt(minDataLengthField.getString());
                ps.updateSetting(S_MIN_DATA_LENGTH, minDataLength);
                maxDataLength = Integer.parseInt(maxDataLengthField.getString());
                ps.updateSetting(S_MAX_DATA_LENGTH, maxDataLength);
            }
            if (currentMenu.equals("setLogging")) {
                logLevel = choose.getSelectedIndex();
                ps.updateSetting(S_LOG_LEVEL, logLevel);
            }
            if (currentMenu.equals("runningTest")) {
                if (m != null) {
                    try {
                        m.close();
                    } catch (NFCIPException e) {
                    }
                }
                if (thread != null) {
                    thread.interrupt();
                }
            }
            if (currentMenu.equals("setMode") || currentMenu.equals("setNumberOfRuns") || currentMenu.equals("setTestRange") || currentMenu.equals("runningTest") || currentMenu.equals("setLogging")) {
                mainMenu();
            }
        } else {
            List down = (List) display.getCurrent();
            switch(down.getSelectedIndex()) {
                case 0:
                    chooseMode();
                    break;
                case 1:
                    chooseNumberOfRuns();
                    break;
                case 2:
                    chooseTestRange();
                    break;
                case 3:
                    chooseLogging();
                    break;
                case 4:
                    startConnection();
                    break;
            }
        }
    }
}
