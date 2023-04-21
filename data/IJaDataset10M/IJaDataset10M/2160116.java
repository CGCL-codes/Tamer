package hinton.analyser;

import hinton.ambassador.RobotStruct;
import hinton.analyser.netstructure.ComponentRenderer;
import hinton.analyser.netstructure.NeuronRenderer;
import hinton.analyser.netstructure.SynapseRenderer;
import hinton.analyser.netstructure.VisualNet;
import hinton.analyser.plotter.FirstReturnPlotter;
import hinton.analyser.plotter.PlotCaption;
import hinton.analyser.plotter.PlotData;
import hinton.analyser.plotter.Plotter;
import hinton.executive.ProcessParameter;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import cholsey.Net;
import cholsey.NeuronType;
import cholsey.Synapse;
import cholsey.SynapseMode;

/**
 * @author rosemann
 *
 * The Analyser Main Window with controls vor Process-Speed
 * manipulation and menus to access the netstructure view and
 * plotter windows
 */
public class Analyser extends JFrame implements NetObserver, NetModifier {

    private static String STATUS_TEXT = "STEP: ";

    private VisualNet visualNet;

    private Net net;

    private ProcessMode processMode;

    private ProcessMode lastProcessMode;

    private double processDelay;

    private JToolBar jtbModifyProcess;

    private JButton btnPlay;

    private JButton btnStep;

    private JButton btnPause;

    private JLabel lblProcessDelay;

    private JSlider sldProcessDelay;

    private JLabel stepStatus;

    private Hashtable processDelayLabels;

    private JDesktopPane desktop;

    private RobotStruct robotStruct;

    private JMenuBar menuBar;

    private JMenu mnuView;

    private JMenu mnuWindow;

    private JMenuItem mniVisualNet;

    private JMenuItem mniAddPlot;

    private JMenuItem mniAddFirstReturn;

    private PlotData plotDataContainer;

    private ArrayList visiblePlotter;

    private ProcessParameter processParameter;

    private MouseInterceptingEventQueue mieq;

    private int plotCount;

    private int firstReturnMapCount;

    private int processedSteps;

    private long startedWaiting;

    private ArrayList netObserver;

    private static Analyser instance;

