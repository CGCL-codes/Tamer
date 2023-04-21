package _2.plan.ucc.ean.impl;

/**
 * An XML AbstractForecastType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public class AbstractForecastTypeImpl extends _2.plan.ucc.ean.impl.PlanDocumentTypeImpl implements _2.plan.ucc.ean.AbstractForecastType {

    public AbstractForecastTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName FORECASTPURPOSE$0 = new javax.xml.namespace.QName("", "forecastPurpose");

    /**
     * Gets the "forecastPurpose" attribute
     */
    public _2.plan.ucc.ean.ForecastPurposeCodeListType.Enum getForecastPurpose() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(FORECASTPURPOSE$0);
            if (target == null) {
                return null;
            }
            return (_2.plan.ucc.ean.ForecastPurposeCodeListType.Enum) target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "forecastPurpose" attribute
     */
    public _2.plan.ucc.ean.ForecastPurposeCodeListType xgetForecastPurpose() {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ForecastPurposeCodeListType target = null;
            target = (_2.plan.ucc.ean.ForecastPurposeCodeListType) get_store().find_attribute_user(FORECASTPURPOSE$0);
            return target;
        }
    }

    /**
     * Sets the "forecastPurpose" attribute
     */
    public void setForecastPurpose(_2.plan.ucc.ean.ForecastPurposeCodeListType.Enum forecastPurpose) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(FORECASTPURPOSE$0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(FORECASTPURPOSE$0);
            }
            target.setEnumValue(forecastPurpose);
        }
    }

    /**
     * Sets (as xml) the "forecastPurpose" attribute
     */
    public void xsetForecastPurpose(_2.plan.ucc.ean.ForecastPurposeCodeListType forecastPurpose) {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ForecastPurposeCodeListType target = null;
            target = (_2.plan.ucc.ean.ForecastPurposeCodeListType) get_store().find_attribute_user(FORECASTPURPOSE$0);
            if (target == null) {
                target = (_2.plan.ucc.ean.ForecastPurposeCodeListType) get_store().add_attribute_user(FORECASTPURPOSE$0);
            }
            target.set(forecastPurpose);
        }
    }
}
