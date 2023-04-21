package org.grailrtls.sim.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import org.grailrtls.libworldmodel.client.ClientWorldModelInterface;
import org.grailrtls.libworldmodel.solver.SolverWorldModelInterface;
import org.grailrtls.sensor.SensorAggregatorInterface;
import org.grailrtls.sim.FakeSolver;
import org.grailrtls.solver.SolverAggregatorInterface;
import org.grailrtls.util.HashableByteArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationTools extends JFrame implements ActionListener, WindowListener {

    private final JMenuBar menu = new JMenuBar();

    private static final Logger log = LoggerFactory.getLogger(SimulationTools.class);

    protected final JTabbedPane tabs = new JTabbedPane();

    protected AggregatorStatisticsPanel aggregatorStatsPanel;

    protected TransmitterDetailsPanel transmitterDetailPanel;

    protected ReceiverDetailsPanel receiverDetailPanel;

    protected SensorAggregatorInterface sensorAggregatorInterface = new SensorAggregatorInterface();

    protected SolverAggregatorInterface solverAggregatorInterface = new SolverAggregatorInterface();

    protected SolverWorldModelInterface solverWorldModelInterface = new SolverWorldModelInterface();

    protected ClientWorldModelInterface clientWorldModelInterface = new ClientWorldModelInterface();

    protected FilePlaybackInterface filePlayback = new FilePlaybackInterface();

    protected static final int DEFAULT_FPS = 10;

    protected int desiredFps = DEFAULT_FPS;

    protected int minFps = (int) (this.desiredFps * 0.8f);

    protected String solverAggregatorHost = "localhost";

    protected int solverAggregatorPort = 7008;

    protected String worldServerHost = "localhost";

    protected int worldServerPort = 7011;

    public static void main(String[] args) {
        if (args.length > 0 && args.length < 4) {
            printUsageInfo();
            System.exit(1);
        }
        SimulationTools tools = new SimulationTools();
        if (args.length >= 4) {
            try {
                int aggPort = Integer.parseInt(args[1]);
                int worldPort = Integer.parseInt(args[3]);
                tools.setAggregatorHost(args[0]);
                tools.setAggregatorPort(aggPort);
                tools.setWorldServerHost(args[2]);
                tools.setWorldServerPort(worldPort);
            } catch (NumberFormatException nfe) {
                System.err.println("One or more options were invalid.");
                printUsageInfo();
                System.exit(1);
            }
            if (args.length > 4) {
                for (int i = 4; i < args.length; ++i) {
                    String arg = args[i];
                    if (args.equals("--fps")) {
                        if (args.length < ++i) {
                            log.error("No valid fps value specified.");
                            return;
                        }
                        try {
                            float fps = Float.parseFloat(args[++i]);
                            tools.setDesiredFps((int) fps);
                        } catch (NumberFormatException nfe) {
                            log.error("Unable to parse fps value {}", args[i], nfe);
                        }
                    }
                }
            }
        }
        log.info("Starting up...");
        tools.initConnections();
        tools.configureDisplay();
        tools.connectToWorldServer();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
        }
        tools.connectToAggregator();
    }

    protected static void printUsageInfo() {
        System.out.println("Parameters: <Aggregator Host> <Aggregator Port> <World Server Host> <World Server Port>");
    }

    public SimulationTools() {
        super("GRAIL RTLS Simulation Toolkit");
    }

    protected void initConnections() {
        this.solverAggregatorInterface.setHost(this.solverAggregatorHost);
        this.solverAggregatorInterface.setPort(this.solverAggregatorPort);
        this.solverAggregatorInterface.setStayConnected(true);
        this.clientWorldModelInterface.setHost(this.worldServerHost);
        this.clientWorldModelInterface.setPort(this.worldServerPort);
        this.clientWorldModelInterface.setStayConnected(true);
    }

    public void configureDisplay() {
        this.addWindowListener(this);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));
        this.setLayout(new BorderLayout());
        this.add(this.tabs, BorderLayout.CENTER);
        this.aggregatorStatsPanel = new AggregatorStatisticsPanel(this.solverAggregatorInterface, this.clientWorldModelInterface);
        this.transmitterDetailPanel = new TransmitterDetailsPanel(this.solverAggregatorInterface, this.clientWorldModelInterface);
        this.receiverDetailPanel = new ReceiverDetailsPanel(this.solverAggregatorInterface, this.clientWorldModelInterface);
        this.tabs.add(this.aggregatorStatsPanel, "Aggr. Stats");
        this.tabs.add(this.transmitterDetailPanel, "Transmitters");
        this.tabs.add(this.receiverDetailPanel, "Receivers");
        this.buildMenu();
        this.setJMenuBar(this.menu);
        this.pack();
        this.setVisible(true);
        this.updateFps();
        this.aggregatorStatsPanel.startUpdates();
        this.transmitterDetailPanel.startUpdates();
        this.receiverDetailPanel.startUpdates();
    }

    public void connectToAggregator() {
        this.solverAggregatorInterface.doConnectionSetup();
    }

    public void connectToWorldServer() {
        this.clientWorldModelInterface.doConnectionSetup();
    }

    protected JMenu windowMenu = new JMenu("Windows");

    protected JMenuItem windowNew = new JMenuItem("New Window");

    protected JMenu refreshMenu = new JMenu("Refresh Rate");

    protected JMenuItem refresh1hz = new JRadioButtonMenuItem("1 Hz");

    protected JMenuItem refresh5hz = new JRadioButtonMenuItem("5 Hz");

    protected JMenuItem refresh10hz = new JRadioButtonMenuItem("10 Hz", true);

    protected JMenuItem refresh15hz = new JRadioButtonMenuItem("15 Hz");

    protected JMenuItem refresh20hz = new JRadioButtonMenuItem("20 Hz");

    protected JMenuItem refresh30hz = new JRadioButtonMenuItem("30 Hz");

    protected ButtonGroup refreshGroup = new ButtonGroup();

    protected ConcurrentHashMap<HashableByteArray, JCheckBoxMenuItem> receiverCheckBoxes = new ConcurrentHashMap<HashableByteArray, JCheckBoxMenuItem>();

    protected static volatile int numWindows = 0;

    protected void buildMenu() {
        this.windowMenu.add(this.windowNew);
        this.windowNew.addActionListener(this);
        this.menu.add(this.windowMenu);
        this.refreshGroup.add(this.refresh1hz);
        this.refreshGroup.add(this.refresh5hz);
        this.refreshGroup.add(this.refresh10hz);
        this.refreshGroup.add(this.refresh15hz);
        this.refreshGroup.add(this.refresh20hz);
        this.refreshGroup.add(this.refresh30hz);
        this.refreshMenu.add(this.refresh1hz);
        this.refreshMenu.add(this.refresh5hz);
        this.refreshMenu.add(this.refresh10hz);
        this.refreshMenu.add(this.refresh15hz);
        this.refreshMenu.add(this.refresh20hz);
        this.refreshMenu.add(this.refresh30hz);
        this.refresh1hz.addActionListener(this);
        this.refresh5hz.addActionListener(this);
        this.refresh10hz.addActionListener(this);
        this.refresh15hz.addActionListener(this);
        this.refresh20hz.addActionListener(this);
        this.refresh30hz.addActionListener(this);
        this.menu.add(refreshMenu);
    }

    public String getAggregatorHost() {
        return solverAggregatorHost;
    }

    public void setAggregatorHost(String aggregatorHost) {
        this.solverAggregatorHost = aggregatorHost;
    }

    public int getAggregatorPort() {
        return solverAggregatorPort;
    }

    public void setAggregatorPort(int aggregatorPort) {
        this.solverAggregatorPort = aggregatorPort;
    }

    public String getWorldServerHost() {
        return worldServerHost;
    }

    public void setWorldServerHost(String worldServerHost) {
        this.worldServerHost = worldServerHost;
    }

    public int getWorldServerPort() {
        return worldServerPort;
    }

    public void setWorldServerPort(int worldServerPort) {
        this.worldServerPort = worldServerPort;
    }

    public int getDesiredFps() {
        return desiredFps;
    }

    public void setDesiredFps(int desiredFps) {
        this.desiredFps = desiredFps;
        this.updateFps();
    }

    protected void updateFps() {
        this.minFps = (int) Math.ceil(this.desiredFps * 0.8f);
        if (this.aggregatorStatsPanel != null) {
            this.aggregatorStatsPanel.setDesiredFps(this.desiredFps);
            this.aggregatorStatsPanel.setMinFps(this.minFps);
        }
        if (this.transmitterDetailPanel != null) {
            this.transmitterDetailPanel.setDesiredFps(this.desiredFps);
            this.transmitterDetailPanel.setMinFps(this.minFps);
        }
        if (this.receiverDetailPanel != null) {
            this.receiverDetailPanel.setDesiredFps(this.desiredFps);
            this.receiverDetailPanel.setMinFps(this.minFps);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.refresh1hz) {
            this.setDesiredFps(1);
        } else if (e.getSource() == this.refresh5hz) {
            this.setDesiredFps(5);
        } else if (e.getSource() == this.refresh10hz) {
            this.setDesiredFps(10);
        } else if (e.getSource() == this.refresh15hz) {
            this.setDesiredFps(15);
        } else if (e.getSource() == this.refresh20hz) {
            this.setDesiredFps(20);
        } else if (e.getSource() == this.refresh30hz) {
            this.setDesiredFps(30);
        } else if (e.getSource() == this.windowNew) {
            this.openNewWindow();
        }
    }

    protected void openNewWindow() {
        SimulationTools newTools = new SimulationTools();
        newTools.addWindowListener(this);
        newTools.solverAggregatorInterface = this.solverAggregatorInterface;
        newTools.clientWorldModelInterface = this.clientWorldModelInterface;
        newTools.configureDisplay();
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        --this.numWindows;
        if (this.numWindows <= 0) {
            log.info("Attempting to exit the application.");
            this.solverAggregatorInterface.doConnectionTearDown();
            this.clientWorldModelInterface.doConnectionTearDown();
            log.info("Closed network connections.");
            log.warn("Forcing exit.");
            System.exit(1);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
        ++SimulationTools.numWindows;
    }
}
