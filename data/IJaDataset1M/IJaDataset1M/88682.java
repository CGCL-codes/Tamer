package org.sgodden.echo.ext20;

import nextapp.echo.app.HttpImageReference;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.Label;

/**
 * An example custom wait indicator which uses a 'throbber' that
 * animates when a server interaction is underway.
 * @author bwoods
 *
 */
@SuppressWarnings("serial")
public class ApplicationWaitIndicator extends Label {

    public static final String PROPERTY_WAIT_ICON = "waitIcon";

    public static final String PROPERTY_NO_WAIT_ICON = "noWaitIcon";

    private static final ImageReference defaultWaitIcon = new HttpImageReference("resources/ext/images/default/grid/wait.gif");

    private static final ImageReference defaultNoWaitIcon = new HttpImageReference("resources/ext/images/default/grid/nowait.gif");

    public ApplicationWaitIndicator() {
        super(defaultNoWaitIcon);
        setRenderId("applicationWaitIndicator");
        setWaitIcon(defaultWaitIcon);
        setNoWaitIcon(defaultNoWaitIcon);
    }

    public ImageReference getWaitIcon() {
        return (ImageReference) get(PROPERTY_WAIT_ICON);
    }

    public void setWaitIcon(ImageReference waitIcon) {
        set(PROPERTY_WAIT_ICON, waitIcon);
    }

    public ImageReference getNoWaitIcon() {
        return (ImageReference) get(PROPERTY_NO_WAIT_ICON);
    }

    public void setNoWaitIcon(ImageReference waitIcon) {
        set(PROPERTY_NO_WAIT_ICON, waitIcon);
    }
}
