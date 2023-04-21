package net.sf.vgap4.assistant.factories;

import java.util.Hashtable;
import java.util.logging.Logger;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import net.sf.vgap4.assistant.exceptions.ResourceException;
import net.sf.vgap4.assistant.figures.draw2d.StandardLabel;
import net.sf.vgap4.assistant.ui.Activator;

public class ImageFactory {

    private static Logger logger = Logger.getLogger("net.sf.vgap4.assistant.factories");

    private static Hashtable<String, Image> imageRegistry = new Hashtable<String, Image>();

    public ImageFactory() {
    }

    public static Image getImage(String reference) throws ResourceException {
        Image image = imageRegistry.get(reference);
        if (null == image) {
            try {
                ImageData imageData = Activator.getImageDescriptor(reference).getImageData();
                image = new Image(Display.getCurrent(), imageData);
                imageRegistry.put(reference, image);
                return image;
            } catch (NullPointerException npe) {
                logger.severe("Image reference:" + reference + " not found on the plug-in namespace.");
                return getImage("icons/alt_window_16.gif");
            }
        } else return image;
    }

    public static Figure getFigure(String reference) throws ResourceException {
        return getLabel(reference);
    }

    public static Label getLabel(String reference) throws ResourceException {
        Label imageFigure = new StandardLabel();
        imageFigure.setIcon(getImage(reference));
        imageFigure.setIconAlignment(PositionConstants.LEFT);
        imageFigure.setIconTextGap(0);
        return imageFigure;
    }
}
