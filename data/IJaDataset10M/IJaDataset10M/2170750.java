package es.caib.zkib.datamodel.xml.definition;

import org.w3c.dom.Element;
import es.caib.zkib.datamodel.xml.ParseException;

public class AttributeDefinition implements DefinitionInterface {

    private String name;

    private String value;

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the value.
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value The value to set.
	 */
    public void setValue(String value) {
        this.value = value;
    }

    public AttributeDefinition() {
        super();
    }

    public void test(Element element) throws ParseException {
        if (name == null) throw new ParseException("Name attribute is mandatory", element);
    }
}
