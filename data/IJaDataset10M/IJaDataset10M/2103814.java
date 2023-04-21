package org.nakedobjects.viewer.lightweight.util;

import org.apache.log4j.Logger;
import org.nakedobjects.utility.Configuration;
import org.nakedobjects.viewer.lightweight.Background;
import org.nakedobjects.viewer.lightweight.Canvas;
import org.nakedobjects.viewer.lightweight.Icon;
import org.nakedobjects.viewer.lightweight.Location;
import org.nakedobjects.viewer.lightweight.Size;

public class LogoBackground implements Background {

    private static final Logger LOG = Logger.getLogger(LogoBackground.class);

    private static final String PARAMETER_BASE = "viewer.lightweight.logo-background.";

    private Size logoSize;

    private Icon logo;

    private Location location;

    public LogoBackground() {
        Configuration cp = Configuration.getInstance();
        String fileName = cp.getString(PARAMETER_BASE + "image", "logo.gif");
        logo = ImageFactory.getImageFactory().loadImage(fileName);
        if (logo == null) {
            LOG.warn("Logo image not found");
        } else {
            location = new Location();
            location.setX(cp.getInteger(PARAMETER_BASE + "position.getX()", 0));
            location.setY(cp.getInteger(PARAMETER_BASE + "position.getY()", 0));
            logoSize = new Size();
            logoSize.setWidth(cp.getInteger(PARAMETER_BASE + "size.width", logo.getWidth()));
            logoSize.setHeight(cp.getInteger(PARAMETER_BASE + "size.height", logo.getHeight()));
        }
    }

    public void paintBackground(Canvas canvas, Size viewSize) {
        if (logo != null) {
            int x;
            int y;
            if (location.getX() == 0 && location.getY() == 0) {
                x = viewSize.getWidth() / 2 - logoSize.getWidth() / 2;
                y = viewSize.getHeight() / 2 - logoSize.getHeight() / 2;
            } else {
                x = (location.getX() >= 0) ? location.getX() : viewSize.getWidth() + location.getX() - logoSize.getWidth();
                y = (location.getY() >= 0) ? location.getY() : viewSize.getHeight() + location.getY() - logoSize.getHeight();
            }
            canvas.drawIcon(logo, x, y, logoSize.getWidth(), logoSize.getHeight());
        }
    }
}
