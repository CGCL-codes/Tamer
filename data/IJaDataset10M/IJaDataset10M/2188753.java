package org.fife.ui.rtextarea;

import javax.swing.Icon;

/**
 * Information about an icon displayed in a {@link Gutter}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface GutterIconInfo {

    /**
	 * Returns the icon being rendered.
	 *
	 * @return The icon being rendered.
	 */
    public Icon getIcon();

    /**
	 * Returns the offset that is being tracked.  The line of this offset is
	 * where the icon is rendered.  This offset may change as the user types.
	 *
	 * @return The offset being tracked.
	 */
    public int getMarkedOffset();
}
