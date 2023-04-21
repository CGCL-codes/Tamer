package _2.plan.ucc.ean.impl;

/**
 * An XML OperationalExceptionCriterionType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public class OperationalExceptionCriterionTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements _2.plan.ucc.ean.OperationalExceptionCriterionType {

    public OperationalExceptionCriterionTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName ACTIVITYTYPE$0 = new javax.xml.namespace.QName("", "activityType");

    /**
     * Gets the "activityType" attribute
     */
    public _2.plan.ucc.ean.ActivityTypeCodeListType.Enum getActivityType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(ACTIVITYTYPE$0);
            if (target == null) {
                return null;
            }
            return (_2.plan.ucc.ean.ActivityTypeCodeListType.Enum) target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "activityType" attribute
     */
    public _2.plan.ucc.ean.ActivityTypeCodeListType xgetActivityType() {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ActivityTypeCodeListType target = null;
            target = (_2.plan.ucc.ean.ActivityTypeCodeListType) get_store().find_attribute_user(ACTIVITYTYPE$0);
            return target;
        }
    }

    /**
     * Sets the "activityType" attribute
     */
    public void setActivityType(_2.plan.ucc.ean.ActivityTypeCodeListType.Enum activityType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(ACTIVITYTYPE$0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(ACTIVITYTYPE$0);
            }
            target.setEnumValue(activityType);
        }
    }

    /**
     * Sets (as xml) the "activityType" attribute
     */
    public void xsetActivityType(_2.plan.ucc.ean.ActivityTypeCodeListType activityType) {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ActivityTypeCodeListType target = null;
            target = (_2.plan.ucc.ean.ActivityTypeCodeListType) get_store().find_attribute_user(ACTIVITYTYPE$0);
            if (target == null) {
                target = (_2.plan.ucc.ean.ActivityTypeCodeListType) get_store().add_attribute_user(ACTIVITYTYPE$0);
            }
            target.set(activityType);
        }
    }
}
