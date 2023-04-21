package org.opt4j.gui;

import org.opt4j.core.optimizer.Control;
import org.opt4j.core.optimizer.Optimizer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The {@code DefaultGUIFrame} is an empty implementation of the GUIFrame.
 * 
 * @author lukasiewycz
 * 
 */
@SuppressWarnings("serial")
@Singleton
public class DefaultGUIFrame extends StandardGUIFrame {

    /**
	 * Constructs a {@code DefaultGUIFrame}.
	 * 
	 * @param populationWidget
	 *            the population widget
	 * @param archiveWidget
	 *            the archive widget
	 * @param plotWidget
	 *            the plot widget
	 * @param controlPanel
	 *            the control panel
	 * @param optimizer
	 *            the optimizer
	 * @param control
	 *            the control
	 */
    @Inject
    public DefaultGUIFrame(PopulationWidget populationWidget, ArchiveWidget archiveWidget, PlotWidget plotWidget, ControlPanel controlPanel, Optimizer optimizer, Control control) {
        super(populationWidget, archiveWidget, plotWidget, controlPanel, optimizer, control, "Viewer", CloseEvent.STOP, false);
    }
}
