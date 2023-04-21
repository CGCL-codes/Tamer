package fr.mywiki.business.entry.meta;

import java.util.Date;
import java.util.Set;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import fr.mywiki.business.entry.meta.datatype.Choice;
import fr.mywiki.business.entry.meta.datatype.DateProperty;
import fr.mywiki.business.entry.meta.datatype.EntryLink;
import fr.mywiki.business.entry.meta.datatype.number.IntNumber;
import fr.mywiki.business.entry.meta.datatype.number.RealNumber;
import fr.mywiki.business.entry.meta.datatype.text.SimpleText;
import fr.mywiki.business.entry.meta.datatype.text.WikiText;

/**
 * This class deals with the creation of specific <code>Property</code>
 * objects.
 */
public class PropertyFactory {

    /**
	 * Standard creation.
	 * 
	 * @param name
	 *          the name of the property.
	 * @param typeName
	 *          the type of the property.
	 * @param required
	 *          is this property required ?
	 * @return a <code>Property</code> object.
	 */
    public static Property createProperty(String name, String typeName, boolean required) {
        return new Property(name, typeName, required);
    }

    /**
	 * Standard creation.
	 * 
	 * @param name
	 *          the name of the property.
	 * @param typeName
	 *          the type of the property.
	 * @return a <code>Property</code> object.
	 */
    public static Property createProperty(String name, String typeName) {
        return createProperty(name, typeName, false);
    }

    public static Property createIntNumberProperty(String name, boolean required) {
        return createProperty(name, IntNumber.TYPE_NAME, required);
    }

    public static Property createIntNumberProperty(String name) {
        return createProperty(name, IntNumber.TYPE_NAME);
    }

    public static Property createIntNumberProperty(String name, Long min, Long max) {
        return createIntNumberProperty(name, min, max, false);
    }

    public static Property createIntNumberProperty(String name, Long min, Long max, boolean required) {
        Property prop = new Property(name);
        IntNumber datatype = new IntNumber(min, max);
        prop.setType(datatype);
        prop.setRequired(new Boolean(required));
        return prop;
    }

    public static Property createRealNumberProperty(String name, boolean required) {
        return createProperty(name, RealNumber.TYPE_NAME, required);
    }

    public static Property createRealProperty(String name) {
        return createProperty(name, RealNumber.TYPE_NAME);
    }

    public static Property createRealNumberProperty(String name, Double min, Double max) {
        return createRealNumberProperty(name, min, max, false);
    }

    public static Property createRealNumberProperty(String name, Double min, Double max, boolean required) {
        Property prop = new Property(name);
        RealNumber datatype = new RealNumber(min, max);
        prop.setType(datatype);
        prop.setRequired(new Boolean(required));
        return prop;
    }

    public static Property createSimpleTextProperty(String name, boolean required) {
        return createProperty(name, SimpleText.TYPE_NAME, required);
    }

    public static Property createSimpleTextProperty(String name) {
        return createProperty(name, SimpleText.TYPE_NAME);
    }

    public static Property createSimpleTextProperty(String name, int maxLength) {
        return createSimpleTextProperty(name, maxLength, false);
    }

    public static Property createSimpleTextProperty(String name, int maxLength, boolean required) {
        Property prop = new Property(name);
        SimpleText datatype = new SimpleText(maxLength);
        prop.setType(datatype);
        prop.setRequired(new Boolean(required));
        return prop;
    }

    public static Property createWikiTextProperty(String name, boolean required) {
        return createProperty(name, WikiText.TYPE_NAME, required);
    }

    public static Property createWikiTextField(String name) {
        return createProperty(name, WikiText.TYPE_NAME);
    }

    public static Property createEntryLinkProperty(String name, int minLinks, int maxLinks, Set potentials, boolean required) {
        Property prop = new Property(name, required);
        DataType datatype = new EntryLink(minLinks, maxLinks, potentials);
        prop.setType(datatype);
        return prop;
    }

    public static Property createEntryLinkProperty(String name, int minLinks, int maxLinks, boolean required) {
        return createEntryLinkProperty(name, minLinks, maxLinks, null, required);
    }

    public static Property createEntryLinkProperty(String name, int minLinks, int maxLinks) {
        return createEntryLinkProperty(name, minLinks, maxLinks, null, false);
    }

    public static Property createEntryLinkProperty(String name) {
        return createEntryLinkProperty(name, EntryLink.MIN_LINKS, EntryLink.MAX_LINKS, null, false);
    }

    public static Property createEntryLinkProperty(String name, boolean required) {
        return createEntryLinkProperty(name, EntryLink.MIN_LINKS, EntryLink.MAX_LINKS, null, required);
    }

