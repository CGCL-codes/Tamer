package org.jedits.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import org.jdom.JDOMException;
import org.jedits.comm.CommException;
import org.jedits.control.TrackControl;
import org.jedits.model.Coordinate;
import org.jedits.model.ManualController;
import org.jedits.model.ModelException;
import org.jedits.model.Railroad;
import org.jedits.model.Train;
import org.jedits.ui.simulator.SimTrackPanel;
import org.jedits.util.Config;

/**
* Main frame
**/
public class MainFrame extends JFrame {

    private static final String CONTROLLERS_VISIBLE_KEY = "manualcontrollers.visible";

    Railroad railroad;

    private Coordinate curCoordinate = new Coordinate();

    private JPanel panTrack = new JPanel();

    private JPanel panTrains = new JPanel();

    private JPanel panControllers = new JPanel();

    private JTextPane toolbar = new JTextPane();

    private BorderLayout borderLayout1 = new BorderLayout();

    private final JMenuBar mbMain = new JMenuBar();

    private final JMenu menuFile = new JMenu();

    private final JMenuItem miSave = new JMenuItem();

    private final JMenuItem miOpen = new JMenuItem();

    private final JMenu menuRailroad = new JMenu();

    private final JMenuItem miEmergencyStop = new JMenuItem();

    private final JMenu menuView = new JMenu();

    private final JCheckBoxMenuItem miManControllers = new JCheckBoxMenuItem();

    private final JMenuItem miExit = new JMenuItem();

    private final JMenu menuMode = new JMenu();

    private final JCheckBoxMenuItem miSimulator = new JCheckBoxMenuItem();

    private final TrackPanel panTrackView = new TrackPanel();

    private final SimTrackPanel panSimTrackView = new SimTrackPanel();

    private final LogPanel panLog = new LogPanel();

