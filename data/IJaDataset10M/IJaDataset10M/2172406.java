package org.makagiga.commons.swing;

import javax.swing.JProgressBar;

/**
 * A progress bar.
 * 
 * @since 4.0 (org.makagiga.commons.swing package)
 */
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
