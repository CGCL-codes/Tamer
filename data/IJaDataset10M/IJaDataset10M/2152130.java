package fr.mywiki.business.entry.meta;

import java.util.Locale;
import org.jdom.Document;
import org.jdom.Element;
import fr.mywiki.business.entry.meta.datatype.Choice;
import fr.mywiki.business.entry.meta.datatype.DateProperty;
import fr.mywiki.business.entry.meta.datatype.EntryLink;
import fr.mywiki.business.entry.meta.datatype.number.IntNumber;
import fr.mywiki.business.entry.meta.datatype.number.RealNumber;
import fr.mywiki.business.entry.meta.datatype.text.SimpleText;
import fr.mywiki.business.entry.meta.datatype.text.WikiText;
import fr.mywiki.business.resources.ResourceManager;
import fr.mywiki.model.ValueInterface;

/**
 * An property is the smallest component of an entry model. It is constituted of
 * a name (to display), a type of used data and some other specifications about
 * the data to enter type in. The term "property" is a bit overrated as this
 * class only contains the property type, the property value being the data
 * entered. The property type and the property value constitute the property.
 */
public class Property implements ValueInterface {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_ENT = "property";

    public static final String FIELD_NAME = "name";

    public static final String FIELD_TYPE = "type";

    public static final String FIELD_REQUIRED = "required";

    public static final String FIELD_DESCRIPTION = "description";

    public static final String LINK_ENTRY_MODEL = "model_id";

    /** The name of this property, which should be displayed on pages */
    private String name;

    /** The data type of this property */
    private DataType type;

    /** Is the property value required ? */
    private Boolean required;

    protected Property() {
        name = new String();
        required = new Boolean(false);
    }

    public Property(String name, boolean required) {
        this.name = name;
        this.required = new Boolean(required);
    }

    protected Property(String name) {
        this(name, false);
    }

    public Property(String name, String typeName, boolean required) {
        this.name = name;
        DataType type = null;
        if (IntNumber.TYPE_NAME.equals(typeName)) type = new IntNumber(); else if (RealNumber.TYPE_NAME.equals(typeName)) type = new RealNumber(); else if (SimpleText.TYPE_NAME.equals(typeName)) type = new SimpleText(); else if (WikiText.TYPE_NAME.equals(typeName)) type = new WikiText(); else if (EntryLink.TYPE_NAME.equals(typeName)) type = new EntryLink(); else if (Choice.TYPE_NAME.equals(typeName)) type = new Choice(); else if (DateProperty.TYPE_NAME.equals(typeName)) type = new DateProperty();
        this.type = type;
        this.required = new Boolean(required);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Boolean isRequired() {
        return required;
    }

    public boolean bIsRequired() {
        return isRequired().booleanValue();
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public void setRequired(boolean required) {
        this.required = new Boolean(required);
    }

    public String getTypeEnt() {
        return TYPE_ENT;
    }

    public Object get(String attName) {
        if (FIELD_NAME.equals(attName)) return getName();
        if (FIELD_TYPE.equals(attName)) return getType();
        if (FIELD_REQUIRED.equals(attName)) return isRequired();
        return null;
    }

    public void set(String attName, Object value) {
    }

    /**
	 * Returns a xml representation of this property type.
	 * 
	 * @return a Element object.
	 */
    protected Element toXml() {
        Element elt = new Element(getType().getTypeName());
        elt.addAttribute(FIELD_NAME, getName());
        elt.addAttribute(FIELD_REQUIRED, isRequired().toString());
        getType().describeXml(elt);
        return elt;
    }

    /**
	 * Returns a xml representation of this property type.
	 * 
	 * @return a <code>Document</code> object. 
	 */
    public Document toXmlDoc() {
        return new Document(toXml());
    }

    public String getTypeName() {
        if (getType() == null) return "";
        return getType().getTypeName();
    }

    /**
	 * Returns the translation of the name of the type into the right language.
	 * 
	 * @param locale a <code>Locale</code> object.
	 * @return a natural text <code>String</code> object.
	 */
    public String getTypeName(Locale locale) {
        return ResourceManager.getMessage(getType().getDisplayKey(), locale);
    }

    public String getRequiredString() {
        return isRequired().toString();
    }
}