    private Analyser() {
        super("Analyser");
        this.menuBar = new JMenuBar();
        this.mnuView = new JMenu("View");
        this.mniVisualNet = new JMenuItem("Net Structure");
        this.mniVisualNet.setEnabled(false);
        this.mniVisualNet.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pause();
                try {
                    showVisualNet();
                } finally {
                    resume();
                }
            }
        });
        this.mniAddPlot = new JMenuItem("Plot Panel");
        this.mniAddPlot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createNewPlotter();
            }
        });
        this.mniAddFirstReturn = new JMenuItem("First Return Map");
        this.mniAddFirstReturn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createNewFirstReturnMap();
            }
        });
        this.mnuView.add(this.mniVisualNet);
        this.mnuView.add(this.mniAddPlot);
        this.mnuView.add(this.mniAddFirstReturn);
        this.menuBar.add(this.mnuView);
        this.mnuWindow = new JMenu("Window");
        this.mnuWindow.addMenuListener(new MenuListener() {

            public void menuCanceled(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuSelected(MenuEvent e) {
                computeWindowMenu();
            }
        });
        this.menuBar.add(this.mnuWindow);
        this.setJMenuBar(this.menuBar);
        this.desktop = new JDesktopPane();
        this.mieq = new MouseInterceptingEventQueue();
        this.jtbModifyProcess = new JToolBar("Processflow Control");
        this.btnPlay = new JButton();
        this.btnPlay.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Analyser.class.getResource("PlayCtrl.gif"))));
        this.btnPlay.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) this.btnPlay.getIcon()).getImage())));
        this.btnPlay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setProcessMode(ProcessMode.CONTINUOUS);
            }
        });
        this.btnStep = new JButton();
        this.btnStep.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Analyser.class.getResource("StepCtrl.gif"))));
        this.btnStep.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) this.btnStep.getIcon()).getImage())));
        this.btnStep.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setProcessMode(ProcessMode.ONESTEP);
            }
        });
        this.btnPause = new JButton();
        this.btnPause.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Analyser.class.getResource("PauseCtrl.gif"))));
        this.btnPause.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) this.btnPause.getIcon()).getImage())));
        this.btnPause.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setProcessMode(ProcessMode.PAUSED);
            }
        });
        this.lblProcessDelay = new JLabel("Process Speed: ");
        this.processDelayLabels = new Hashtable();
        this.processDelayLabels.put(new Integer(0), new JLabel("1/s"));
        this.processDelayLabels.put(new Integer(50), new JLabel("50/s"));
        this.processDelayLabels.put(new Integer(100), new JLabel("max"));
        this.sldProcessDelay = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        this.sldProcessDelay.setLabelTable(this.processDelayLabels);
        this.sldProcessDelay.setExtent(1);
        this.sldProcessDelay.setPaintLabels(true);
        this.sldProcessDelay.setPaintTicks(true);
        this.sldProcessDelay.setPaintTrack(true);
        this.sldProcessDelay.setSnapToTicks(true);
        this.sldProcessDelay.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int delay = sldProcessDelay.getValue();
                if (delay == 0) {
                    processDelay = 1.0d;
                } else if (delay == 100) {
                    processDelay = 0.0d;
                } else {
                    processDelay = 1.0d - delay / 100.0d;
                }
            }
        });
        this.stepStatus = new JLabel(Analyser.STATUS_TEXT);
        this.jtbModifyProcess.add(this.btnPlay);
        this.jtbModifyProcess.add(this.btnStep);
        this.jtbModifyProcess.add(this.btnPause);
        this.jtbModifyProcess.addSeparator();
        this.jtbModifyProcess.add(this.lblProcessDelay);
        this.jtbModifyProcess.add(this.sldProcessDelay);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.jtbModifyProcess, BorderLayout.PAGE_START);
        this.getContentPane().add(this.desktop, BorderLayout.CENTER);
        this.getContentPane().add(this.stepStatus, BorderLayout.PAGE_END);
        this.setProcessMode(ProcessMode.CONTINUOUS);
        this.processDelay = 0.0d;
        this.desktop.setSize(320, 240);
        this.desktop.setPreferredSize(this.desktop.getSize());
        this.pack();
        this.startedWaiting = -1;
        this.netObserver = new ArrayList();
    }

    /**
   * Returns the singleton instance of the analyser.
   * 
   * Due to the implementation of the Singleton Design Pattern
   * Analyser can be used as a "Well-Known" class.
   * 
   * @return Analyser singleton instance
   */
    public static Analyser getInstance() {
        if (Analyser.instance == null) {
            Analyser.instance = new Analyser();
        }
        return Analyser.instance;
    }

    /**
   * Sets the current RobotStruct to robotStruct
   * @param robotStruct RobotStruct to use as current RobotStruct
   */
    public void setRobotStruct(RobotStruct robotStruct) {
        this.robotStruct = robotStruct;
    }

    /**
   * Returns the current RobotStruct
   * 
   * @return the current RobotStruct
   */
    public RobotStruct getRobotStruct() {
        return this.robotStruct;
    }

    /**
   * Returns the name of the neuron with the given ID
   * For Input/Output neurons a mapping name in the current
   * Robotstruct is searched. If no name is mapped for the neuron
   * or for Hidden neurons a string indicating the type of the 
   * neuron is created.
   *  
   * @param neuronID to get the Name for
   * @return neuron Name or null if theres no neuron for the given ID
   */
    public String getNeuronName(int neuronID) {
        NeuronType nt = this.net.getNeuron(neuronID).getNeuronType();
        RobotStruct rs = Analyser.getInstance().getRobotStruct();
        String neuronName;
        if (nt == null) {
            return null;
        }
        if (nt.equals(NeuronType.INPUT) && rs != null) {
            neuronName = rs.getOutputObject(neuronID);
        } else if (nt.equals(NeuronType.OUTPUT) && rs != null) {
            neuronName = rs.getInputObject(neuronID - net.getInputNeurons().size());
        } else {
            neuronName = nt.toString() + " ID " + neuronID;
        }
        return neuronName;
    }

    /**
   * Sets the current ProcessParameter to processParameter
   * 
   * @param processParameter
   */
    public void setProcessParameter(ProcessParameter processParameter) {
        this.processParameter = processParameter;
    }

    /**
   * Returns the current ProcessParamter
   * @return current ProcessParameter
   */
    public ProcessParameter getProcessParameter() {
        return this.processParameter;
    }

    /**
   * Pauses the analyser and also the Processor
   */
    public void pause() {
        if (this.lastProcessMode == null) {
            this.lastProcessMode = this.processMode;
            this.processMode = ProcessMode.PAUSED;
        }
    }

    /**
   * Reset the ProcessMode of the analyser to the mode prior
   * the call of pause() 
   */
    public void resume() {
        if (this.lastProcessMode != null) {
            this.processMode = this.lastProcessMode;
            this.lastProcessMode = null;
        }
    }

    public void registerNetObserver(NetObserver no) {
        if (!this.netObserver.contains(no)) {
            this.netObserver.add(no);
        }
    }

    public void unregisterNetObserver(NetObserver no) {
        this.netObserver.remove(no);
    }

    private void computeWindowMenu() {
        this.mnuWindow.removeAll();
        if (this.visualNet != null && this.visualNet.isVisible()) {
            this.mnuWindow.add(this.computeWindowMenuItem(this.visualNet));
        }
        for (int i = 0; i < this.visiblePlotter.size(); i++) {
            this.mnuWindow.add(this.computeWindowMenuItem((JInternalFrame) this.visiblePlotter.get(i)));
        }
    }

    private JMenuItem computeWindowMenuItem(JInternalFrame frame) {
        JMenuItem ret = new JMenuItem(frame.getTitle());
        final JInternalFrame eventFrame = frame;
        ret.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                eventFrame.toFront();
                try {
                    eventFrame.setSelected(true);
                } catch (Exception exception) {
                }
            }
        });
        return ret;
    }

    /**
   * Brings up the netstructure frame
   */
    private void showVisualNet() {
        if (visualNet == null) {
            visualNet = new VisualNet(net);
            visualNet.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
            desktop.add(visualNet);
            this.netObserver.add(visualNet);
            visualNet.setSize(200, 200);
        }
        visualNet.show();
    }

    /**
   * Sets the current ProcessMode of the analyser to processMode
   * @param processMode the new processMode
   */
    private void setProcessMode(ProcessMode processMode) {
        if (processMode == ProcessMode.CONTINUOUS) {
            this.btnPlay.setEnabled(false);
            this.btnStep.setEnabled(false);
            this.btnPause.setEnabled(true);
        }
        if (processMode == ProcessMode.PAUSED) {
            this.btnPlay.setEnabled(true);
            this.btnStep.setEnabled(true);
            this.btnPause.setEnabled(false);
        }
        this.processMode = processMode;
    }

    /**
   * Brings up a new plotter window
   */
    private void createNewPlotter() {
        Plotter plotter = new Plotter("Plot " + this.plotCount, net);
        plotCount = (plotCount == Integer.MAX_VALUE ? 0 : plotCount + 1);
        this.desktop.add(plotter);
        plotter.setVisible(true);
        plotter.setSize(400, 200);
        this.visiblePlotter.add(plotter);
        this.netObserver.add(plotter);
    }

    /**
   * Brings up a new first return map window
   */
    private void createNewFirstReturnMap() {
        FirstReturnPlotter firstReturnMap = new FirstReturnPlotter("First Return Map " + this.firstReturnMapCount, net);
        firstReturnMapCount = (firstReturnMapCount == Integer.MAX_VALUE ? 0 : firstReturnMapCount + 1);
        this.desktop.add(firstReturnMap);
        firstReturnMap.setVisible(true);
        firstReturnMap.setSize(400, 200);
        this.visiblePlotter.add(firstReturnMap);
        this.netObserver.add(firstReturnMap);
    }

    private void notifyObserverAboutNetUpdateAction(Net net) {
        for (int i = 0; i < this.netObserver.size(); i++) {
            ((NetObserver) this.netObserver.get(i)).netUpdate(net);
        }
    }

    private void notifyObserverAboutSetNetAction(Net net) {
        for (int i = 0; i < this.netObserver.size(); i++) {
            ((NetObserver) this.netObserver.get(i)).setNet(net);
        }
    }

    public void setNet(Net net) {
        if (net != null) {
            this.pause();
            this.processedSteps = 0;
            if (this.processParameter != null) {
                this.stepStatus.setText(Analyser.STATUS_TEXT + this.processedSteps + "/" + this.processParameter.cycles());
            } else {
                this.stepStatus.setText(Analyser.STATUS_TEXT + this.processedSteps + "/" + 0);
            }
            if (this.visualNet != null) {
                try {
                    this.visualNet.setVisible(false);
                } catch (Exception e) {
                }
            }
            this.mniVisualNet.setEnabled(net != null);
            this.net = net;
            if (this.plotDataContainer == null) {
                this.plotDataContainer = PlotData.getInstance();
                this.netObserver.add(this.plotDataContainer);
            }
            this.notifyObserverAboutSetNetAction(net);
            PlotCaption.newNet();
            ModifiedNeuronOutputList.getInstance().setNet(net);
            try {
                Thread.sleep(20);
            } catch (Exception e) {
            }
            ;
            if (this.visiblePlotter != null) {
                this.visiblePlotter.clear();
            } else {
                this.visiblePlotter = new ArrayList();
            }
            if (this.visualNet != null) {
                this.visualNet.show();
            }
            this.resume();
        }
    }

    public void netUpdate(Net net) {
        if (this.processMode == ProcessMode.CONTINUOUS) {
            this.notifyObserverAboutNetUpdateAction(net);
        } else if (this.processMode == ProcessMode.ONESTEP) {
            this.notifyObserverAboutNetUpdateAction(net);
            this.setProcessMode(ProcessMode.PAUSED);
        }
        this.processedSteps++;
        this.stepStatus.setText(Analyser.STATUS_TEXT + this.processedSteps + "/" + this.processParameter.cycles());
        this.repaint();
    }

    /**
   * Inform the analyser that the Plotter plotter has benn closed
   * @param plotter the Plotter that has been closed
   */
    public void plotterClosed(Plotter plotter) {
        this.netObserver.remove(plotter);
        this.visiblePlotter.remove(plotter);
    }

    /**
   * Inform the analyser that the FirstReturnPlotter map has benn closed
   * @param FirstReturnPotter the Plotter that has been closed
   */
    public void firstReturnMapClosed(FirstReturnPlotter map) {
        this.netObserver.remove(map);
        this.visiblePlotter.remove(map);
    }

    public void modifyNet() {
        ModifiedNeuronOutputList.getInstance().modifyNet();
    }

    /**
   * Notify the analyser of a processing step
   * @param net the net that has been processed
   * @return true if the Analyser is in a ProcessMode to deal a net update
   *         false if not
   */
    public boolean step(Net net) {
        long delay;
        if (this.processMode == ProcessMode.PAUSED) {
            return false;
        } else if (this.processMode == ProcessMode.ONESTEP) {
            this.netUpdate(net);
        } else if (this.processMode == ProcessMode.CONTINUOUS) {
            if (this.processDelay == 0.0d) {
                this.netUpdate(net);
                this.startedWaiting = -1;
            } else {
                if (this.startedWaiting == -1) {
                    this.startedWaiting = System.currentTimeMillis();
                }
                delay = (long) (this.processDelay * 900);
                if (System.currentTimeMillis() - this.startedWaiting < delay) {
                    return false;
                } else {
                    this.netUpdate(net);
                    this.startedWaiting = -1;
                }
            }
        }
        return true;
    }

    public void reset() {
        this.processedSteps = 0;
        this.stepStatus.setText(Analyser.STATUS_TEXT + this.processedSteps + "/" + this.processParameter.cycles());
        PlotData.getInstance().reset();
    }

    /**
   * This method intercepts MouseEvents for the net structure frame
   * to show context sensitve menus
   * @see   Hinton.analyser.MouseInterceptingEventQueue
   * @param me the intercepted MouseEvent
   */
    public void intercept(MouseEvent me) {
        MouseEvent newme = SwingUtilities.convertMouseEvent(me.getComponent(), me, SwingUtilities.getDeepestComponentAt(me.getComponent(), me.getX(), me.getY()));
        ComponentRenderer cr = this.visualNet.getComponentRendererAt(newme.getPoint());
        if (cr instanceof NeuronRenderer && SwingUtilities.isRightMouseButton(me)) {
            showNeuronPopupMenu(newme, ((NeuronRenderer) cr).getNeuron().id());
        } else if (cr instanceof SynapseRenderer && SwingUtilities.isRightMouseButton(me)) {
            showSynapsePopupMenu(newme, ((SynapseRenderer) cr).getSynapse());
        } else {
            this.mieq.postIntercepted(newme);
        }
    }

    /**
   * Show a popup menu if a MouseEvent over a NeuronRenderer is intercepted
   * @param me        the intercepted MouseEvent
   * @param neuronID  id of the neuron represented by the NeuronRenderer
   */
    private void showNeuronPopupMenu(MouseEvent me, int neuronID) {
        JPopupMenu jpm = new JPopupMenu();
        JMenuItem props = new JMenuItem("Properties");
        JMenuItem infl = new JMenuItem("Show Influence");
        JMenu plot = createPlotMenu(this.net.getNeuron(neuronID));
        final MouseEvent intercepted = me;
        final int id = neuronID;
        jpm.add(plot);
        jpm.add(infl);
        jpm.add(props);
        props.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                visualNet.mouseClicked(intercepted);
            }
        });
        infl.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                visualNet.setInfluenceMode(id);
            }
        });
        jpm.addMouseListener(new MyPopupMouseAdapter(jpm));
        jpm.show(me.getComponent(), me.getX() - 5, me.getY() - 5);
    }

    private void showSynapsePopupMenu(MouseEvent me, Synapse synapse) {
        JPopupMenu jpm = new JPopupMenu();
        JMenuItem props = new JMenuItem("Properties");
        JMenuItem plot;
        final MouseEvent intercepted = me;
        if (synapse.mode().mode() == SynapseMode.SYNAPSE_MODE_DYNAMIC) {
            plot = createPlotMenu(synapse);
            jpm.add(plot);
        }
        jpm.add(props);
        props.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                visualNet.mouseClicked(intercepted);
            }
        });
        jpm.addMouseListener(new MyPopupMouseAdapter(jpm));
        jpm.show(me.getComponent(), me.getX() - 5, me.getY() - 5);
    }

    /**
   * Create a Menu with the choice to plot the Output of 
   * the neuron identified by neuronID in a new plotframe
   * or in a existing one
   * 
   * @param plotObj the Object which values are to plot
   * @return choices menu
   */
    private JMenu createPlotMenu(Object plotObj) {
        JMenu plot = new JMenu("Plot");
        JMenuItem newPlotter = new JMenuItem("New Plotter");
        JMenuItem current;
        Plotter plotter;
        final Object plotObject = plotObj;
        newPlotter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createNewPlotter();
                ((Plotter) visiblePlotter.get(visiblePlotter.size() - 1)).addObject(plotObject);
            }
        });
        plot.add(newPlotter);
        for (int i = 0; i < this.visiblePlotter.size(); i++) {
            plotter = (Plotter) this.visiblePlotter.get(i);
            if (!plotter.isClosed()) {
                plot.add(existingPlotterMenuItem(plotObj, plotter));
            }
        }
        return plot;
    }

    /**
   * Utility method for creatPlotMenu() returns a MenuItem for the existing
   * Plotter existingPlotter which action listener adds the Object to the
   * Plotter existingPlotter
   * 
   * @param plotObj reference to the Object
   * @param existingPlotter an existing Plotter
   * @return menuitem to add the neuron to the the Plotter existingPlotter
   */
    private JMenuItem existingPlotterMenuItem(Object plotObj, Plotter existingPlotter) {
        final Plotter plotter = existingPlotter;
        final Object plotObject = plotObj;
        JMenuItem ret = new JMenuItem(plotter.getName());
        ret.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                plotter.addObject(plotObject);
            }
        });
        return ret;
    }

    /**
   * 
   * @author rosemann
   *
   * Utility class needed since the popup menus for a NeuronRenderer are
   * shown an a MouseClicked event instead of the MousePressed event
   */
    private class MyPopupMouseAdapter extends MouseAdapter {

        JPopupMenu owner;

        private MyPopupMouseAdapter() {
        }

        public MyPopupMouseAdapter(JPopupMenu owner) {
            this.owner = owner;
        }

        public void mouseExited(MouseEvent e) {
            this.owner.setVisible(false);
        }
    }
}
