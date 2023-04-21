package DE.FhG.IGD.semoa.server;

import java.util.*;
import java.io.Serializable;

/**
 * Represents an error code. Error codes are passed to
 * components such as a lifecycle. The error codes can
 * be compared by means of '='.
 *
 * @author Volker Roth
 * @version "$Id: ErrorCode.java 827 2003-01-22 05:29:27Z jpeters $"
 */
public class ErrorCode extends Object implements Serializable {

    /**
     * The forward mapping from name to error code.
     */
    private static Map forward_ = new HashMap(16, 1.0f);

    /**
     * The reverse mapping from code to error name.
     */
    private static List reverse_ = new ArrayList(16);

    /**
     * Everything is fine, no errors.
     */
    public static final ErrorCode OK = ErrorCode.def("OK");

    /**
     * An error occurred that is not of any of the other
     * specified types.
     */
    public static final ErrorCode UNKNOWN = ErrorCode.def("UNKNOWN ERROR");

    /**
     * A thread death was detected.
     */
    public static final ErrorCode THREAD_DEATH = ErrorCode.def("THREAD DEATH");

    /**
     * An agent tried to migrate, but the destination host
     * couldn't be reached.<p>
     *
     * Also: a message could not be delivered because the
     * host of the receiving agent is not reachable.
     */
    public static final ErrorCode DESTINATION_UNREACHABLE = ErrorCode.def("DESTINATION UNREACHABLE");

    /**
     * An agent tried to migrate, but the destination host
     * refused to accept the agent.
     */
    public static final ErrorCode DESTINATION_REFUSED = ErrorCode.def("DESTINATION REFUSED");

    /**
     * A message could not be delivered because the receiving
     * agent is not known at the destination host.
     */
    public static final ErrorCode NO_SUCH_RECIPIENT = ErrorCode.def("NO SUCH RECIPIENT");

    /**
     * A message could not be delivered because the receiving
     * agent is temporarily unavailable at the destination
     * host. However, the receiving agent is known to that
     * host and thus exists.
     */
    public static final ErrorCode RECIPIENT_UNREACHABLE = ErrorCode.def("RECIPIENT UNREACHABLE");

    /**
     * A message could not be delivered because the receiving
     * agent's message queue is congested. Try sending again
     * after a delay and give the agent a chance to clean up.
     */
    public static final ErrorCode RECIPIENT_CONGESTED = ErrorCode.def("RECIPIENT CONGESTED");

    /**
     * A message could not be delivered because the connection
     * to the agent platform couldn't be established.
     */
    public static final ErrorCode HOST_UNREACHABLE = ErrorCode.def("HOST UNREACHABLE");

    /**
     * A message has been modified through the filter.
     * This error code can rather be seen as signal for
     * the <code>OutBox</code> to proceed by forwarding 
     * the modified message instead of the original one.
     */
    public static final ErrorCode MODIFIED = ErrorCode.def("MODIFIED");

    /**
     * An agent/message was rejected. Agents/Messages can be 
     * rejected e.g., when they migrate or when they are filtered. 
     * Rejection means that notification can be given to the 
     * party/agent thatsent the agent/message.
     */
    public static final ErrorCode REJECT = ErrorCode.def("REJECTED");

    /**
     * An agent could not verified properly up to a point
     * where its owner can be identified reliably. Consequently,
     * the agent shall be discarded silently without further
     * notice in order to prevent trivial DoS attacks.
     */
    public static final ErrorCode DISCARD = ErrorCode.def("DISCARD");

    /**
     * The string that identifies this error code.
     */
    private transient String id_;

    /**
     * The error code number.
     */
    private transient int code_;

    /**
     * No one but this class creates instances.
     *
     * @param id The string that identifies the error code.
     * @param code The code number that identifies the error code.
     */
    private ErrorCode(String id, int code) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        id_ = id.toUpperCase();
        code_ = code;
    }

    /**
     * Defines an error code. This method is called only by
     * static initializers of this class. Error codes are
     * also assigned a unique number for identification.
     * This number depends on the order in which error
     * codes are initialized. Java calls initializers in
     * the order in which they are defined, hence, the
     * numbering equals the order of the constant declarations
     * in this class.
     *
     * @param name The human-readable name of the error code.
     */
    private static ErrorCode def(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        name = name.toUpperCase();
        if (forward_.containsKey(name)) {
            throw new IllegalArgumentException("code exists: " + name);
        }
        ErrorCode err;
        err = new ErrorCode(name, reverse_.size());
        forward_.put(name, err);
        reverse_.add(err);
        return err;
    }

    /**
     * @return The error code instance with the given
     *   human-readable name, or <code>UNKNOWN</code>
     *   if no error code with that name is known.
     *   Note that error code names are all upper case.
     */
    public static ErrorCode byName(String name) {
        ErrorCode err;
        if (name == null) {
            return ErrorCode.UNKNOWN;
        }
        err = (ErrorCode) forward_.get(name.toUpperCase());
        if (err == null) {
            return ErrorCode.UNKNOWN;
        }
        return err;
    }

    /**
     * @return The error code instance with the given
     *   number, or <code>UNKNOWN</code> if no error
     *   code with the given number is known.
     */
    public static ErrorCode byNumber(int num) {
        if (num < 0 || num >= reverse_.size()) {
            return ErrorCode.UNKNOWN;
        }
        return (ErrorCode) reverse_.get(num);
    }

    /**
     * @return The number of this error code.
     */
    public int intValue() {
        return code_;
    }

    /**
     * @return The human-readable name of this error code.
     */
    public String toString() {
        return id_;
    }
}
