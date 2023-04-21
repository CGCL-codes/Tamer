package spagswt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import spagcore.SpaglCoreCheckBox;
import spagcore.SpaglCoreContainer;
import spagcore.SpaglCoreEvent;
import spagcore.SpaglCoreFont;
import spagcore.SpaglCoreImage;
import spagswt.SpaglImage;
import spagswt.SpaglContainer;
import spagswt.SpaglFont;

/**
 * A check box (org.eclipse.swt.widgets.Button) that conforms to the SpaglCoreCheckbox interface
 * @author Sam Pottinger
 */
public class SpaglCheckBox implements SpaglSwtWidget, SpaglCoreCheckBox {

    private SpaglCoreContainer parentWidget;

    private Button innerWidget;

    private SpaglCoreFont widgetFont;

    private SpaglCoreImage widgetImage;

    /**
	 * Creates a bare bones checkbox
	 * 
	 * @param newParent The parent {@link SpaglContainer} object
	 */
    public SpaglCheckBox(SpaglCoreContainer newParent) {
        innerWidget = new Button(((SpaglSwtContainer) newParent).getComposite(), SWT.CHECK);
        parentWidget = newParent;
    }

    /**
	 * Creates a check box with a specified starting text
	 * 
	 * @param newParent The parent {@link SpaglContainer} object
	 * @param newText The text to be displayed on the right of this widget
	 */
    public SpaglCheckBox(SpaglCoreContainer newParent, String newText) {
        innerWidget = new Button(((SpaglSwtContainer) newParent).getComposite(), SWT.CHECK);
        parentWidget = newParent;
        innerWidget.setText(newText);
    }

    /**
	 * Creates a check box with a specified starting text and layout parameters
	 * 
	 * @param newParent The parent {@link SpaglContainer} object
	 * @param newText The text to be displayed on the right of this widget
	 * @param parameters The parameters for the layout manager
	 */
    public SpaglCheckBox(SpaglCoreContainer newParent, String newText, String parameters) {
        innerWidget = new Button(((SpaglSwtContainer) newParent).getComposite(), SWT.CHECK);
        parentWidget = newParent;
        parentWidget.placeWidget(this, parameters);
        innerWidget.setText(newText);
    }

    /**
	 * Gets the native SWT widget (not cross-library)
	 * 
	 * @return Returns a SWT {@link Control} object
	 */
    public Control getNativeWidget() {
        return innerWidget;
    }

    /**
	 * Gets the parent container for the object
	 * 
	 * @return The {@link SpaglContainer} for this object as a {@link SpaglCoreContainer}
	 */
    public SpaglCoreContainer getContainer() {
        return (SpaglSwtContainer) parentWidget;
    }

    /**
	 * Gets the height of this widget
	 * 
	 * @return The native height of the widget
	 */
    public int getHeight() {
        return innerWidget.getBounds().y;
    }

    /**
	 * Gets the width of this widget
	 * 
	 * @return The native width of the widget
	 */
    public int getWidth() {
        return innerWidget.getBounds().x;
    }

    /**
	 * Sets the size of this widget
	 * 
	 * @param width The integer y size
	 * @param height The integer x size
	 */
    public void setSize(int width, int height) {
        innerWidget.setSize(width, height);
    }

    /**
	 * Get the text on the right of the widget
	 * 
	 * @return The text associated with the check box
	 */
    public String getText() {
        return innerWidget.getText();
    }

    /**
	 * Determines if the widget is selected
	 * 
	 * @return True if the widget is selected or false if it is not
	 */
    public boolean isSelected() {
        return innerWidget.getSelection();
    }

    /**
	 * Set the selection state of the widget
	 * 
	 * @param newValue True if this widget will display a check mark or false if it is to be empty
	 */
    public void setSelected(boolean newValue) {
        innerWidget.setSelection(newValue);
    }

    /**
	 * Sets the text displayed to the right of this widget
	 * 
	 * @param newText the text that will be associated with the object
	 */
    public void setText(String newText) {
        innerWidget.setText(newText);
    }

    /**
	 * Gets the native widget font
	 * 
	 * @return The native inner widget as a swt {@link Control} object.
	 */
    public SpaglCoreFont getWidgetFont() {
        return widgetFont;
    }

    /**
	 * Sets the font for this widget
	 * 
	 * @param newFont a {@link SpaglFont} to associate with the object
	 */
    public void setFont(SpaglCoreFont newFont) {
        widgetFont = newFont;
        innerWidget.setFont(((SpaglFont) newFont).getNativeFont());
    }

    /**
	 * Sets the font for this widget with an option of disposing the font directly
	 * 
	 * @param newFont a {@link SpaglFont} to associate with the object
	 * @param dispose true if the font should be disposed of immediatley (not recommended)
	 */
    public void setFont(SpaglCoreFont newFont, boolean dispose) {
        innerWidget.setFont(((SpaglFont) newFont).getNativeFont());
        if (dispose) {
            widgetFont = null;
            ((SpaglFont) newFont).dispose();
        } else widgetFont = newFont;
    }

    /**
	 * Gets the image associated with the widget
	 * 
	 * @return The object's {@link SpaglImage} as a {@link SpaglCoreImage}
	 */
    public SpaglCoreImage getImage() {
        return widgetImage;
    }

    /**
	 * Set the image for this widget
	 * 
	 * @param newImage A {@link SpaglImage} that will act as this widget's new image
	 */
    public void setImage(SpaglCoreImage newImage) {
        widgetImage = newImage;
        innerWidget.setImage(((SpaglImage) newImage).getNativeImage());
    }

    /**
	 * Set the image for this widget with the option of immediate disposal
	 * 
	 * @param newImage A {@link SpaglImage} that will act as this widget's new image
	 * @param dispose True if this image should be dispoed right away (not recommended)
	 */
    public void setImage(SpaglCoreImage newImage, boolean dispose) {
        innerWidget.setImage(((SpaglImage) newImage).getNativeImage());
        if (dispose) {
            widgetImage = null;
            ((SpaglImage) newImage).dispose();
        } else widgetImage = newImage;
    }

    /**
	 * Disposes this widget through SWT
	 */
    public void dispose() {
        innerWidget.dispose();
    }

    /**
	 * Adds an event listener to this check box
	 * 
	 * @param eventID The ID of the event to be listened for
	 * @param method The (string) name of the method to be called when the event is fired
	 * @param o The object where the method should be fired
	 */
    public void addEventListener(int eventID, String method, Object o) {
        try {
            switch(eventID) {
                case SpaglEvent.SELECTION_CHANGED_EVENT:
                    {
                        innerWidget.addListener(SWT.Selection, new SpaglEventListener(o.getClass().getMethod(method, SpaglCoreEvent.class), o, SpaglEvent.SELECTION_EVENT));
                        break;
                    }
                default:
                    throw new IllegalArgumentException("Event not supported by this widget");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
