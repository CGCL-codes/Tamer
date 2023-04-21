package org.gaugebook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import flightsim.simconnect.NotificationPriority;
import flightsim.simconnect.SimConnect;
import flightsim.simconnect.SimConnectConstants;
import flightsim.simconnect.SimConnectDataType;
import flightsim.simconnect.SimConnectPeriod;
import flightsim.simconnect.SimObjectType;
import flightsim.simconnect.recv.DispatcherTask;
import flightsim.simconnect.recv.EventFrameHandler;
import flightsim.simconnect.recv.EventHandler;
import flightsim.simconnect.recv.ExceptionHandler;
import flightsim.simconnect.recv.OpenHandler;
import flightsim.simconnect.recv.QuitHandler;
import flightsim.simconnect.recv.RecvEvent;
import flightsim.simconnect.recv.RecvEventFrame;
import flightsim.simconnect.recv.RecvException;
import flightsim.simconnect.recv.RecvOpen;
import flightsim.simconnect.recv.RecvQuit;
import flightsim.simconnect.recv.RecvSimObjectData;
import flightsim.simconnect.recv.SimObjectDataHandler;

/**
 * GaugeBook - Home cockpit instrument suite
 *
 * Copyright (C) 2009 Michael Nagler
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Display gauges and control your FSX on your notebook or any other machine, that is capable of running the Java Runtime Environment.
 * FSX needs to have Service Pack 2 or the Acceleration pack installed.
 * 
 * GaugeBook makes use of the following open source software:
 * <ol>
 * <li><a href="http://lc0277.nerim.net/jsimconnect/">jsimconnect</a> - a Java Simconnect Framework by lc0277 (LGPL)
 * </ol>
 * 
 * The gauges of the first versions of GaugeBook were built upon
 * <a href="http://jgaugebean.sourceforge.net/">jgaugebean</a> a Gauge Java Bean by George Kharitonashvili (GPL)
 * 
 * 
 * <h3>License</h3>
 * This is free, open source software, license: LGPL.
 * 
 * @author Michael Nagler
 * @version 0.7.6 beta
 * @see http://www.gaugebook.org
 */
