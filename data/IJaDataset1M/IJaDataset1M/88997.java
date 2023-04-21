package org.nakedobjects.plugins.dndviewer.viewer.basic;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.dndviewer.Background;
import org.nakedobjects.plugins.dndviewer.Canvas;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Image;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Location;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Size;
import org.nakedobjects.plugins.dndviewer.viewer.image.ImageFactory;
import org.nakedobjects.plugins.dndviewer.viewer.util.Properties;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public class LogoBackground implements Background {

    private static final Logger LOG = Logger.getLogger(LogoBackground.class);

    private static final String PARAMETER_BASE = Properties.PROPERTY_BASE + "logo-background.";

    private Location location;

    private Image logo;

    private Size logoSize;

    public LogoBackground() {
        final NakedObjectConfiguration configuration = NakedObjectsContext.getConfiguration();
        final String fileName = configuration.getString(PARAMETER_BASE + "image", "background");
        logo = ImageFactory.getInstance().loadImage(fileName);
        if (logo == null) {
            logo = ImageFactory.getInstance().loadImage("poweredby-logo");
        }
        if (logo == null) {
            LOG.debug("logo image not found: " + fileName);
        } else {
            location = Properties.getLocation(PARAMETER_BASE + "location", new Location(-30, -30));
            logoSize = Properties.getSize(PARAMETER_BASE + "size", logo.getSize());
        }
    }

    public void draw(final Canvas canvas, final Size viewSize) {
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
            canvas.drawImage(logo, x, y, logoSize.getWidth(), logoSize.getHeight());
        }
    }
}
