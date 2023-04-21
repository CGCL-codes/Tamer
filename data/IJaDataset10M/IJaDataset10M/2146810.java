package net.jmarias.uqueue.contact;

import java.util.*;
import java.io.*;

/**
 * Logical states to classify a Contact. They are only useful for reporting because
 * its final status isn't wrote down until the contact is ended and removed from
 * the system, therefore they must not be used as a way for monitoring the live
 * Contact state. There are other live states upon ContactState enum which expose
 * a more suitable monitoring information for Contacts.<br>
 * Note: These scenarios are considered for 2 nodes only sessions. If there were
 * more peers and the Contact creator wasn't participating anymore, other peer than
 * the Contact Owner would be considered
 * @author jose
 */
public final class ContactStatus implements Comparable, Serializable {

    private static int index = 0;

    private final String name;

    private final int value = index++;

    private ContactStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        return value - ((ContactStatus) o).value;
    }

    /** Initial state for the Contact when its's created. Since it hasn't an useful
     * value, it will be overwritten with another final status */
    public static final ContactStatus UNKNOWN = new ContactStatus("UNKNOWN");

    /** The Contact has been attended during it life's cycle. This happens
    * when some Node other than the Originator one establishes the CONNECTED state */
    public static final ContactStatus CONNECTED = new ContactStatus("CONNECTED");

    /** If the Originator Node went to the DISCONNECTED or FAILED state, the Contact
    * status hadn't been established as CONNECTED yet, and the Contact scope was either
    * INBOUND or INTERNAL, the Contact status will be established as ABANDONED */
    public static final ContactStatus ABANDONED = new ContactStatus("ABANDONED");

    /** If the Originator Node went to the DISCONNECTED or FAILED state, the Contact
    * status hadn't been established as CONNECTED yet, and the Contact scope was either
    * OUTBOUND or EXTERNAL, the Contact status will be established as CANCELLED */
    public static final ContactStatus CANCELLED = new ContactStatus("CANCELLED");

    /** If the only Node other than the Originator one went to the DISCONNECTED or
     * FAILED state, the Contact status hadn't been established as CONNECTED yet,
     * and the Contact scope was either INBOUND or INTERNAL, then the Contact
     * status will be established as REJECTED */
    public static final ContactStatus REJECTED = new ContactStatus("REJECTED");

    /** If the only Node other than the Originator one went to the DISCONNECTED or
     * FAILED state, the Contact status hadn't been established as CONNECTED yet,
     * and the Contact scope was either OUTBOUND or EXTERNAL, then the Contact
     * status will be established as FAILED */
    public static final ContactStatus FAILED = new ContactStatus("FAILED");

    private static final ContactStatus[] PRIVATE_VALUES = { UNKNOWN, CONNECTED, ABANDONED, CANCELLED, REJECTED, FAILED };

    public static final List<ContactStatus> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

    private Object readResolve() throws ObjectStreamException {
        return PRIVATE_VALUES[index];
    }
}
