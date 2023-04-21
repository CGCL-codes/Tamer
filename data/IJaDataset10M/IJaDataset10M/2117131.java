package org.makagiga.commons;

import javax.swing.JProgressBar;

/** A progress bar. */
public class MProgressBar extends JProgressBar {

    /**
	 * Constructs a progress bar.
	 */
    public MProgressBar() {
    }

    /**
	 * Increases value by one.
	 */
    public void step() {
        setValue(getValue() + 1);
    }
}
