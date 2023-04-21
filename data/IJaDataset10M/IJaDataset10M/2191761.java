package m6800.applet.components;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import m6800.applet.SimulatorApplet;
import m6800.panels.CPUPanel;
import m6800.panels.GeneralPanel;
import org.apache.log4j.Logger;

/**
 * controlPanel() provides a panel object which contains and the control
 * features of the program.
 */
public class AppletControlPanel extends GeneralPanel implements ActionListener, AdjustmentListener, Runnable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initial speed settings.
     */
    private static final int INITIAL_SPEED_SETTING = 70;

    /**
     * Logger for this class.
     */
    private static Logger logger = Logger.getLogger(AppletControlPanel.class);

    /**
     * Boolean to indicate compile requested.
     */
    private boolean compileRequested;

    /**
     * Boolean to indicate step requested.
     */
    private boolean stepRequested;

    /**
     * Boolean to indicate run requested.
     */
    private boolean runRequested;

    /**
     * Thread.
     */
    private Thread manager;

    /**
     * Assemble button.
     */
    private Button buttonAssemble;

    /**
     * Bin/Hex button.
     */
    private Button buttonBinHex;

    /**
     * Run button.
     */
    private Button runButton;

    /**
     * Step button.
     */
    private Button stepButton;

    /**
     * Stop button.
     */
    private Button stopButton;

    /**
     * Memory size choice.
     */
    private Choice memorySizeChooser;

    /**
     * Memory label.
     */
    private Label memoryLabel;

    /**
     * Speed title label.
     */
    private Label speedTitleLabel;

    /**
     * Scroll bar.
     */
    private Scrollbar speedScroll;

    /**
     * Speed label.
     */
    private Label speedLabel;

    /**
     * Layout manager.
     */
    private GridBagLayout gridBag;

    /**
     * CPU Panel.
     */
    private CPUPanel cpuPanel;

    /**
     * constructor method controlPanel() .
     * 
     */
    public AppletControlPanel() {
        setCompileRequested(false);
        setStepRequested(false);
        this.setBackground(getCenterColor());
        createComponents();
        initLayout();
        addActionListeners();
        AdjustmentEvent event = new AdjustmentEvent(speedScroll, ox, ox, INITIAL_SPEED_SETTING);
        adjustmentValueChanged(event);
    }

    /**
     * Lay out the components.
     */
    private void initLayout() {
        gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridBag);
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(2, 2, 5, 2);
        c.weightx = 1.0;
        gridBag.setConstraints(buttonAssemble, c);
        add(buttonAssemble);
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        gridBag.setConstraints(getStepButton(), c);
        add(getStepButton());
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.setConstraints(getRunButton(), c);
        add(getRunButton());
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(getStopButton(), c);
        add(getStopButton());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 2, 10);
        gridBag.setConstraints(speedTitleLabel, c);
        add(speedTitleLabel);
        c.insets = new Insets(0, 10, 0, 10);
        gridBag.setConstraints(speedScroll, c);
        add(speedScroll);
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(2, 2, 10, 2);
        c.weightx = 1.0;
        gridBag.setConstraints(speedLabel, c);
        add(speedLabel);
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 2, 10, 2);
        c.weightx = 1.0;
        gridBag.setConstraints(buttonBinHex, c);
        add(buttonBinHex);
        c.insets = new Insets(10, 0, 0, 0);
        gridBag.setConstraints(memoryLabel, c);
        add(memoryLabel);
        c.insets = new Insets(0, 10, 0, 10);
        gridBag.setConstraints(memorySizeChooser, c);
        add(memorySizeChooser);
        panelWidth = 130;
        panelHeight = 300;
        size = new Dimension(panelWidth + 1, panelHeight + 1);
    }

    /**
     * Create buttons etc.
     */
    private void createComponents() {
        buttonAssemble = new Button("Assemble");
        setStepButton(new Button("Step"));
        setRunButton(new Button("Run"));
        setStopButton(new Button("Stop"));
        getStepButton().setEnabled(false);
        getStopButton().setEnabled(false);
        memorySizeChooser = new Choice();
        memorySizeChooser.addItem("255");
        memoryLabel = new Label("Data Memory Size:");
        getRunButton().setEnabled(false);
        speedLabel = new Label("Speed = " + INITIAL_SPEED_SETTING);
        speedTitleLabel = new Label("Execution Speed:");
        buttonBinHex = new Button("Bin/Hex");
        speedScroll = new Scrollbar(Scrollbar.HORIZONTAL, INITIAL_SPEED_SETTING, 5, 0, 100);
    }

    /**
     * Add action listeners.
     */
    private void addActionListeners() {
        buttonAssemble.addActionListener(this);
        getStepButton().addActionListener(this);
        getStopButton().addActionListener(this);
        getRunButton().addActionListener(this);
        buttonBinHex.addActionListener(this);
        speedScroll.addAdjustmentListener(this);
    }

    /**
     * make this a thread.
     */
    public void run() {
    }

    /**
     * Start.
     */
    public void start() {
        if (manager == null) {
            manager = new Thread(this);
            manager.start();
        }
    }

    /**
     * Stop method.
     */
    @SuppressWarnings("deprecation")
    public void stop() {
        if (manager != null) {
            manager = new Thread(this);
            manager.stop();
            manager = null;
        }
    }

    /**
     * listener for scroll bar events.
     * 
     * @param e
     *            - AdjustmentEvent
     */
    public void adjustmentValueChanged(final AdjustmentEvent e) {
        int speed;
        speed = e.getValue();
        if (speed > 95) {
            speed = 96;
        }
        speedLabel.setText("Speed = " + (speed));
        SimulatorApplet.setAnimationDelay(96 - speed);
    }

    /**
     * listener for button events.
     * 
     * @param event
     *            - the ActionEvent
     */
    public void actionPerformed(final ActionEvent event) {
        String buttonLabel = event.getActionCommand();
        if (buttonLabel.equals("Step")) {
            setStepRequested(true);
            try {
                notifyAll();
            } catch (Exception ex) {
                logger.warn("Exception notify caught");
            }
        } else if (buttonLabel.equals("Run")) {
            getRunButton().setEnabled(false);
            setRunRequested(true);
            try {
                notifyAll();
            } catch (Exception ex) {
                logger.warn("Exception notify caught");
            }
        } else if (buttonLabel.equals("Stop")) {
            getStopButton().setEnabled(false);
            setRunRequested(false);
            try {
                notifyAll();
            } catch (Exception ex) {
                logger.warn("Exception notify caught");
            }
        } else if (buttonLabel.equals("Assemble")) {
            setCompileRequested(true);
            cpuPanel.resetAllValues();
            try {
                notify();
            } catch (Exception ex) {
                logger.warn("Exception notify caught");
            }
        } else if (buttonLabel.equals("Bin/Hex")) {
            if (cpuPanel.getAccumulatorA().getHexMode()) {
                cpuPanel.getAccumulatorA().setHexMode(false);
                cpuPanel.getDataBuffer().setHexMode(false);
                cpuPanel.getProgramCounter().setHexMode(false);
                cpuPanel.getIR().setHexMode(false);
                cpuPanel.getAddressBuffer().setHexMode(false);
            } else {
                cpuPanel.getAccumulatorA().setHexMode(true);
                cpuPanel.getDataBuffer().setHexMode(true);
                cpuPanel.getProgramCounter().setHexMode(true);
                cpuPanel.getIR().setHexMode(true);
                cpuPanel.getAddressBuffer().setHexMode(true);
            }
        }
        logger.debug(buttonLabel + " Pressed");
    }

    /**
     * returns the data memory size as specified by the user.
     * 
     * @return - returns the data memory size as specified by the user
     */
    public int getDataMemorySize() {
        int index = memorySizeChooser.getSelectedIndex();
        String item = memorySizeChooser.getItem(index);
        return (Integer.valueOf(item)).intValue();
    }

    /**
     * Set run requested.
     * 
     * @param runRequested
     *            - is a run requested?
     */
    public void setRunRequested(final boolean runRequested) {
        this.runRequested = runRequested;
    }

    /**
     * Get run requested.
     * 
     * @return - returns run requested
     */
    public boolean isRunRequested() {
        return runRequested;
    }

    /**
     * Standard setter.
     * 
     * @param stepRequested
     *            - is a step requested?
     */
    public void setStepRequested(final boolean stepRequested) {
        this.stepRequested = stepRequested;
    }

    /**
     * Standard setter.
     * 
     * @return - returns true if a step is requested
     */
    public boolean isStepRequested() {
        return stepRequested;
    }

    /**
     * Standard setter.
     * 
     * @param compileRequested
     *            - is a compile requested?
     */
    public void setCompileRequested(final boolean compileRequested) {
        this.compileRequested = compileRequested;
    }

    /**
     * Standard getter.
     * 
     * @return - returns the compile request status
     */
    public boolean isCompileRequested() {
        return compileRequested;
    }

    /**
     * Standard setter.
     * 
     * @param cpuPanel
     *            - the CpuPanel object.
     */
    public void setCpuPanel(final CPUPanel cpuPanel) {
        this.cpuPanel = cpuPanel;
    }

    /**
     * Standard setter.
     * 
     * @param runButton
     *            - run botton.
     */
    public void setRunButton(final Button runButton) {
        this.runButton = runButton;
    }

    /**
     * Standard getter.
     * 
     * @return - returns the run button
     */
    public Button getRunButton() {
        return runButton;
    }

    /**
     * Standard setter.
     * 
     * @param stepButton
     *            - step button
     */
    public void setStepButton(final Button stepButton) {
        this.stepButton = stepButton;
    }

    /**
     * Standard getter.
     * 
     * @return - returns the step button
     */
    public Button getStepButton() {
        return stepButton;
    }

    /**
     * Standard setter.
     * 
     * @param stopButton
     *            - stop button
     */
    public void setStopButton(final Button stopButton) {
        this.stopButton = stopButton;
    }

    /**
     * Standard getter.
     * 
     * @return - returns the stop button
     */
    public Button getStopButton() {
        return stopButton;
    }
}