    public static Property createEntryLinkProperty(String name, Set potentials) {
        return createEntryLinkProperty(name, EntryLink.MIN_LINKS, EntryLink.MAX_LINKS, potentials, false);
    }

    public static Property createEntryLinkProperty(String name, Set potentials, boolean required) {
        return createEntryLinkProperty(name, EntryLink.MIN_LINKS, EntryLink.MAX_LINKS, potentials, required);
    }

    public static Property createChoiceProperty(String name, int minLinks, int maxLinks, Set potentials, boolean required) {
        Property prop = new Property(name, required);
        DataType datatype = new EntryLink(minLinks, maxLinks, potentials);
        prop.setType(datatype);
        return prop;
    }

    public static Property createChoiceProperty(String name, int minLinks, int maxLinks, boolean required) {
        Property prop = new Property(name, required);
        DataType datatype = new EntryLink(minLinks, maxLinks);
        prop.setType(datatype);
        return prop;
    }

    public static Property createChoiceProperty(String name, int minLinks, int maxLinks) {
        Property prop = new Property(name);
        DataType datatype = new EntryLink(minLinks, maxLinks);
        prop.setType(datatype);
        return prop;
    }

    public static Property createChoiceProperty(String name) {
        Property prop = new Property(name);
        DataType datatype = new EntryLink();
        prop.setType(datatype);
        return prop;
    }

    public static Property createChoiceProperty(String name, boolean required) {
        Property prop = new Property(name, required);
        DataType datatype = new EntryLink();
        prop.setType(datatype);
        return prop;
    }

    public static Property createChoiceProperty(String name, Set potentials) {
        Property prop = new Property(name);
        DataType datatype = new EntryLink(potentials);
        prop.setType(datatype);
        return prop;
    }

    public static Property createChoiceProperty(String name, Set potentials, boolean required) {
        Property prop = new Property(name, required);
        DataType datatype = new EntryLink(potentials);
        prop.setType(datatype);
        return prop;
    }

    public static Property createDateProperty(String name, Date minDate, Date maxDate, boolean required) {
        Property prop = new Property(name, required);
        DataType datatype = new DateProperty(minDate, maxDate);
        prop.setType(datatype);
        return prop;
    }

    public static Property createDateProperty(String name, Date minDate, Date maxDate) {
        Property prop = new Property(name);
        DataType datatype = new DateProperty(minDate, maxDate);
        prop.setType(datatype);
        return prop;
    }

    public static Property createDateProperty(String name, boolean required) {
        Property prop = new Property(name, required);
        DataType datatype = new DateProperty();
        prop.setType(datatype);
        return prop;
    }

    public static Property createDateProperty(String name) {
        Property prop = new Property(name);
        DataType datatype = new DateProperty();
        prop.setType(datatype);
        return prop;
    }

    /**
	 * Creates a <code>Property</code> object from a Xml String which
	 * corresponds to its representation inside the database.
	 * 
	 * @param elt
	 *          a <code>Element</code> object which enables the whole creation
	 *          of the property.
	 * @return a Property object.
	 */
    public static Property createProperty(Element elt) {
        Property prop = null;
        String type = elt.getName();
        String name = elt.getAttributeValue(Property.FIELD_NAME);
        Attribute reqStr = elt.getAttribute(Property.FIELD_REQUIRED);
        boolean required = reqStr != null && Boolean.TRUE.toString().equals(reqStr.getValue());
        if (SimpleText.TYPE_NAME.equals(type)) {
            prop = createSimpleTextProperty(name, required);
        } else if (WikiText.TYPE_NAME.equals(type)) {
            prop = createWikiTextProperty(name, required);
        } else if (IntNumber.TYPE_NAME.equals(type)) {
            prop = createIntNumberProperty(name, required);
        } else if (RealNumber.TYPE_NAME.equals(type)) {
            prop = createRealNumberProperty(name, required);
        } else if (DateProperty.TYPE_NAME.equals(type)) {
            prop = createDateProperty(name, required);
        } else if (Choice.TYPE_NAME.equals(type)) {
            prop = createChoiceProperty(name, required);
        } else if (EntryLink.TYPE_NAME.equals(type)) {
            prop = createEntryLinkProperty(name, required);
        }
        if (prop != null) prop.getType().fillFromXml(elt);
        return prop;
    }

    /**
	 * Creates a <code>Property</code> object from a Xml String which
	 * corresponds to its representation inside the database.
	 * 
	 * @param elt
	 *          a <code>Document</code> object which enables the whole creation
	 *          of the property.
	 * @return a Property object.
	 */
    public static Property createProperty(Document doc) {
        return createProperty(doc.getRootElement());
    }
}
