package com.google.gdata.model.gd;

import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes the status of an event attendee.
 *
 * 
 */
public class AttendeeStatus extends Element {

    /** Value. */
    public static final class Value {

        /** Attendee has accepted. */
        public static final String ACCEPTED = Namespaces.gPrefix + "event.accepted";

        /** Attendee has declined. */
        public static final String DECLINED = Namespaces.gPrefix + "event.declined";

        /** Invitation has been sent, but the person has not accepted. */
        public static final String INVITED = Namespaces.gPrefix + "event.invited";

        /** Attendee has accepted tentatively. */
        public static final String TENTATIVE = Namespaces.gPrefix + "event.tentative";

        /** Array containing all available values. */
        private static final String[] ALL_VALUES = { ACCEPTED, DECLINED, INVITED, TENTATIVE };

        /** Returns an array of all values defined in this class. */
        public static String[] values() {
            return ALL_VALUES;
        }

        private Value() {
        }
    }

    /**
   * The key for this element.
   */
    public static final ElementKey<Void, AttendeeStatus> KEY = ElementKey.of(new QName(Namespaces.gNs, "attendeeStatus"), Void.class, AttendeeStatus.class);

    /**
   * Value.
   */
    public static final AttributeKey<String> VALUE = AttributeKey.of(new QName(null, "value"), String.class);

    static {
        ElementCreator builder = DefaultRegistry.build(KEY);
        builder.addAttribute(VALUE).setRequired(true);
    }

    /**
   * Default mutable constructor.
   */
    public AttendeeStatus() {
        this(KEY);
    }

    /**
   * Create an instance using a different key.
   */
    public AttendeeStatus(ElementKey<Void, ? extends AttendeeStatus> key) {
        super(key);
    }

    /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
    public AttendeeStatus(ElementKey<Void, ? extends AttendeeStatus> key, Element source) {
        super(key, source);
    }

    @Override
    public AttendeeStatus lock() {
        return (AttendeeStatus) super.lock();
    }

    /**
   * Returns the value.
   *
   * @return value
   */
    public String getValue() {
        return super.getAttributeValue(VALUE);
    }

    /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
    public AttendeeStatus setValue(String value) {
        super.setAttributeValue(VALUE, value);
        return this;
    }

    /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!sameClassAs(obj)) {
            return false;
        }
        AttendeeStatus other = (AttendeeStatus) obj;
        return eq(getValue(), other.getValue());
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        if (getValue() != null) {
            result = 37 * result + getValue().hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        return "{AttendeeStatus value=" + getAttributeValue(VALUE) + "}";
    }
}
