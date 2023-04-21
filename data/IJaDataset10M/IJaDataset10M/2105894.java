package de.haumacher.timecollect;

import de.haumacher.timecollect.LayoutConfig.Bounds;

public interface DialogConfig {

    /**
	 * Bounds of the dialog.
	 */
    Bounds getBounds();

    /**
	 * Notifies about an update of the {@link #getBounds()}.
	 */
    void notifyUpdate();
}
