package org.exist.xquery.value;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.exist.xquery.XPathException;

public class GMonthDayValue extends AbstractDateTimeValue {

    public GMonthDayValue() throws XPathException {
        super(stripCalendar(TimeUtils.getInstance().newXMLGregorianCalendar(new GregorianCalendar())));
    }

    public GMonthDayValue(XMLGregorianCalendar calendar) throws XPathException {
        super(stripCalendar((XMLGregorianCalendar) calendar.clone()));
    }

    public GMonthDayValue(String timeValue) throws XPathException {
        super(timeValue);
        try {
            if (calendar.getXMLSchemaType() != DatatypeConstants.GMONTHDAY) throw new IllegalStateException();
        } catch (IllegalStateException e) {
            throw new XPathException("xs:time instance must not have year, month or day fields set");
        }
    }

    private static XMLGregorianCalendar stripCalendar(XMLGregorianCalendar calendar) {
        calendar = (XMLGregorianCalendar) calendar.clone();
        calendar.setYear(DatatypeConstants.FIELD_UNDEFINED);
        calendar.setHour(DatatypeConstants.FIELD_UNDEFINED);
        calendar.setMinute(DatatypeConstants.FIELD_UNDEFINED);
        calendar.setSecond(DatatypeConstants.FIELD_UNDEFINED);
        calendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
        return calendar;
    }

    public AtomicValue convertTo(int requiredType) throws XPathException {
        switch(requiredType) {
            case Type.GMONTHDAY:
            case Type.ATOMIC:
            case Type.ITEM:
                return this;
            case Type.STRING:
                return new StringValue(getStringValue());
            case Type.UNTYPED_ATOMIC:
                return new UntypedAtomicValue(getStringValue());
            default:
                throw new XPathException("Type error: cannot cast xs:time to " + Type.getTypeName(requiredType));
        }
    }

    protected AbstractDateTimeValue createSameKind(XMLGregorianCalendar cal) throws XPathException {
        return new GMonthDayValue(cal);
    }

    public int getType() {
        return Type.GMONTHDAY;
    }

    protected QName getXMLSchemaType() {
        return DatatypeConstants.GMONTHDAY;
    }

    public ComputableValue minus(ComputableValue other) throws XPathException {
        throw new XPathException("Subtraction is not supported on values of type " + Type.getTypeName(getType()));
    }
}
