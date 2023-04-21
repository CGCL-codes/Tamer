package de.tu_berlin.math.coga.zet.viewer;

import de.tu_berlin.math.coga.common.algorithm.AlgorithmEvent;
import de.tu_berlin.math.coga.common.algorithm.AlgorithmListener;
import de.tu_berlin.math.coga.common.algorithm.AlgorithmProgressEvent;
import de.tu_berlin.math.coga.common.util.Formatter;
import de.tu_berlin.math.coga.components.JStatusBar;
import javax.swing.JProgressBar;

/**
 * @author Jan-Philipp Kappmeier
 */
public class JEventStatusBar extends JStatusBar implements AlgorithmListener {

    JProgressBar progressBar = new JProgressBar(0, 100);

    /**
	 * Initializes an empty status bar.
	 */
    public JEventStatusBar() {
        super();
        addElement(progressBar);
        rebuild();
    }

    /**
	 * Listener is called if the algorithm sends an event. When the algorithm
	 * runs in another thread (the {@link SwingWorker} thread), the progress bar
	 * updates itself during the runtime.
	 * @param event the event thrown. not only progress events. take care.
	 */
    public void eventOccurred(AlgorithmEvent event) {
        if (event instanceof AlgorithmProgressEvent) {
            final double progress = ((AlgorithmProgressEvent) event).getProgress();
            progressBar.setValue((int) (progress * 100));
            progressBar.setToolTipText(Formatter.formatPercent(progress));
        }
    }
}
