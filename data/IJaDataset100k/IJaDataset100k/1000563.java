package org.designerator.common.interfaces;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

public interface ICustomTabFolder {

    /**
	 * @return titles of tabs
	 */
    public String[] getItemTitles();

    public Composite getControl();

    /**
	 * @param color or null to reset to system default
	 */
    public void setBorderColor(Color color);
}
