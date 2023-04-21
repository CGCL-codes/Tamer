package de.javatt.data.scenario.gui.verification;

import javax.swing.AbstractButton;

/**
 * AbstractButtonProperty is an ObjectProperty that gives
 * access to AbstractButton instances.
 * 
 * @author Matthias Kempa
 *
 */
public class AbstractButtonProperty extends JComponentProperty {

    private AbstractButton myButton;

    /**
     * 
     */
    public AbstractButtonProperty() {
        super();
    }

    public void setObject(Object obj) {
        if (obj instanceof AbstractButton) {
            myButton = (AbstractButton) obj;
        }
        super.setObject(obj);
    }

    /**
     * Returns the Button's Action command..
     * @return
     */
    public String getActionCommand() {
        String returnValue = (myButton == null) ? null : myButton.getActionCommand();
        return returnValue;
    }

    /**
     * Returns the Button's text..
     * @return
     */
    public String getText() {
        String returnValue = (myButton == null) ? null : myButton.getText();
        return returnValue;
    }

    /**
     * The vertical alignment of the button's text.
     * @return
     */
    public Integer getVerticalAlignment() {
        Integer returnValue = (myButton == null) ? null : new Integer(myButton.getVerticalAlignment());
        return returnValue;
    }

    /**
     * The vertical position of the button's text.
     * @return
     */
    public Integer getVerticalTextPosition() {
        Integer returnValue = (myButton == null) ? null : new Integer(myButton.getVerticalTextPosition());
        return returnValue;
    }

    /**
     * The horizontal alignment of the button's text.
     * @return
     */
    public Integer getHorizontalAlignment() {
        Integer returnValue = (myButton == null) ? null : new Integer(myButton.getHorizontalAlignment());
        return returnValue;
    }

    /**
     * The horizontal position of the button's text.
     * @return
     */
    public Integer getHorizontalTextPosition() {
        Integer returnValue = (myButton == null) ? null : new Integer(myButton.getHorizontalTextPosition());
        return returnValue;
    }

    /**
     * Returns the Border Painted Property.
     * @return
     */
    public Boolean getIsBorderPainted() {
        Boolean returnValue = (myButton == null) ? null : new Boolean(myButton.isBorderPainted());
        return returnValue;
    }

    /**
     * Returns the ContentAreaFilled() Property.
     * @return
     */
    public Boolean getIsContentAreaFilled() {
        Boolean returnValue = (myButton == null) ? null : new Boolean(myButton.isContentAreaFilled());
        return returnValue;
    }

    /**
     * Returns the IsFocusPainted() Property.
     * @return
     */
    public Boolean getIsFocusPainted() {
        Boolean returnValue = (myButton == null) ? null : new Boolean(myButton.isFocusPainted());
        return returnValue;
    }

    /**
     * Returns the IsRolloverEnabled() Property.
     * @return
     */
    public Boolean getIsRolloverEnabled() {
        Boolean returnValue = (myButton == null) ? null : new Boolean(myButton.isRolloverEnabled());
        return returnValue;
    }

    /**
     * Returns the IsSelected() Property.
     * @return
     */
    public Boolean getIsSelected() {
        Boolean returnValue = (myButton == null) ? null : new Boolean(myButton.isSelected());
        return returnValue;
    }
}
