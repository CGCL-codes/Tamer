package net.sourceforge.geeboss.view.editors.pedal;

import net.sourceforge.geeboss.model.editors.EditorHandler;
import net.sourceforge.geeboss.model.editors.Parameter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Loop stomp box
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public class Loop extends TwoButtonStomp {

    /** The background image */
    private static final Image BACKGROUND_IMAGE = new Image(Display.getCurrent(), Loop.class.getResourceAsStream("images/loop.png"));

    /**
     * Create a new Loop stomp
     * @param handler the associated editor handler
     * @param parent the parent component
     * @param style the style flag
     * @param background the background the pedal will be displayed on
     */
    public Loop(EditorHandler handler, Composite parent, int style, Image background) {
        super(handler, parent, style, background);
    }

    /**
     * Returns the implementation specific left parameter
     * @return the implementation specific left parameter
     */
    protected Parameter getLeftParameter() {
        return mEditorHandler.getParameter("Send");
    }

    /**
     * Returns the implementation specific right parameter
     * @return the implementation specific right parameter
     */
    protected Parameter getRightParameter() {
        return mEditorHandler.getParameter("Return");
    }

    /**
     * Get the implementation specific background image
     * @return the implementation specific background image
     */
    protected Image getBackgroundImage() {
        return BACKGROUND_IMAGE;
    }

    public int getDrive() {
        return mLeftKnob.getValue();
    }

    public void setDrive(int aDrive) {
        mLeftKnob.setValue(aDrive);
    }
}
