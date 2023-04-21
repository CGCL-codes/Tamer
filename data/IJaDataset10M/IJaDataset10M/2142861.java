package org.hip.kernel.bom.impl;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import org.hip.kernel.bom.AbstractSerializer;
import org.hip.kernel.bom.DomainObjectIterator;
import org.hip.kernel.bom.DomainObjectVisitor;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.Property;
import org.hip.kernel.bom.PropertySet;
import org.hip.kernel.bom.SortedArray;
import org.hip.kernel.sys.VSys;

/**
 * 	This class is a Serializer for XML objects.
 *  Includes a filter for XML data.
 * 
 * 	@author		Benno Luthiger
 * 	@see		java.io.Serializable
 */
public class XMLSerializer extends AbstractSerializer implements Serializable {

    private DecimalFormat decimalFormat = new DecimalFormat();

    private XMLCharacterFilter xmlCharFilter;

    protected Locale locale = VSys.dftLocale;

    /**
	 * XMLSerializer default constructor.
	 */
    public XMLSerializer() {
        super();
    }

    /**
	 * XMLSerializer constructor for inputed inLevel.
	 *
	 * @param inLevel int
	 */
    public XMLSerializer(int inLevel) {
        super(inLevel);
    }

    /**
	 * XMLSerializer for inputed inLevel filtering the characters in inFilter.
	 *
	 * @param inLevel int
	 * @param inFilter org.hip.kernel.bom.impl.XMLCharacterFilter
	 */
    public XMLSerializer(int inLevel, XMLCharacterFilter inFilter) {
        super(inLevel);
        xmlCharFilter = inFilter;
    }

    /**
	 * XMLSerializer filtering the characters in inFilter.
	 *
	 * @param inFilter org.hip.kernel.bom.impl.XMLCharacterFilter
	 */
    public XMLSerializer(XMLCharacterFilter inFilter) {
        super();
        xmlCharFilter = inFilter;
    }

    /**
	 * Setter for filter to be used for serializing.
	 * 
	 * @param inFilter XMLCharacterFilter or <code>null</code>.
	 */
    public void setFilter(XMLCharacterFilter inFilter) {
        xmlCharFilter = inFilter;
    }

    /**
	 * Emits a comment line.
	 * 
	 * @param inComment java.lang.String
	 */
    protected synchronized void emitComment(String inComment) {
        emit_nl();
        emit("<!--" + inComment + "-->");
    }

    /**
	 * Emits an end tag.
	 * 
	 * @param inContent java.lang.String
	 */
    protected synchronized void emitEndTag(String inContent) {
        getBuffer().append("</" + inContent + ">");
    }

    /**
	 * Emits a start tag.
	 * 
	 * @param inContent java.lang.String
	 */
    protected synchronized void emitStartTag(String inContent) {
        emit("<" + inContent + ">");
    }

    /**
	 * Filters text if a XMLCharacterFilter is set and appends text.
	 * 
	 * @param inObject java.lang.String
	 */
    protected synchronized void emitText(Object inObject) {
        if (inObject == null) {
            getBuffer().append(getNullString());
        } else {
            if (xmlCharFilter != null) {
                getBuffer().append(xmlCharFilter.filter(inObject.toString()));
            } else {
                getBuffer().append(inObject);
            }
        }
    }

    /**
	 * Emits the sequence ending the visit of a DomainObject
	 *
	 * @param inObject org.hip.kernel.bom.GeneralDomainObject
	 */
    protected void endDomainObject(GeneralDomainObject inObject) {
        emit_nl();
        emit_indent();
        emitEndTag(inObject.getObjectName());
    }

    /**
	 * Emits the sequence ending the visit of a DomainObjectIterator
	 *
	 * @param inIterator org.hip.kernel.bom.DomainObjectIterator
	 */
    protected void endIterator(DomainObjectIterator inIterator) {
        emit_nl();
        emit_indent();
        emitEndTag("ObjectList");
    }

    /**
	 * Sequence ending the visit of a Property
	 *
	 * @param inProperty org.hip.kernel.bom.Property
	 */
    protected void endProperty(Property inProperty) {
        emitEndTag(inProperty.getName());
    }

