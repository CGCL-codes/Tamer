package _2.plan.ucc.ean.impl;

/**
 * An XML EventTacticType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public class EventTacticTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements _2.plan.ucc.ean.EventTacticType {

    public EventTacticTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName EVENTTACTICTYPE$0 = new javax.xml.namespace.QName("", "eventTacticType");

    private static final javax.xml.namespace.QName COMMENTTEXT$2 = new javax.xml.namespace.QName("", "commentText");

    private static final javax.xml.namespace.QName EVENTAMOUNT$4 = new javax.xml.namespace.QName("", "eventAmount");

    private static final javax.xml.namespace.QName ACTIVITYPERIOD$6 = new javax.xml.namespace.QName("", "activityPeriod");

    /**
     * Gets the "eventTacticType" element
     */
    public _2.plan.ucc.ean.EventTacticTypeCodeType getEventTacticType() {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.EventTacticTypeCodeType target = null;
            target = (_2.plan.ucc.ean.EventTacticTypeCodeType) get_store().find_element_user(EVENTTACTICTYPE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "eventTacticType" element
     */
    public void setEventTacticType(_2.plan.ucc.ean.EventTacticTypeCodeType eventTacticType) {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.EventTacticTypeCodeType target = null;
            target = (_2.plan.ucc.ean.EventTacticTypeCodeType) get_store().find_element_user(EVENTTACTICTYPE$0, 0);
            if (target == null) {
                target = (_2.plan.ucc.ean.EventTacticTypeCodeType) get_store().add_element_user(EVENTTACTICTYPE$0);
            }
            target.set(eventTacticType);
        }
    }

    /**
     * Appends and returns a new empty "eventTacticType" element
     */
    public _2.plan.ucc.ean.EventTacticTypeCodeType addNewEventTacticType() {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.EventTacticTypeCodeType target = null;
            target = (_2.plan.ucc.ean.EventTacticTypeCodeType) get_store().add_element_user(EVENTTACTICTYPE$0);
            return target;
        }
    }

    /**
     * Gets the "commentText" element
     */
    public java.lang.String getCommentText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(COMMENTTEXT$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "commentText" element
     */
    public _2.plan.ucc.ean.EventTacticType.CommentText xgetCommentText() {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.EventTacticType.CommentText target = null;
            target = (_2.plan.ucc.ean.EventTacticType.CommentText) get_store().find_element_user(COMMENTTEXT$2, 0);
            return target;
        }
    }

    /**
     * True if has "commentText" element
     */
    public boolean isSetCommentText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(COMMENTTEXT$2) != 0;
        }
    }

    /**
     * Sets the "commentText" element
     */
    public void setCommentText(java.lang.String commentText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(COMMENTTEXT$2, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(COMMENTTEXT$2);
            }
            target.setStringValue(commentText);
        }
    }

    /**
     * Sets (as xml) the "commentText" element
     */
    public void xsetCommentText(_2.plan.ucc.ean.EventTacticType.CommentText commentText) {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.EventTacticType.CommentText target = null;
            target = (_2.plan.ucc.ean.EventTacticType.CommentText) get_store().find_element_user(COMMENTTEXT$2, 0);
            if (target == null) {
                target = (_2.plan.ucc.ean.EventTacticType.CommentText) get_store().add_element_user(COMMENTTEXT$2);
            }
            target.set(commentText);
        }
    }

    /**
     * Unsets the "commentText" element
     */
    public void unsetCommentText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(COMMENTTEXT$2, 0);
        }
    }

    /**
     * Gets the "eventAmount" element
     */
    public _2.ucc.ean.MeasurementValueType getEventAmount() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.MeasurementValueType target = null;
            target = (_2.ucc.ean.MeasurementValueType) get_store().find_element_user(EVENTAMOUNT$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * True if has "eventAmount" element
     */
    public boolean isSetEventAmount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(EVENTAMOUNT$4) != 0;
        }
    }

    /**
     * Sets the "eventAmount" element
     */
    public void setEventAmount(_2.ucc.ean.MeasurementValueType eventAmount) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.MeasurementValueType target = null;
            target = (_2.ucc.ean.MeasurementValueType) get_store().find_element_user(EVENTAMOUNT$4, 0);
            if (target == null) {
                target = (_2.ucc.ean.MeasurementValueType) get_store().add_element_user(EVENTAMOUNT$4);
            }
            target.set(eventAmount);
        }
    }

    /**
     * Appends and returns a new empty "eventAmount" element
     */
    public _2.ucc.ean.MeasurementValueType addNewEventAmount() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.MeasurementValueType target = null;
            target = (_2.ucc.ean.MeasurementValueType) get_store().add_element_user(EVENTAMOUNT$4);
            return target;
        }
    }

    /**
     * Unsets the "eventAmount" element
     */
    public void unsetEventAmount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EVENTAMOUNT$4, 0);
        }
    }

    /**
     * Gets the "activityPeriod" element
     */
    public _2.ucc.ean.TimePeriodType getActivityPeriod() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.TimePeriodType target = null;
            target = (_2.ucc.ean.TimePeriodType) get_store().find_element_user(ACTIVITYPERIOD$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * True if has "activityPeriod" element
     */
    public boolean isSetActivityPeriod() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(ACTIVITYPERIOD$6) != 0;
        }
    }

    /**
     * Sets the "activityPeriod" element
     */
    public void setActivityPeriod(_2.ucc.ean.TimePeriodType activityPeriod) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.TimePeriodType target = null;
            target = (_2.ucc.ean.TimePeriodType) get_store().find_element_user(ACTIVITYPERIOD$6, 0);
            if (target == null) {
                target = (_2.ucc.ean.TimePeriodType) get_store().add_element_user(ACTIVITYPERIOD$6);
            }
            target.set(activityPeriod);
        }
    }

    /**
     * Appends and returns a new empty "activityPeriod" element
     */
    public _2.ucc.ean.TimePeriodType addNewActivityPeriod() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.TimePeriodType target = null;
            target = (_2.ucc.ean.TimePeriodType) get_store().add_element_user(ACTIVITYPERIOD$6);
            return target;
        }
    }

    /**
     * Unsets the "activityPeriod" element
     */
    public void unsetActivityPeriod() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ACTIVITYPERIOD$6, 0);
        }
    }

    /**
     * An XML commentText(@).
     *
     * This is an atomic type that is a restriction of _2.plan.ucc.ean.EventTacticType$CommentText.
     */
    public static class CommentTextImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements _2.plan.ucc.ean.EventTacticType.CommentText {

        public CommentTextImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType, false);
        }

        protected CommentTextImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
