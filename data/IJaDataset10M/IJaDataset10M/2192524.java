package parsers.dtd.model;

/**
 *
 * @author mateo
 */
public class DTDAttlistState extends DTDState {

    protected String elementName;

    protected String attributeName;

    protected String defaultValue;

    protected String attributeType;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public DTDAttlistState(DTDAttlistState previousState) {
        super(previousState);
        if (previousState != null) {
            this.attributeName = previousState.getAttributeName();
            this.attributeType = previousState.getAttributeType();
            this.defaultValue = previousState.getDefaultValue();
            this.elementName = previousState.getElementName();
        }
    }

    public DTDAttlistState(DTDState previousState) {
        super(previousState);
    }

    @Override
    public DTDState consumeCharacter(char c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
