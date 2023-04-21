package xbird.xquery.type.xs;

import javax.xml.datatype.*;
import xbird.xqj.XQJConstants;
import xbird.xquery.XQueryException;
import xbird.xquery.dm.value.AtomicValue;
import xbird.xquery.dm.value.xsi.GregorianDateTimeValue;
import xbird.xquery.meta.DynamicContext;
import xbird.xquery.misc.XsDatatypeFactory;
import xbird.xquery.type.*;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class GYearType extends GregorianBaseType {

    private static final long serialVersionUID = -3791658253278465788L;

    public static final String SYMBOL = "xs:gYear";

    public static final GYearType GYEAR = new GYearType();

    public GYearType() {
        super(TypeTable.GYEAR_TID, SYMBOL);
    }

    public AtomicValue createInstance(String literal, AtomicType srcType, DynamicContext dynEnv) throws XQueryException {
        return new GregorianDateTimeValue(literal, this);
    }

    public GregorianDateTimeValue createInstance(XMLGregorianCalendar value) {
        final DatatypeFactory factory = XsDatatypeFactory.getDatatypeFactory();
        int year = value.getYear();
        int tz = value.getTimezone();
        XMLGregorianCalendar cal = factory.newXMLGregorianCalendarDate(year, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, tz);
        return new GregorianDateTimeValue(cal, GYEAR);
    }

    @Override
    public boolean isDaySet() {
        return false;
    }

    @Override
    public boolean isMonthSet() {
        return false;
    }

    @Override
    protected boolean isSuperTypeOf(final AtomicType expected) {
        return expected instanceof GYearType;
    }

    @Override
    public int getXQJBaseType() {
        return XQJConstants.XQBASETYPE_GYEAR;
    }
}
