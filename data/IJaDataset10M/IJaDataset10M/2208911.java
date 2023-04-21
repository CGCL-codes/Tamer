package lcd_api.widgets;

import lcd_api.DynamicValueManager;
import lcd_api.dynamic_values.DynamicValue;
import lcd_api.dynamic_values.StringDynamicValue;
import lcd_api.dynamic_values.IDynamicValue.ValueType;
import lcd_api.infrastructure.exceptions.DynamicValueNotRegisteredException;
import lcd_api.infrastructure.exceptions.IllegalAttributeException;
import lcd_api.infrastructure.exceptions.MessageIncompleteException;
import lcd_api.infrastructure.exceptions.WidgetPropertiesAlreadySetException;
import lcd_api.messages.outbound.WidgetAdd;
import lcd_api.messages.outbound.WidgetSet;
import lcd_api.messages.outbound.attributes.IconTypes;
import lcd_api.messages.outbound.attributes.WidgetTypeEnum;

/**
 * An IconWidget represents a Widget on the LCD screen containing a pre-defined
 * icon symbol. The ability of this symbol to be displayed on the LCD is largely
 * dependent on the current LCD driver and must be one of the pre-defined
 * IconTypes enumerated values.
 * 
 * If an unknown Icon is included in the Widget definition, the ELLIPSIS Icon
 * will be the default value used.
 * 
 * @author Robert Derelanko
 */
public class IconWidget extends Widget {

    /**
	 * This field will contain the DynamicValue referencing the Icon name to 
	 * display on the server. If the IconName was supplied in the definition as
	 * a constant, this will simply be a Constant DynamicValue generated at
	 * runtime.
	 */
    private StringDynamicValue iconNameValue = null;

    /**
	 * The main constructor.
	 * 
	 * @param theStartX The starting X character position of the widget.
	 * @param theStartY The starting Y character position of the widget.
	 * @param theScreenID The SCREEN_ID of the Screen the widget will be 
	 * associated with.
	 * @param theWidgetSerial The unique serial number associated with the
	 * widget that must be in the range 0-999.
	 * @throws IllegalArgumentException If the supplied widgetSerial does not 
	 * conform to the expected format (see Widget.generateWidgetID() for more 
	 * information) then an IllegalArgumentException may be thrown.
	 */
    public IconWidget(int theStartX, int theStartY, String theScreenID, int theWidgetSerial) {
        super(theStartX, theStartY, theScreenID);
        if (theWidgetSerial < 0) {
            throw new IllegalArgumentException("theWidgetSerial paramter must be a positive value.");
        }
        this.widgetType = WidgetType.ICON;
        try {
            this.widgetID = this.generateWidgetID(theWidgetSerial);
        } catch (IllegalArgumentException anException) {
            StringBuilder eb = new StringBuilder();
            eb.append("The supplied widgetSerial appears to be in an ");
            eb.append("unexpected format. Unable to set the widgetID.");
            throw new IllegalArgumentException(eb.toString());
        }
        this.height = 1;
        this.width = 1;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void defineWidget(String theWidgetDefinition) throws DynamicValueNotRegisteredException {
        if (theWidgetDefinition == null || theWidgetDefinition.length() == 0) {
            StringBuilder eb = new StringBuilder();
            eb.append("The supplied widget definition was null or empty.");
            throw new IllegalArgumentException(eb.toString());
        }
        String dynamicValueID = DynamicValue.extractTriggerID(theWidgetDefinition);
        if (dynamicValueID != null) {
            DynamicValue theValue = DynamicValueManager.getInstance().getDynamicValue(dynamicValueID);
            if (theValue == null) {
                StringBuilder eb = new StringBuilder();
                eb.append("Unable to locate a registered DynamicValue ");
                eb.append("associated with the ID: \"");
                eb.append(dynamicValueID);
                eb.append("\"");
                throw new DynamicValueNotRegisteredException(eb.toString());
            }
            if (theValue.getType() != ValueType.STRING) {
                StringBuilder eb = new StringBuilder();
                eb.append("A ");
                eb.append(theValue.getType().toString());
                eb.append(" DynamicValue was detected in the Widget ");
                eb.append("definition but only a ");
                eb.append(ValueType.STRING.toString());
                eb.append(" DynamicValue is currently supported.");
                throw new IllegalArgumentException(eb.toString());
            }
            this.iconNameValue = (StringDynamicValue) theValue;
        } else {
            StringDynamicValue constantIconName = new StringDynamicValue("Icon_Name");
            constantIconName.markConstant();
            constantIconName.setValue(theWidgetDefinition);
            this.iconNameValue = constantIconName;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public WidgetAdd getWidgetAddMessage() throws MessageIncompleteException {
        WidgetAdd addMessage = new WidgetAdd();
        try {
            addMessage.setWidgetID(this.widgetID);
        } catch (IllegalAttributeException aBadAttribute) {
            StringBuilder eb = new StringBuilder();
            eb.append("The Widget's widgetID appears to be invalid. This ");
            eb.append("should not be possible within this point of the code ");
            eb.append("unless there is a bug in the constructor.");
            throw new MessageIncompleteException(eb.toString());
        }
        try {
            addMessage.setScreenID(this.associatedScreenID);
        } catch (IllegalAttributeException aBadAttribute) {
            StringBuilder eb = new StringBuilder();
            eb.append("The Widget's widgetID appears to be invalid. This ");
            eb.append("should not be possible within this point of the code ");
            eb.append("unless there is a bug in the constructor.");
            throw new MessageIncompleteException(eb.toString());
        }
        addMessage.setWidgetType(WidgetTypeEnum.ICON);
        return addMessage;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public WidgetSet getWidgetSetMessage() throws MessageIncompleteException {
        WidgetSet setMessage = new WidgetSet();
        try {
            setMessage.setWidgetID(this.widgetID);
        } catch (IllegalAttributeException aBadAttribute) {
            StringBuilder eb = new StringBuilder();
            eb.append("The Widget's widgetID appears to be invalid. This ");
            eb.append("should not be possible within this point of the code ");
            eb.append("unless there is a bug in the constructor.");
            throw new MessageIncompleteException(eb.toString());
        }
        IconTypes referencedType = IconTypes.getType(this.iconNameValue.getValue());
        if (referencedType == null) {
            referencedType = IconTypes.ELLIPSIS;
        }
        try {
            setMessage.setProps_Icon(this.startX, this.startY, referencedType);
        } catch (WidgetPropertiesAlreadySetException alreadySetException) {
        }
        try {
            setMessage.setScreenID(this.associatedScreenID);
        } catch (IllegalAttributeException aBadAttribute) {
            StringBuilder eb = new StringBuilder();
            eb.append("The Widget's widgetID appears to be invalid. This ");
            eb.append("should not be possible within this point of the code ");
            eb.append("unless there is a bug in the constructor.");
            throw new MessageIncompleteException(eb.toString());
        }
        this.timeLastSent = System.currentTimeMillis();
        return setMessage;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean wasRecentlyUpdated() {
        if (this.iconNameValue == null) {
            return false;
        }
        if (this.iconNameValue.getTimeLastUpdated() > this.timeLastSent) {
            return true;
        }
        return false;
    }
}