    public void setRailroad(Railroad rr, TrackControl control) {
        if (this.railroad != null) {
            try {
                this.railroad.close();
            } catch (CommException ex) {
                ex.printStackTrace();
            }
        }
        this.railroad = rr;
        try {
            rr.validate();
        } catch (ModelException ex) {
            ex.printStackTrace();
        }
        try {
            rr.initialize();
        } catch (CommException ex) {
            ex.printStackTrace();
        }
        panTrackView.setRailroad(rr);
        panSimTrackView.setRailroad(rr);
        panControllers.removeAll();
        int y = 0;
        for (Iterator i = rr.getControllers().iterator(); i.hasNext(); y++) {
            ManualController mc = (ManualController) i.next();
            panControllers.add(new ManualControllerPanel(mc, rr), new GridBagConstraints(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        }
        panControllers.validate();
        panTrains.removeAll();
        int x = 0;
        for (Iterator i = rr.getTrains().iterator(); i.hasNext(); x++) {
            final Train train = (Train) i.next();
            final TrainPanel tp = new TrainPanel(train);
            panTrains.add(tp, new GridBagConstraints(x, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            tp.addTrainPanelListener(control);
        }
        panTrains.validate();
        panTrackView.addTrackPanelListener(control);
        if (panTrains.getComponentCount() > 0) {
            ((TrainPanel) panTrains.getComponent(0)).grabFocus();
        }
    }

    public static void run(String args[]) throws CommException {
        final MainFrame f = new MainFrame();
        f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
        f.show();
        Railroad rr;
        try {
            rr = new Railroad(new File(System.getProperty("user.home") + "/railroad.xml"));
        } catch (JDOMException ex) {
            ex.printStackTrace();
            System.out.println("Creating default railroad");
            rr = new Railroad();
        } catch (CommException ex) {
            ex.printStackTrace();
            rr = new Railroad();
        } catch (ModelException ex) {
            ex.printStackTrace();
            rr = new Railroad();
        }
        final TrackControl control = new TrackControl();
        f.setRailroad(rr, control);
        f.init();
    }

    private void init() {
        final Thread initThread = new Thread(new Runnable() {

            public void run() {
                final Railroad rr = railroad;
                try {
                    rr.disableControllers();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        initThread.start();
    }

    /**
	 * Create a new instance
	 */
    public MainFrame() {
        System.setOut(panLog.getStream());
        System.setErr(panLog.getStream());
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean v;
        v = Config.getInstance().getProperty(CONTROLLERS_VISIBLE_KEY, true);
        panControllers.setVisible(v);
        miManControllers.setState(v);
        miSimulator.setState(false);
    }

    /**
	 * JBuilder generated initialization method
	 */
    private void jbInit() throws Exception {
        panTrack.setMinimumSize(new Dimension(40, 40));
        getContentPane().setLayout(borderLayout1);
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }

            public void windowStateChanged(WindowEvent e) {
                e.getWindow().validate();
            }
        });
        panControllers.setLayout(new GridBagLayout());
        panTrains.setLayout(new GridBagLayout());
        panTrack.setLayout(new GridBagLayout());
        toolbar.setToolTipText("");
        toolbar.setText("Status");
        toolbar.setEditable(false);
        toolbar.setFocusable(false);
        menuFile.setText("File");
        miSave.setText("Save");
        miSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miSave_actionPerformed(e);
            }
        });
        miOpen.setText("Open");
        miOpen.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miOpen_actionPerformed(e);
            }
        });
        this.setJMenuBar(mbMain);
        this.setTitle("JEdits");
        panTrackView.setBorder(BorderFactory.createLoweredBevelBorder());
        panTrackView.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                panTrackView_mouseDragged(e);
            }
        });
        panSimTrackView.setBorder(BorderFactory.createLoweredBevelBorder());
        panSimTrackView.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                panSimTrackView_mouseDragged(e);
            }
        });
        panSimTrackView.setVisible(false);
        panTrains.setBorder(BorderFactory.createLoweredBevelBorder());
        panTrains.setMaximumSize(new Dimension(2147483647, 300));
        panTrains.setMinimumSize(new Dimension(50, 200));
        panControllers.setMinimumSize(new Dimension(80, 100));
        menuView.setText("View");
        miManControllers.setSelected(true);
        miManControllers.setText("Manual Controllers");
        miManControllers.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miManControllers_actionPerformed(e);
            }
        });
        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miExit_actionPerformed(e);
            }
        });
        menuMode.setText("Mode");
        miSimulator.setText("Simlator");
        miSimulator.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miSimulator_actionPerformed(e);
            }
        });
        menuRailroad.setText("Railroad");
        miEmergencyStop.setText("All Stop!");
        miEmergencyStop.setAction(new EmergencyStopAction());
        miEmergencyStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        this.getContentPane().add(panTrack, BorderLayout.CENTER);
        this.getContentPane().add(toolbar, BorderLayout.SOUTH);
        final JPanel trPanel = new JPanel(new GridBagLayout());
        trPanel.add(panSimTrackView, new GridBagConstraints(0, 0, 1, 1, 0.3, 0.7, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        trPanel.add(panTrackView, new GridBagConstraints(1, 0, 1, 1, 0.3, 0.7, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panTrack.add(trPanel, new GridBagConstraints(0, 0, 2, 1, 0.3, 0.7, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panTrack.add(panTrains, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panTrack.add(panLog, new GridBagConstraints(1, 1, 1, 1, 0.5, 0.3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panTrack.add(panControllers, new GridBagConstraints(2, 0, 1, 2, 0.1, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        mbMain.add(menuFile);
        mbMain.add(menuView);
        mbMain.add(menuMode);
        mbMain.add(menuRailroad);
        menuFile.add(miOpen);
        menuFile.add(miSave);
        menuFile.addSeparator();
        menuFile.add(miExit);
        menuView.add(miManControllers);
        menuRailroad.add(miEmergencyStop);
        menuMode.add(miSimulator);
    }

    void this_windowClosing(WindowEvent e) {
        System.out.println("Closing...");
        System.exit(0);
    }

    void miSave_actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (railroad != null) {
            fc.setSelectedFile(railroad.getFile());
        }
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                railroad.store(fc.getSelectedFile());
            } catch (IOException ex) {
                log(ex);
            }
        }
    }

    private void log(Throwable ex) {
        toolbar.setText(ex.toString());
    }

    void miOpen_actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (railroad != null) {
            fc.setSelectedFile(railroad.getFile());
        }
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Railroad rr = new Railroad(fc.getSelectedFile());
                setRailroad(rr, new TrackControl());
                init();
            } catch (CommException ex) {
                log(ex);
            } catch (ModelException ex) {
                log(ex);
            } catch (JDOMException ex) {
                log(ex);
            }
        }
    }

    /**
	 * Handle a mouse moved event
	 */
    final void panTrackView_mouseDragged(MouseEvent e) {
        Coordinate c = panTrackView.point2coordinate(e.getPoint());
        if (!c.equals(curCoordinate)) {
            curCoordinate = c;
            toolbar.setText("" + c.x + "," + c.y);
        }
    }

    /**
	 * Handle a mouse moved event
	 */
    final void panSimTrackView_mouseDragged(MouseEvent e) {
    }

    void miManControllers_actionPerformed(ActionEvent e) {
        panControllers.setVisible(!panControllers.isVisible());
        Config.getInstance().setProperty(CONTROLLERS_VISIBLE_KEY, panControllers.isVisible());
    }

    void miExit_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    void miSimulator_actionPerformed(ActionEvent e) {
        final boolean on = !railroad.isSimulatorMode();
        railroad.setSimulatorMode(on);
        panSimTrackView.setVisible(on);
    }

    public class EmergencyStopAction extends AbstractAction {

        public EmergencyStopAction() {
            super("EmergencyStop");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ESC"));
        }

        public void actionPerformed(ActionEvent e) {
            for (Iterator i = railroad.getTrains().iterator(); i.hasNext(); ) {
                final Train t = (Train) i.next();
                try {
                    t.setSpeed(0);
                } catch (CommException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
