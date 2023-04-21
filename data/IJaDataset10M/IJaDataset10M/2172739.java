package net.sourceforge.geeboss.view.editors.pedal;

import net.sourceforge.geeboss.model.editors.EditorHandler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

/**
 * Volume stomp box
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public class Volume extends Pedal {

    /** The background image */
    private static final Image BACKGROUND_IMAGE = new Image(Display.getCurrent(), Volume.class.getResourceAsStream("images/volume.png"));

    /** Detail button location */
    public static final Point DETAIL_BUTTON_LOCATION = new Point(69, 63);

    /**
     * Create a new Volume stomp
     * @param handler the associated editor handler
     * @param parent the parent component
     * @param style the style flag
     * @param background the background the pedal will be displayed on
     */
    public Volume(EditorHandler handler, Composite parent, int style, Image background) {
        super(handler, parent, style, background);
    }

    /**
     * Get the implementation specific background image
     * @return the implementation specific background image
     */
    protected Image getBackgroundImage() {
        return BACKGROUND_IMAGE;
    }

    /**
     * Get the implementation specific detail button location
     * @return the implementation specific detail button location
     */
    protected Point getDetailButtonLocation() {
        return DETAIL_BUTTON_LOCATION;
    }
}