    /**
	 * Sequence ending the visit of a PropertySet
	 *
	 * @param inSet org.hip.kernel.bom.PropertySet
	 */
    protected void endPropertySet(PropertySet inSet) {
        emit_nl();
        emit_indent();
        emitEndTag(DomainObjectVisitor.PROPERTY_SET_TAG);
    }

    /**
	 * Emits the sequence ending the visit of a SortedArray
	 *
	 * @param inSortedArray org.hip.kernel.bom.SortedArray
	 */
    protected void endSortedArray(SortedArray inSortedArray) {
        emit_nl();
        emit_indent();
        emitEndTag("ObjectList");
    }

    public void start() {
    }

    /**
	 * Sequence starting the visit of a DomainObject
	 *
	 * @param inObject org.hip.kernel.bom.GeneralDomainObject
	 */
    protected void startDomainObject(GeneralDomainObject inObject) {
        emit_nl();
        emitStartTag(inObject.getObjectName());
    }

    /**
	 * Sequence starting the visit of a DomainObjectIterator
	 *
	 * @param inIterator org.hip.kernel.bom.DomainObjectIterator
	 */
    protected void startIterator(DomainObjectIterator inIterator) {
        emit_nl();
        emitStartTag("ObjectList");
    }

    /**
	 * Sequence starting the visit of a Property
	 *
	 * @param inProperty org.hip.kernel.bom.Property
	 */
    protected void startProperty(Property inProperty) {
        emit_nl();
        emitStartTag(inProperty.getName());
        Object lValue = inProperty.getValue();
        String lFormatPattern = inProperty.getFormatPattern();
        if (lValue == null) return;
        if ("none".equals(lFormatPattern)) {
            emitText(inProperty.getValue());
            return;
        }
        if (lValue instanceof Timestamp) {
            DateFormat lDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            Calendar lCalendar = lDateFormat.getCalendar();
            lCalendar.setTime((Timestamp) lValue);
            emitText(lDateFormat.format(lCalendar.getTime()));
            return;
        }
        if (lValue instanceof Date) {
            DateFormat lDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            Calendar lCalendar = lDateFormat.getCalendar();
            lCalendar.setTime((Date) lValue);
            emitText(lDateFormat.format(lCalendar.getTime()));
            return;
        }
        if (lValue instanceof Time) {
            DateFormat lTimeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
            Calendar lCalendar = lTimeFormat.getCalendar();
            lCalendar.setTime((Time) lValue);
            emitText(lTimeFormat.format(lCalendar.getTime()));
            return;
        }
        if (lValue instanceof Number) {
            if (lFormatPattern != null) {
                if (lFormatPattern.equals(" ") && ((Number) lValue).intValue() == 0) return;
                decimalFormat.applyPattern(lFormatPattern);
                emitText(decimalFormat.format((Number) lValue));
                return;
            } else {
                emitText(NumberFormat.getNumberInstance().format((Number) lValue));
                return;
            }
        }
        emitText(emitPropertyValue(inProperty));
    }

    /**
	 * Subclasses can overwrite this method if they want the property value 
	 * emitted in a special way.
	 * 
	 * @param inProperty org.hip.kernel.bom.Property
	 * @return java.lang.Object
	 */
    protected Object emitPropertyValue(Property inProperty) {
        return inProperty.getValue();
    }

    /**
	 * Sequence starting the visit of a PropertySet
	 *
	 * @param inSet org.hip.kernel.bom.PropertySet
	 */
    protected void startPropertySet(PropertySet inSet) {
        emit_nl();
        emitStartTag(DomainObjectVisitor.PROPERTY_SET_TAG);
    }

    /**
	 * Sequence starting the visit of a SortedArray
	 *
	 * @param inSortedArray org.hip.kernel.bom.SortedArray
	 */
    protected void startSortedArray(SortedArray inSortedArray) {
        emit_nl();
        emitStartTag("SortedArray");
    }

    /**
	 * Set the Locale the date values can be formatted with.
	 * 
	 * @param inLocale java.util.Locale
	 */
    public void setLocale(Locale inLocale) {
        if (inLocale != null) locale = inLocale;
    }
}