public class GaugeBook extends JFrame implements ComponentListener, MouseListener, MouseWheelListener, KeyListener, WindowStateListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7462115738154471622L;

    protected static String appName = "GaugeBook";

    protected static String appVersion = "0.7.6 beta (rev 227)";

    public static final boolean debug = true;

    public static final boolean experimental = false;

    private static final int EVENT_ID_DATA = 1;

    private static final int EVENT_ID_FRAME = 2;

    private static final int EVENT_ID_SIMSTART = 3;

    private static final int EVENT_ID_SIMSTOP = 4;

    private static final int EVENT_ID_EXCEPTION = 5;

    private static final int EVENT_ID_OPEN = 6;

    private static final int SIM_STATE_RUNNING = 1;

    private static final int SIM_STATE_PAUSED = 2;

    private static final int SIM_STATE_STOPPED = 3;

    private int simState = SIM_STATE_STOPPED;

    private int event_id = 100;

    private int dispatched_event_id = event_id;

    /** The vector gauges contains all gauges, that are currently displayed. */
    protected Vector<AbstractGauge> gauges = new Vector<AbstractGauge>();

    /** The vector fsxVars stores the all FSXVariables being used by the current XML gauge definition. */
    protected Vector<FSXVariable> fsxVars = new Vector<FSXVariable>();

    protected Vector<JPanel> gaugePanels = new Vector<JPanel>();

    /**
	 * The activePanel stores the current panel number.
	 */
    protected int activePanel = 0;

    /**
	 * The main Panel. To the BorderLayout.CENTER all custom gauges are added. 
	 * At .NORTH is the optional SimpleRadioPanel, at .SOUTH the FKeyBar.
	 */
    protected JPanel pnlGaugeBook;

    protected SimpleRadioPanel radioPanel;

    protected FKeyBar fkeybar;

    protected SimConnect simConnect;

    protected DispatcherTask dispatcherTask;

    protected boolean isDispatcherTaskRunning = false;

    protected SettingsDialog settingsDialog;

    protected InputDialog inputDialog;

    protected GraphicsDevice graphicsDevice;

    protected DisplayMode originalDM;

    protected boolean isFullScreen = false;

    int originalWidth = 960;

    int originalHeight = 560;

    protected Properties config = new Properties();

    protected File userdir = new File(System.getProperty("user.dir"));

    protected String fileSep = System.getProperty("file.separator");

    protected String configFileName = userdir.getAbsolutePath() + fileSep + "cfg" + fileSep + appName + ".properties";

    protected String xmlPath = userdir.getAbsolutePath() + fileSep + "xml" + fileSep;

    protected String pdfPath = userdir.getAbsolutePath() + fileSep + "charts" + fileSep;

    protected String ttfPath = userdir.getAbsolutePath() + fileSep + "fonts" + fileSep;

    public String welcomePDFile = userdir.getAbsolutePath() + fileSep + "cfg" + fileSep + "chartviewer_welcome.pdf";

    protected int windowState;

    protected boolean firstrun = false;

    protected String license_text = "Copyright (C) 2009 Michael Nagler, nailware.org\n\n" + "This program comes with ABSOLUTELY NO WARRANTY,\n" + "for details see http://www.gnu.org/licenses/gpl.html.\n" + "This is free software, and you are welcome to redistribute it\n" + "under certain conditions; for details see also have a look at\n" + "http://www.gnu.org/licenses/gpl.html\n\n" + "You have a copy of the GPL3 license in your gaugebook directory.";

    protected boolean configLoaded;

    public boolean licenseaccepted = false;

    public GaugeBook(GraphicsDevice device, GraphicsConfiguration conf) {
        super(appName + " " + appVersion, conf);
        System.out.println(appName + " " + appVersion + "\n\n" + license_text);
        this.graphicsDevice = device;
        if (debug) System.out.println("System available accelerated memory: " + device.getAvailableAcceleratedMemory() + " byte.");
        originalDM = device.getDisplayMode();
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
        setIconImage(Toolkit.getDefaultToolkit().getImage("gfx/icon.gif"));
        loadSettings();
        if (config.getProperty("gaugebook.licenseaccepted") == null || config.getProperty("gaugebook.licenseaccepted").equals("false")) {
            Object[] options = { "Yes", "No" };
            int n = JOptionPane.showOptionDialog(new JFrame(), license_text + "\n\nDo you accept the GPL3 license for GaugeBook (required)?", "GaugeBook license agreement", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n == 0) {
                config.setProperty("gaugebook.licenseaccepted", "true");
                licenseaccepted = true;
                saveSettings();
            } else {
                config.setProperty("gaugebook.licenseaccepted", "false");
                licenseaccepted = false;
                saveSettings();
                shutdown();
            }
        }
        settingsDialog = new SettingsDialog(this, "GaugeBook settings");
        if (!configLoaded) {
            config = settingsDialog.getDefaultProperties();
            settingsDialog.setProperties(config);
        }
        if (firstrun) {
            settingsDialog.setProperties(config);
            settingsDialog.setVisible(true);
            config = settingsDialog.getProperties();
            saveSettings();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pnlGaugeBook = new JPanel(new BorderLayout());
        pnlGaugeBook.setBackground(Color.DARK_GRAY);
        radioPanel = new SimpleRadioPanel(this, "RadioPanel alpha");
        if (config.containsKey("gaugebook.radiopanel") && config.getProperty("gaugebook.radiopanel").equals("true")) {
            radioPanel.addKeyListener(this);
            pnlGaugeBook.add(radioPanel, BorderLayout.NORTH);
        }
        fkeybar = new FKeyBar(this);
        if (config.containsKey("gaugebook.fkeypanel") && config.getProperty("gaugebook.fkeypanel").equals("true")) {
            fkeybar.addKeyListener(this);
            pnlGaugeBook.add(fkeybar, BorderLayout.SOUTH);
        }
        if (!config.containsKey("gaugebook.xmlfile") || config.getProperty("gaugebook.xmlfile").trim().isEmpty()) {
            System.out.println("invalid gaugebook.xmlfile: " + config.getProperty("gaugebook.xmlfile"));
            config.setProperty("gaugebook.xmlfile", "C172.xml");
        }
        if (!isFullScreen && config.containsKey("gaugebook.alwaysontop")) {
            super.setAlwaysOnTop(config.getProperty("gaugebook.alwaysontop").equals("true") && !settingsDialog.getProperties().getProperty("gaugebook.fullscreen").equals("true"));
        }
        if (!isFullScreen && config.containsKey("gaugebook.width") && config.containsKey("gaugebook.height")) {
            setSize((int) Float.parseFloat(config.getProperty("gaugebook.width")), (int) Float.parseFloat(config.getProperty("gaugebook.height")));
        }
        if (!isFullScreen && config.containsKey("gaugebook.xpos") && config.containsKey("gaugebook.ypos")) {
            this.setLocation((int) Float.parseFloat(config.getProperty("gaugebook.xpos")), (int) Float.parseFloat(config.getProperty("gaugebook.ypos")));
        } else if (!isFullScreen) {
            dim = getToolkit().getScreenSize();
            abounds = getBounds();
            setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
        }
        try {
            XmlGuiBuilder.loadGui(this, xmlPath + config.getProperty("gaugebook.xmlfile"), gaugePanels, gauges);
            for (int i = 0; i < gauges.size(); i++) {
                gauges.get(i).setAntialias(config.getProperty("gaugebook.antialias").equals("true"));
            }
        } catch (Exception e) {
            Object[] options = { "Retry", "Settings", "Quit" };
            int n = JOptionPane.showOptionDialog(new JFrame(), "The XML File '" + config.getProperty("gaugebook.xmlfile") + "' has errors:\n" + e.getLocalizedMessage() + "\n" + "Do you want to retry or quit?", "XML parser error", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            if (n == 0) {
                GaugeBookStarter.restart(this);
            } else if (n == 1) {
                showSettingsDialog(true);
                saveSettings();
            } else {
                shutdown();
            }
        }
        pnlGaugeBook.add(gaugePanels.get(0), BorderLayout.CENTER);
        getContentPane().add(pnlGaugeBook);
        this.setFocusable(true);
        addKeyListener(this);
        addComponentListener(this);
    }

    /** Constructor used by GaugeBookFSUIPC */
    public GaugeBook(String string, GraphicsConfiguration conf) {
        super(appName + " " + appVersion, conf);
    }

    long lastFrame = 0;

    long lasttime = 0;

    int frames = 0;

    FSXVariable atcType, atcModel, atcID;

    protected synchronized void init(final GaugeBook owner) {
        try {
            final SimConnect sc = new SimConnect("GaugeBook", owner.config.getProperty("gaugebook.connect.ip"), Integer.parseInt(owner.config.getProperty("gaugebook.connect.port")));
            owner.simConnect = sc;
            if (debug) System.out.println("Data definition has " + fsxVars.size() + " FSX variables.");
            for (int i = 0; i < fsxVars.size(); i++) {
                FSXVariable var = (FSXVariable) fsxVars.get(i);
                sc.addToDataDefinition(EVENT_ID_DATA, var.getName(), var.getUnit(), var.getType());
            }
            sc.subscribeToSystemEvent(EVENT_ID_FRAME, "Frame");
            sc.subscribeToSystemEvent(EVENT_ID_SIMSTART, "SimStart");
            sc.subscribeToSystemEvent(EVENT_ID_SIMSTOP, "SimStop");
            final DispatcherTask dt = new DispatcherTask(sc);
            owner.dispatcherTask = dt;
            lastFrame = System.currentTimeMillis();
            dt.addOpenHandler(new OpenHandler() {

                public void handleOpen(SimConnect sender, RecvOpen e) {
                    if (debug) System.out.println("\nConnected to " + e.getApplicationName());
                }
            });
            dt.addEventFrameHandler(new EventFrameHandler() {

                public void handleEventFrame(SimConnect sender, RecvEventFrame event) {
                    if (dispatched_event_id < event_id) {
                        try {
                            dispatched_event_id = event_id;
                            if (dispatcherTask != null) sender.callDispatch(dispatcherTask);
                        } catch (IOException e1) {
                            if (debug) e1.printStackTrace();
                        }
                    }
                    int ms = 100;
                    if (System.currentTimeMillis() > lastFrame + ms) {
                        lastFrame = System.currentTimeMillis();
                        try {
                            sender.requestDataOnSimObject(event_id++, EVENT_ID_DATA, 0, SimConnectPeriod.ONCE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dt.addExceptionHandler(new ExceptionHandler() {

                public void handleException(SimConnect sender, RecvException e) {
                    System.out.println("Exception (" + e.getException() + ") packet " + e.getSendID());
                }
            });
            dt.addQuitHandler(new QuitHandler() {

                public void handleQuit(SimConnect sender, RecvQuit e) {
                    if (debug) System.out.println("Flight Simulator quit");
                    Object[] options = { "Yes", "No" };
                    int n = JOptionPane.showOptionDialog(new JFrame(), "Restart GaugeBook?", "Flight Simulation quit", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                    if (n == 0) {
                        GaugeBookStarter.restart(owner);
                    } else {
                        shutdown();
                    }
                }
            });
            dt.addEventHandler(new EventHandler() {

                public void handleEvent(SimConnect sender, RecvEvent e) {
                    if (e.getEventID() == EVENT_ID_DATA) {
                        try {
                            sender.requestDataOnSimObjectType(EVENT_ID_DATA, 1, 100, SimObjectType.USER);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    } else if (e.getEventID() == EVENT_ID_SIMSTART) {
                        System.out.println("Simulation started");
                        simState = SIM_STATE_RUNNING;
                    } else if (e.getEventID() == EVENT_ID_SIMSTOP) {
                        System.out.println("Simulation stopped");
                        simState = SIM_STATE_STOPPED;
                    }
                }
            });
            dt.addSimObjectDataHandler(new SimObjectDataHandler() {

                public void handleSimObject(SimConnect sender, RecvSimObjectData e) {
                    for (int i = 0; i < fsxVars.size(); i++) {
                        FSXVariable var;
                        if (fsxVars.get(i).getType() == SimConnectDataType.STRING32) {
                            var = fsxVars.get(i);
                            var.setValue(e.getDataString32());
                        } else if (fsxVars.get(i).getType() == SimConnectDataType.STRING256) {
                            var = fsxVars.get(i);
                            var.setValue(e.getDataString256());
                        } else if (fsxVars.get(i).getType() == SimConnectDataType.FLOAT64) {
                            var = fsxVars.get(i);
                            var.setValue(e.getDataFloat64());
                        }
                    }
                    for (int i = 0; i < gauges.size(); i++) {
                        if (gauges.get(i).isVisible()) gauges.get(i).update();
                    }
                    if (radioPanel.isVisible()) radioPanel.update();
                    if (fkeybar.isVisible()) fkeybar.update();
                    if (e.getEntryNumber() == e.getOutOf()) {
                    }
                }
            });
            new Thread(dt).start();
            isDispatcherTaskRunning = true;
            atcType = registerFSXVariable("ATC TYPE");
            atcModel = registerFSXVariable("ATC MODEL");
            atcID = registerFSXVariable("ATC ID");
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    if (System.currentTimeMillis() - lasttime > 1000) {
                        lasttime = System.currentTimeMillis();
                        String title = appName + " " + appVersion;
                        if (atcType.getValue() != null) {
                            title = title + " - " + atcType.getStringValue();
                            title += " " + atcModel.getStringValue();
                            title += " - " + atcID.getStringValue();
                            title += " - " + config.getProperty("gaugebook.xmlfile");
                            title += " - www.gaugebook.org";
                        }
                        setTitle(title);
                        frames = 0;
                    }
                }
            }, 1000, 1000);
        } catch (IOException e1) {
            if (debug) {
                System.out.println("Connection failed:");
                e1.printStackTrace();
            }
            Object[] options = { "Retry", "Settings", "Quit" };
            int n = JOptionPane.showOptionDialog(new JFrame(), "Error: " + e1.getMessage(), "Connection failed", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            if (n == 0) {
                GaugeBookStarter.restart(this);
            } else if (n == 1) {
                showSettingsDialog(true);
                saveSettings();
            } else {
                shutdown();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (isDispatcherTaskRunning) {
                isDispatcherTaskRunning = false;
                dispatcherTask.tryStop();
                dispatcherTask = null;
            }
            if (debug) {
                System.out.println("An exception occured:");
                e2.printStackTrace();
            }
            Object[] options = { "Retry", "Quit" };
            int n = JOptionPane.showOptionDialog(new JFrame(), "Error: " + e2.getMessage(), "Exception occured", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            if (n == 0) {
                GaugeBookStarter.restart(this);
            } else {
                shutdown();
            }
            shutdown();
        }
    }

    public synchronized void dispatchEvent(final String event, final int param) {
        System.out.println("dispatchEvent: " + event);
        if (this.dispatcherTask == null || !isDispatcherTaskRunning) return;
        try {
            simConnect.mapClientEventToSimEvent(++event_id, event);
            System.out.println("Event ID: " + event_id + ": " + event + " param: " + param);
            simConnect.transmitClientEvent(0, event_id, param, NotificationPriority.HIGHEST.ordinal(), SimConnectConstants.EVENT_FLAG_GROUPID_IS_PRIORITY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispatchEvent(String event) {
        if (this.dispatcherTask == null || !isDispatcherTaskRunning) return;
        this.dispatchEvent(event, 0);
    }

    public FSXVariable registerFSXVariable(String name) {
        int id = -1;
        for (int i = 0; i < fsxVars.size(); i++) {
            if (name == fsxVars.get(i).getName()) {
                id = i;
                break;
            }
        }
        if (id == -1) {
            id = fsxVars.size();
            fsxVars.add(FSXVariable.getVar(name));
        }
        return fsxVars.get(id);
    }

    public Vector<FSXVariable> getFSXVariables() {
        return this.fsxVars;
    }

    protected void showSettingsDialog(boolean runMode) {
        settingsDialog = new SettingsDialog(this, "GaugeBook settings");
        settingsDialog.setRunMode(runMode);
        settingsDialog.setProperties(config);
        settingsDialog.setVisible(true);
        settingsDialog.toFront();
        config = settingsDialog.getProperties();
        saveSettings();
        if (settingsDialog.getResult() == SettingsDialog.RESULT_OK) {
            GaugeBookStarter.restart(this);
            return;
        }
        for (int i = 0; i < gauges.size(); i++) {
            gauges.get(i).setAntialias(config.getProperty("gaugebook.antialias").equals("true"));
        }
        super.setAlwaysOnTop(config.getProperty("gaugebook.alwaysontop").equals("true") && !settingsDialog.getProperties().getProperty("gaugebook.fullscreen").equals("true"));
    }

    protected void loadSettings() {
        configLoaded = false;
        try {
            FileInputStream fis = new FileInputStream(configFileName);
            config = new Properties();
            config.load(fis);
            fis.close();
            configLoaded = true;
            licenseaccepted = config.getProperty("gaugebook.licenseaccepted") != null && config.getProperty("gaugebook.licenseaccepted").equals("true");
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found.");
            firstrun = true;
        } catch (IOException e) {
        }
    }

    protected void saveSettings() {
        try {
            FileOutputStream fos = new FileOutputStream(configFileName);
            config.store(fos, appName + " " + appVersion + " properties." + "\n#This file is automatically generated. Do not edit (unless you know what you're doin).");
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void switchFullScreenMode(boolean fullscreen) {
        setVisible(false);
        validate();
        if (fullscreen) {
            try {
                if (debug) System.out.println("entering full screen exclusive mode");
                setUndecorated(true);
                originalWidth = getWidth();
                originalHeight = getHeight();
                setBounds(0, 0, originalDM.getWidth(), originalDM.getHeight());
                graphicsDevice.setFullScreenWindow(this);
                isFullScreen = true;
            } catch (Exception ex) {
                if (debug) ex.printStackTrace();
                graphicsDevice.setDisplayMode(originalDM);
                graphicsDevice.setFullScreenWindow(null);
                if (!isDisplayable()) setUndecorated(false);
                setSize(originalWidth, originalHeight);
            }
        } else {
            if (debug) System.out.println("full screen not supported -> normal window mode");
            graphicsDevice.setFullScreenWindow(null);
            isFullScreen = false;
            if (!isDisplayable()) setUndecorated(false);
        }
        setVisible(true);
        validate();
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public String getFontPath() {
        return ttfPath;
    }

    static class GaugeBookStarter {

        public static void start() {
            restart(null);
        }

        public static void restart(GaugeBook gb) {
            if (gb != null) {
                if (gb.isDispatcherTaskRunning) {
                    gb.dispatcherTask.tryStop();
                    gb.dispatcherTask = null;
                }
                gb.setVisible(false);
                gb.dispose();
                gb = null;
                FSXVariable.reset();
            }
            UIManager.put("nimbusBase", Color.BLACK);
            UIManager.put("nimbusBlueGrey", Color.DARK_GRAY);
            UIManager.put("control", Color.DARK_GRAY);
            UIManager.put("text", Color.LIGHT_GRAY);
            UIManager.put("nimbusLightBackground", Color.BLACK);
            UIManager.put("nimbusFocus", Color.GREEN);
            UIManager.put("nimbusButton.foreground", Color.BLACK);
            UIManager.put("nimbusSelectedText", Color.WHITE);
            UIManager.put("focusedSelectionBackground", new Color(0, 42, 0));
            try {
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
            }
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] devices = env.getScreenDevices();
            GraphicsConfiguration conf = devices[0].getDefaultConfiguration();
            gb = new GaugeBook(devices[0], conf);
            if (gb.config.getProperty("gaugebook.fullscreen").equals("true")) {
            }
            gb.init(gb);
            gb.setVisible(true);
        }
    }

    public void shutdown() {
        if (isDispatcherTaskRunning) {
            dispatcherTask.tryStop();
            dispatcherTask = null;
        }
        setVisible(false);
        dispose();
        System.exit(0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        AbstractGauge g = null;
        try {
            g = (AbstractGauge) gaugePanels.get(activePanel).getComponent(0);
            if (gaugePanels.get(activePanel).getComponentCount() == 1) {
                g = (AbstractGauge) gaugePanels.get(activePanel).getComponent(0);
                if (g.getFkeylabels() != null) {
                    g.keyPressed(e);
                    if (e.isConsumed()) return;
                }
            }
        } catch (ArrayIndexOutOfBoundsException x) {
            x.printStackTrace();
        }
        switch(e.getKeyCode()) {
            case KeyEvent.VK_F1:
                switch(fkeybar.getPage()) {
                    case 0:
                        if (e.isShiftDown()) {
                            dispatchEvent("COM_STBY_RADIO_SWAP");
                        } else if (e.isControlDown()) {
                            inputDialog = new InputDialog(this, "COM1", "COM_RADIO_SET", InputDialog.Unit.BCD16COMFREQ);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("COM1_TRANSMIT_SELECT");
                        }
                        break;
                    case 1:
                        dispatchEvent("AP_MASTER");
                        break;
                    case 2:
                        dispatchEvent("TOGGLE_AVIONICS_MASTER");
                        break;
                }
                break;
            case KeyEvent.VK_F2:
                switch(fkeybar.getPage()) {
                    case 0:
                        if (e.isShiftDown()) {
                            dispatchEvent("COM2_RADIO_SWAP");
                        } else if (e.isControlDown()) {
                            inputDialog = new InputDialog(this, "COM2", "COM2_RADIO_SET", InputDialog.Unit.BCD16COMFREQ);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("COM2_TRANSMIT_SELECT");
                        }
                        break;
                    case 1:
                        if (e.isShiftDown() || e.isControlDown()) {
                            inputDialog = new InputDialog(this, "HDG BUG", "HEADING_BUG_SET", InputDialog.Unit.INT);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("AP_PANEL_HEADING_HOLD");
                        }
                        break;
                    case 2:
                        dispatchEvent("TOGGLE_CABIN_LIGHTS");
                        break;
                }
                break;
            case KeyEvent.VK_F3:
                switch(fkeybar.getPage()) {
                    case 0:
                        dispatchEvent("COM_RECEIVE_ALL_TOGGLE");
                        break;
                    case 1:
                        if (e.isShiftDown() || e.isControlDown()) {
                            inputDialog = new InputDialog(this, "VOR1 OBS", "VOR1_SET", InputDialog.Unit.INT);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("AP_NAV1_HOLD");
                        }
                        break;
                    case 2:
                        dispatchEvent("PANEL_LIGHTS_TOGGLE");
                        break;
                }
                break;
            case KeyEvent.VK_F4:
                switch(fkeybar.getPage()) {
                    case 0:
                        if (e.isShiftDown()) {
                            dispatchEvent("NAV1_RADIO_SWAP");
                        } else if (e.isControlDown()) {
                            inputDialog = new InputDialog(this, "NAV1", "NAV1_RADIO_SET", InputDialog.Unit.BCD16NAVFREQ);
                            inputDialog.setVisible(true);
                        } else if (e.isAltDown()) {
                            System.exit(0);
                        } else {
                            dispatchEvent("RADIO_VOR1_IDENT_TOGGLE");
                        }
                        break;
                    case 1:
                        dispatchEvent("AP_APR_HOLD");
                        break;
                    case 2:
                        dispatchEvent("TOGGLE_BEACON_LIGHTS");
                        break;
                }
                break;
            case KeyEvent.VK_F5:
                switch(fkeybar.getPage()) {
                    case 0:
                        if (e.isShiftDown()) {
                            dispatchEvent("NAV2_RADIO_SWAP");
                        } else if (e.isControlDown()) {
                            inputDialog = new InputDialog(this, "NAV2", "NAV2_RADIO_SET", InputDialog.Unit.BCD16NAVFREQ);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("RADIO_VOR2_IDENT_TOGGLE");
                        }
                        break;
                    case 1:
                        dispatchEvent("AP_BC_HOLD");
                        break;
                    case 2:
                        dispatchEvent("LANDING_LIGHTS_TOGGLE");
                        break;
                }
                break;
            case KeyEvent.VK_F6:
                switch(fkeybar.getPage()) {
                    case 0:
                        dispatchEvent("MARKER_SOUND_TOGGLE");
                        break;
                    case 1:
                        if (e.isShiftDown() || e.isControlDown()) {
                            inputDialog = new InputDialog(this, "AP ALT ft", "AP_ALT_VAR_SET_ENGLISH", InputDialog.Unit.INT);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("AP_ALT_HOLD");
                        }
                        break;
                    case 2:
                        dispatchEvent("TOGGLE_TAXI_LIGHTS");
                        break;
                }
                break;
            case KeyEvent.VK_F7:
                switch(fkeybar.getPage()) {
                    case 0:
                        if (e.isShiftDown() || e.isControlDown()) {
                            dispatchEvent("TOGGLE_DME");
                        } else {
                            dispatchEvent("RADIO_SELECTED_DME_IDENT_TOGGLE");
                        }
                        break;
                    case 1:
                        if (e.isShiftDown() || e.isControlDown()) {
                            inputDialog = new InputDialog(this, "VOR2 OBS", "VOR2_SET", InputDialog.Unit.INT);
                            inputDialog.setVisible(true);
                        } else {
                            inputDialog = new InputDialog(this, "VOR1 OBS", "VOR1_SET", InputDialog.Unit.INT);
                            inputDialog.setVisible(true);
                        }
                        break;
                    case 2:
                        dispatchEvent("TOGGLE_NAV_LIGHTS");
                        break;
                }
                break;
            case KeyEvent.VK_F8:
                switch(fkeybar.getPage()) {
                    case 0:
                        if (e.isControlDown()) {
                            inputDialog = new InputDialog(this, "ADF", "ADF_COMPLETE_SET", InputDialog.Unit.BCD16HZ);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("RADIO_ADF_IDENT_TOGGLE");
                        }
                        break;
                    case 1:
                        if (e.isShiftDown() || e.isControlDown()) {
                            inputDialog = new InputDialog(this, "SPD kts", "AP_SPD_VAR_SET", InputDialog.Unit.INT);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("AP_PANEL_SPEED_HOLD");
                        }
                        break;
                    case 2:
                        dispatchEvent("STROBES_TOGGLE");
                        break;
                }
                break;
            case KeyEvent.VK_F9:
                switch(fkeybar.getPage()) {
                    case 0:
                        inputDialog = new InputDialog(this, "Transponder", "XPNDR_SET", InputDialog.Unit.BCDXPDR);
                        inputDialog.setVisible(true);
                        break;
                    case 1:
                        if (e.isShiftDown() || e.isControlDown()) {
                            inputDialog = new InputDialog(this, "VS fpm", "AP_VS_VAR_SET_ENGLISH", InputDialog.Unit.INT);
                            inputDialog.setVisible(true);
                        } else {
                            dispatchEvent("AP_PANEL_SPEED_HOLD");
                        }
                        break;
                    case 2:
                        dispatchEvent("PITOT_HEAT_TOGGLE");
                        break;
                }
                break;
            case KeyEvent.VK_F10:
                switch(fkeybar.getPage()) {
                    case 0:
                        if (e.isShiftDown() || e.isControlDown()) {
                            dispatchEvent("BAROMETRIC");
                        } else {
                            inputDialog = new InputDialog(this, "QNH MB", "KOHLSMAN_SET", InputDialog.Unit.KOHLSMANINHG);
                            inputDialog.setVisible(true);
                        }
                        break;
                    case 1:
                        dispatchEvent("TOGGLE_GPS_DRIVES_NAV1");
                        break;
                    case 2:
                        dispatchEvent("SMOKE_TOGGLE");
                        break;
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_F11:
                fkeybar.prevPage();
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_F12:
                fkeybar.nextPage();
                break;
            case KeyEvent.VK_ESCAPE:
                if (e.isShiftDown()) {
                    GaugeBookFMC fmc = new GaugeBookFMC(this);
                    fmc.setLocationRelativeTo(this);
                    fmc.setVisible(true);
                    fmc.toFront();
                } else {
                    showSettingsDialog(true);
                }
                break;
            case KeyEvent.VK_UP:
                pnlGaugeBook.remove(gaugePanels.get(activePanel));
                activePanel = activePanel > 0 ? activePanel - 1 : gaugePanels.size() - 1;
                pnlGaugeBook.add(gaugePanels.get(activePanel), BorderLayout.CENTER);
                if (gaugePanels.get(activePanel).getComponentCount() == 1) {
                    g = (AbstractGauge) gaugePanels.get(activePanel).getComponent(0);
                    System.out.println(g.toString());
                    if (g.getFkeylabels() != null) {
                        fkeybar.setCustomPage(g.getFkeylabels(), g.getFkeyevents(), g.getFkeyleds());
                    } else {
                        fkeybar.setPage(fkeybar.getPage());
                    }
                }
                pnlGaugeBook.revalidate();
                pnlGaugeBook.repaint();
                break;
            case KeyEvent.VK_DOWN:
                pnlGaugeBook.remove(gaugePanels.get(activePanel));
                activePanel = activePanel < gaugePanels.size() - 1 ? activePanel + 1 : 0;
                pnlGaugeBook.add(gaugePanels.get(activePanel), BorderLayout.CENTER);
                if (gaugePanels.get(activePanel).getComponentCount() == 1) {
                    g = (AbstractGauge) gaugePanels.get(activePanel).getComponent(0);
                    if (g.getFkeylabels() != null) {
                        fkeybar.setCustomPage(g.getFkeylabels(), g.getFkeyevents(), g.getFkeyleds());
                    } else {
                        fkeybar.setPage(fkeybar.getPage());
                    }
                }
                pnlGaugeBook.revalidate();
                pnlGaugeBook.repaint();
                break;
            case KeyEvent.VK_1:
                dispatchEvent("TOGGLE_BEACON_LIGHTS");
                break;
            case KeyEvent.VK_2:
                dispatchEvent("LANDING_LIGHTS_TOGGLE");
                break;
            case KeyEvent.VK_3:
                dispatchEvent("TOGGLE_TAXI_LIGHTS");
                break;
            case KeyEvent.VK_4:
                dispatchEvent("TOGGLE_NAV_LIGHTS");
                break;
            case KeyEvent.VK_5:
                dispatchEvent("STROBES_TOGGLE");
                break;
            case KeyEvent.VK_6:
                dispatchEvent("TOGGLE_CABIN_LIGHTS");
                break;
            case KeyEvent.VK_A:
                if (e.isShiftDown()) {
                    dispatchEvent("TOGGLE_AVIONICS_MASTER");
                } else if (e.isControlDown()) {
                    dispatchEvent("AP_PANEL_ALTITUDE_HOLD");
                } else {
                    inputDialog = new InputDialog(this, "AP ALT ft", "AP_ALT_VAR_SET_ENGLISH", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                }
                break;
            case KeyEvent.VK_B:
                dispatchEvent("TOGGLE_MASTER_BATTERY");
                break;
            case KeyEvent.VK_C:
                dispatchEvent("TOGGLE_CABIN_LIGHTS");
                break;
            case KeyEvent.VK_D:
                break;
            case KeyEvent.VK_F:
                dispatchEvent("FUEL_PUMP");
                break;
            case KeyEvent.VK_G:
                if (e.isControlDown()) {
                    dispatchEvent("TOGGLE_GPS_DRIVES_NAV1");
                } else if (e.isShiftDown()) {
                    dispatchEvent("TOGGLE_MASTER_ALTERNATOR");
                } else {
                    dispatchEvent("GEAR_TOGGLE");
                }
                break;
            case KeyEvent.VK_H:
                if (e.isControlDown()) {
                    dispatchEvent("AP_PANEL_HEADING_HOLD");
                } else {
                    inputDialog = new InputDialog(this, "HDG BUG", "HEADING_BUG_SET", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                }
                break;
            case KeyEvent.VK_I:
                dispatchEvent("PANEL_LIGHTS_TOGGLE");
                break;
            case KeyEvent.VK_L:
                dispatchEvent("LANDING_LIGHTS_TOGGLE");
                break;
            case KeyEvent.VK_M:
                dispatchEvent("MAGNETO_SET");
                break;
            case KeyEvent.VK_N:
                if (e.isControlDown()) {
                    dispatchEvent("AP_NAV1_HOLD");
                } else if (e.isShiftDown()) {
                    dispatchEvent("AP_NAV_SELECT_SET");
                } else {
                    dispatchEvent("TOGGLE_NAV_LIGHTS");
                }
                break;
            case KeyEvent.VK_O:
                if (e.isControlDown()) {
                    inputDialog = new InputDialog(this, "VOR2 OBS", "VOR2_SET", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                } else {
                    inputDialog = new InputDialog(this, "VOR1 OBS", "VOR1_SET", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                }
                break;
            case KeyEvent.VK_P:
                dispatchEvent("PITOT_HEAT_TOGGLE");
                break;
            case KeyEvent.VK_Q:
                inputDialog = new InputDialog(this, "QNH MB", "KOHLSMAN_SET", InputDialog.Unit.KOHLSMANINHG);
                inputDialog.setVisible(true);
                break;
            case KeyEvent.VK_R:
                dispatchEvent("SMOKE_TOGGLE");
                break;
            case KeyEvent.VK_S:
                dispatchEvent("STROBES_TOGGLE");
                break;
            case KeyEvent.VK_T:
                dispatchEvent("TOGGLE_TAXI_LIGHTS");
                break;
            case KeyEvent.VK_X:
                inputDialog = new InputDialog(this, "XPDR", "XPNDR_SET", InputDialog.Unit.BCDXPDR);
                inputDialog.setVisible(true);
                break;
            case KeyEvent.VK_Y:
                dispatchEvent("YAW_DAMPER_TOGGLE");
                break;
            case KeyEvent.VK_Z:
                dispatchEvent("AP_MASTER");
                break;
            case KeyEvent.VK_ENTER:
                if (e.isAltDown()) {
                    if (isFullScreen) {
                        config.setProperty("gaugebook.fullscreen", "false");
                    } else {
                        config.setProperty("gaugebook.fullscreen", "true");
                    }
                    saveSettings();
                    GaugeBookStarter.restart(this);
                }
            default:
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
        if (isFullScreen || windowState == MAXIMIZED_BOTH || windowState == ICONIFIED) return;
        config.setProperty("gaugebook.xpos", "" + getLocation().getX());
        config.setProperty("gaugebook.ypos", "" + getLocation().getY());
    }

    @Override
    public void componentResized(ComponentEvent arg0) {
        if (isFullScreen || windowState == MAXIMIZED_BOTH || windowState == ICONIFIED) return;
        config.setProperty("gaugebook.width", "" + getSize().getWidth());
        config.setProperty("gaugebook.height", "" + getSize().getHeight());
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        if (arg0.getSource().getClass().toString().equals("class javax.swing.JLabel")) {
            JLabel l = (JLabel) arg0.getSource();
            if (l.getName().equals("COM1FREQ")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("COM1_TRANSMIT_SELECT");
                } else {
                    inputDialog = new InputDialog(this, "COM1", "COM_RADIO_SET", InputDialog.Unit.BCD16COMFREQ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("COM1SWAP")) {
                dispatchEvent("COM_STBY_RADIO_SWAP");
            } else if (l.getName().equals("COM1STBY")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("COM1_TRANSMIT_SELECT");
                } else if (arg0.getButton() == MouseEvent.BUTTON2) {
                    dispatchEvent("COM_STBY_RADIO_SWAP");
                } else {
                    inputDialog = new InputDialog(this, "COM1 STBY", "COM_STBY_RADIO_SET", InputDialog.Unit.BCD16COMFREQ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("COM2FREQ")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("COM2_TRANSMIT_SELECT");
                } else {
                    inputDialog = new InputDialog(this, "COM2", "COM2_RADIO_SET", InputDialog.Unit.BCD16COMFREQ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("COM2SWAP")) {
                dispatchEvent("COM2_RADIO_SWAP");
            } else if (l.getName().equals("COM2STBY")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("COM2_TRANSMIT_SELECT");
                } else if (arg0.getButton() == MouseEvent.BUTTON2) {
                    dispatchEvent("COM2_RADIO_SWAP");
                } else {
                    inputDialog = new InputDialog(this, "COM2 STBY", "COM2_STBY_RADIO_SET", InputDialog.Unit.BCD16COMFREQ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("COMBOTH")) {
                dispatchEvent("COM_RECEIVE_ALL_TOGGLE");
            } else if (l.getName().equals("NAV1FREQ")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("RADIO_VOR1_IDENT_TOGGLE");
                } else {
                    inputDialog = new InputDialog(this, "NAV1", "NAV1_RADIO_SET", InputDialog.Unit.BCD16NAVFREQ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("NAV1SWAP")) {
                dispatchEvent("NAV1_RADIO_SWAP");
            } else if (l.getName().equals("NAV1STBY")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("RADIO_VOR1_IDENT_TOGGLE");
                } else if (arg0.getButton() == MouseEvent.BUTTON2) {
                    dispatchEvent("NAV1_RADIO_SWAP");
                } else {
                    inputDialog = new InputDialog(this, "NAV1", "NAV1_STBY_SET", InputDialog.Unit.BCD16NAVFREQ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("NAV2FREQ")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("RADIO_VOR2_IDENT_TOGGLE");
                } else {
                    inputDialog = new InputDialog(this, "NAV2", "NAV2_RADIO_SET", InputDialog.Unit.BCD16NAVFREQ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("NAV2SWAP")) {
                dispatchEvent("NAV2_RADIO_SWAP");
            } else if (l.getName().equals("NAV2STBY")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("RADIO_VOR2_IDENT_TOGGLE");
                } else if (arg0.getButton() == MouseEvent.BUTTON2) {
                    dispatchEvent("NAV2_RADIO_SWAP");
                } else {
                    inputDialog = new InputDialog(this, "NAV2", "NAV2_STBY_SET", InputDialog.Unit.BCD16NAVFREQ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("ADF1FREQ")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("RADIO_ADF_IDENT_TOGGLE");
                } else {
                    inputDialog = new InputDialog(this, "ADF", "ADF_COMPLETE_SET", InputDialog.Unit.BCD16HZ);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("MKRTGL")) {
                dispatchEvent("MARKER_SOUND_TOGGLE");
            } else if (l.getName().equals("DMETGL")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("RADIO_DME1_IDENT_TOGGLE");
                } else {
                    dispatchEvent("RADIO_DME2_IDENT_TOGGLE");
                }
            } else if (l.getName().equals("XPDRCODE")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                } else {
                    inputDialog = new InputDialog(this, "Transponder", "XPNDR_SET", InputDialog.Unit.BCDXPDR);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("SETTINGS")) {
                showSettingsDialog(true);
            } else if (l.getName().equals("QNHSET")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    inputDialog = new InputDialog(this, "QNH MB", "KOHLSMAN_SET", InputDialog.Unit.KOHLSMANINHG);
                    inputDialog.setVisible(true);
                } else {
                    dispatchEvent("BAROMETRIC");
                }
            } else if (l.getName().equals("QUIT")) {
                Object[] options = { "Exit", "Cancel" };
                int res = JOptionPane.showOptionDialog(this, "Quit GaugeBook?", "Confirm quit", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                if (res == 0) {
                    shutdown();
                }
            } else if (l.getName().equals("AP")) {
                dispatchEvent("AP_MASTER");
            } else if (l.getName().equals("HDG")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("AP_PANEL_HEADING_HOLD");
                } else {
                    inputDialog = new InputDialog(this, "HDG BUG", "HEADING_BUG_SET", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("NAV")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("AP_NAV1_HOLD");
                } else {
                    dispatchEvent("AP_NAV_SELECT_SET");
                }
            } else if (l.getName().equals("APR")) {
                dispatchEvent("AP_APR_HOLD");
            } else if (l.getName().equals("REV")) {
                dispatchEvent("AP_BC_HOLD");
            } else if (l.getName().equals("ALT")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("AP_PANEL_ALTITUDE_HOLD");
                } else {
                    inputDialog = new InputDialog(this, "AP ALT ft", "AP_ALT_VAR_SET_ENGLISH", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("CRS")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    inputDialog = new InputDialog(this, "VOR1 OBS", "VOR1_SET", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                } else {
                    inputDialog = new InputDialog(this, "VOR2 OBS", "VOR2_SET", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("SPD")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("AP_PANEL_SPEED_HOLD");
                } else {
                    inputDialog = new InputDialog(this, "SPD kts", "AP_SPD_VAR_SET", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("GPS")) {
                dispatchEvent("TOGGLE_GPS_DRIVES_NAV1");
            } else if (l.getName().equals("VS")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("AP_PANEL_SPEED_HOLD");
                } else {
                    inputDialog = new InputDialog(this, "VS fpm", "AP_VS_VAR_SET_ENGLISH", InputDialog.Unit.INT);
                    inputDialog.setVisible(true);
                }
            } else if (l.getName().equals("MAG")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    dispatchEvent("MAGNETO_INCR");
                } else {
                    dispatchEvent("MAGNETO_DECR");
                }
            } else if (l.getName().equals("MASTER")) {
                dispatchEvent("TOGGLE_MASTER_BATTERY");
                dispatchEvent("TOGGLE_MASTER_ALTERNATOR");
            } else if (l.getName().equals("FUEL")) {
                dispatchEvent("FUEL_PUMP");
            } else if (l.getName().equals("PANEL")) {
                dispatchEvent("PANEL_LIGHTS_TOGGLE");
            } else if (l.getName().equals("CABIN")) {
                dispatchEvent("TOGGLE_CABIN_LIGHTS");
            } else if (l.getName().equals("BCN")) {
                dispatchEvent("TOGGLE_BEACON_LIGHTS");
            } else if (l.getName().equals("LAND")) {
                dispatchEvent("LANDING_LIGHTS_TOGGLE");
            } else if (l.getName().equals("TAXI")) {
                dispatchEvent("TOGGLE_TAXI_LIGHTS");
            } else if (l.getName().equals("NAVL")) {
                dispatchEvent("TOGGLE_NAV_LIGHTS");
            } else if (l.getName().equals("STROBE")) {
                dispatchEvent("STROBES_TOGGLE");
            } else if (l.getName().equals("PITOT")) {
                dispatchEvent("PITOT_HEAT_TOGGLE");
            } else if (l.getName().equals("AVI")) {
                dispatchEvent("TOGGLE_AVIONICS_MASTER");
            } else if (l.getName().equals("PREV")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    fkeybar.prevPage();
                } else {
                    pnlGaugeBook.remove(gaugePanels.get(activePanel));
                    activePanel = activePanel > 0 ? activePanel - 1 : gaugePanels.size() - 1;
                    pnlGaugeBook.add(gaugePanels.get(activePanel), BorderLayout.CENTER);
                    if (gaugePanels.get(activePanel).getComponentCount() == 1) {
                        AbstractGauge g = (AbstractGauge) gaugePanels.get(activePanel).getComponent(0);
                        System.out.println(g.toString());
                        if (g.getFkeylabels() != null) {
                            fkeybar.setCustomPage(g.getFkeylabels(), g.getFkeyevents(), g.getFkeyleds());
                        } else {
                            fkeybar.setPage(fkeybar.getPage());
                        }
                    }
                    pnlGaugeBook.revalidate();
                    pnlGaugeBook.repaint();
                }
            } else if (l.getName().equals("NEXT")) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    fkeybar.nextPage();
                } else {
                    pnlGaugeBook.remove(gaugePanels.get(activePanel));
                    activePanel = activePanel < gaugePanels.size() - 1 ? activePanel + 1 : 0;
                    pnlGaugeBook.add(gaugePanels.get(activePanel), BorderLayout.CENTER);
                    if (gaugePanels.get(activePanel).getComponentCount() == 1) {
                        AbstractGauge g = (AbstractGauge) gaugePanels.get(activePanel).getComponent(0);
                        if (g.getFkeylabels() != null) {
                            fkeybar.setCustomPage(g.getFkeylabels(), g.getFkeyevents(), g.getFkeyleds());
                        } else {
                            fkeybar.setPage(fkeybar.getPage());
                        }
                    }
                    pnlGaugeBook.revalidate();
                    pnlGaugeBook.repaint();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
        if (arg0.getSource().getClass().toString().equals("class javax.swing.JLabel")) {
            JLabel l = (JLabel) arg0.getSource();
            System.out.println("arg0.x = " + arg0.getX() + " width=" + l.getWidth());
            if (arg0.getWheelRotation() < 0 && l.getName().equals("COM1STBY")) {
                if (arg0.getX() < l.getWidth() * .6) {
                    dispatchEvent("COM_RADIO_WHOLE_INC");
                } else {
                    dispatchEvent("COM_RADIO_FRACT_INC");
                }
            } else if (arg0.getWheelRotation() > 0 && l.getName().equals("COM1STBY")) {
                if (arg0.getX() < l.getWidth() * .6) {
                    dispatchEvent("COM_RADIO_WHOLE_DEC");
                } else {
                    dispatchEvent("COM_RADIO_FRACT_DEC");
                }
            } else if (arg0.getWheelRotation() < 0 && l.getName().equals("COM2STBY")) {
                if (arg0.getX() < l.getWidth() * .6) {
                    dispatchEvent("COM2_RADIO_WHOLE_INC");
                } else {
                    dispatchEvent("COM2_RADIO_FRACT_INC");
                }
            } else if (arg0.getWheelRotation() > 0 && l.getName().equals("COM2STBY")) {
                if (arg0.getX() < l.getWidth() * .6) {
                    dispatchEvent("COM2_RADIO_WHOLE_DEC");
                } else {
                    dispatchEvent("COM2_RADIO_FRACT_DEC");
                }
            } else if (arg0.getWheelRotation() < 0 && l.getName().equals("NAV1STBY")) {
                if (arg0.getX() < l.getWidth() * .6) {
                    dispatchEvent("NAV1_RADIO_WHOLE_INC");
                } else {
                    dispatchEvent("NAV1_RADIO_FRACT_INC");
                }
            } else if (arg0.getWheelRotation() > 0 && l.getName().equals("NAV1STBY")) {
                if (arg0.getX() < l.getWidth() * .6) {
                    dispatchEvent("NAV1_RADIO_WHOLE_DEC");
                } else {
                    dispatchEvent("NAV1_RADIO_FRACT_DEC");
                }
            } else if (arg0.getWheelRotation() < 0 && l.getName().equals("NAV2STBY")) {
                if (arg0.getX() < l.getWidth() * .6) {
                    dispatchEvent("NAV2_RADIO_WHOLE_INC");
                } else {
                    dispatchEvent("NAV2_RADIO_FRACT_INC");
                }
            } else if (arg0.getWheelRotation() > 0 && l.getName().equals("NAV2STBY")) {
                if (arg0.getX() < l.getWidth() * .6) {
                    dispatchEvent("NAV1_RADIO_WHOLE_DEC");
                } else {
                    dispatchEvent("NAV1_RADIO_FRACT_DEC");
                }
            } else if (l.getName().equals("NAV")) {
                if (arg0.getWheelRotation() > 0) {
                    dispatchEvent("VOR1_OBI_DEC");
                } else {
                    dispatchEvent("VOR1_OBI_INC");
                }
            } else if (l.getName().equals("CRS")) {
                if (arg0.getWheelRotation() > 0) {
                    dispatchEvent("VOR2_OBI_DEC");
                } else {
                    dispatchEvent("VOR2_OBI_INC");
                }
            } else if (l.getName().equals("HDG")) {
                if (arg0.getWheelRotation() > 0) {
                    dispatchEvent("HEADING_BUG_DEC");
                } else {
                    dispatchEvent("HEADING_BUG_INC");
                }
            } else if (l.getName().equals("ALT")) {
                if (arg0.getWheelRotation() > 0) {
                    dispatchEvent("AP_ALT_VAR_DEC");
                } else {
                    dispatchEvent("AP_ALT_VAR_INC");
                }
            } else if (l.getName().equals("SPD")) {
                if (arg0.getWheelRotation() > 0) {
                    dispatchEvent("AP_SPD_VAR_DEC");
                } else {
                    dispatchEvent("AP_SPD_VAR_INC");
                }
            } else if (l.getName().equals("VS")) {
                if (arg0.getWheelRotation() > 0) {
                    dispatchEvent("AP_VS_VAR_DEC");
                } else {
                    dispatchEvent("AP_VS_VAR_INC");
                }
            } else if (l.getName().equals("QNHSET")) {
                if (arg0.getWheelRotation() > 0) {
                    dispatchEvent("KOHLSMAN_DEC");
                } else {
                    dispatchEvent("KOHLSMAN_INC");
                }
            }
        }
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        windowState = e.getNewState();
    }

    public static void main(String[] args) {
        GaugeBookStarter.start();
    }

    public int getSimState() {
        return simState;
    }
}
