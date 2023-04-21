package org.opt4j.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.opt4j.config.Icons;
import org.opt4j.core.optimizer.Control;
import org.opt4j.core.optimizer.ControlListener;
import org.opt4j.core.optimizer.OptimizationStartListener;
import org.opt4j.core.optimizer.OptimizationStopListener;
import org.opt4j.core.optimizer.Optimizer;
import org.opt4j.core.optimizer.Control.State;
import com.google.inject.Inject;

/**
 * The {@code ControlPanel} is a JPanel that allows to access the
 * {@link Control}.
 * 
 * @author lukasiewycz
 * 
 */
public class ControlPanel implements OptimizationStartListener, OptimizationStopListener, ControlListener {

    protected final Control control;

    protected final Optimizer optimizer;

    protected JButton start;

    protected JButton pause;

    protected JButton stop;

    protected JButton terminate;

    /**
	 * Constructs a {@code ControlPanel}.
	 * 
	 * @param control
	 *            the control
	 * @param optimizer
	 *            the optimizer
	 */
    @Inject
    public ControlPanel(Control control, Optimizer optimizer) {
        this.control = control;
        this.optimizer = optimizer;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        construct();
    }

    /**
	 * Registers the listeners.
	 */
    @Inject
    public void init() {
        optimizer.addStartListener(this);
        optimizer.addStopListener(this);
    }

    /**
	 * Constructs the panel.
	 */
    protected final void construct() {
        start = new JButton("", Icons.getIcon(Icons.CONTROL_START));
        pause = new JButton("", Icons.getIcon(Icons.CONTROL_PAUSE));
        stop = new JButton("", Icons.getIcon(Icons.CONTROL_STOP));
        terminate = new JButton("", Icons.getIcon(Icons.CONTROL_TERM));
        start.setToolTipText("Start");
        pause.setToolTipText("Pause");
        stop.setToolTipText("Stop");
        terminate.setToolTipText("Terminate");
        start.setFocusable(false);
        pause.setFocusable(false);
        stop.setFocusable(false);
        terminate.setFocusable(false);
        start.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                control.doStart();
                update();
            }
        });
        pause.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                control.doPause();
                update();
            }
        });
        stop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                control.doStop();
                update();
            }
        });
        terminate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                control.doTerminate();
                update();
            }
        });
    }

    /**
	 * Updates the view.
	 */
    public void update() {
        final State state;
        if (!optimizer.isRunning()) {
            state = State.TERMINATED;
        } else {
            state = control.getState();
        }
        final boolean bStart = (state == State.PAUSED);
        final boolean bPause = (state == State.RUNNING);
        final boolean bStop = (state == State.RUNNING || state == State.PAUSED);
        final boolean bTerminate = (state != State.TERMINATED);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                start.setEnabled(bStart);
                pause.setEnabled(bPause);
                stop.setEnabled(bStop);
                terminate.setEnabled(bTerminate);
            }
        });
    }

    public void optimizationStarted(Optimizer optimizer) {
        update();
        control.addListener(this);
    }

    public void optimizationStopped(Optimizer optimizer) {
        update();
    }

    /**
	 * Returns the start button.
	 * 
	 * @return the start button
	 */
    public JButton getStart() {
        return start;
    }

    /**
	 * Returns the pause button.
	 * 
	 * @return the pause button
	 */
    public JButton getPause() {
        return pause;
    }

    /**
	 * Returns the stop button.
	 * 
	 * @return the stop button
	 */
    public JButton getStop() {
        return stop;
    }

    /**
	 * Returns the terminate button.
	 * 
	 * @return the terminate button
	 */
    public JButton getTerminate() {
        return terminate;
    }

    public void stateChanged(State state) {
        update();
    }
}
