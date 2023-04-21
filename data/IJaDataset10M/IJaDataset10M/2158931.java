package org.avis.io.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolCodecException;
import static org.avis.io.XdrCoding.getObjects;
import static org.avis.io.XdrCoding.getString;
import static org.avis.io.XdrCoding.putObjects;
import static org.avis.io.XdrCoding.putString;

public class Nack extends XidMessage {

    public static final int ID = 48;

    public static final int PROT_INCOMPAT = 0001;

    public static final int PROT_ERROR = 1001;

    public static final int NO_SUCH_SUB = 1002;

    public static final int IMPL_LIMIT = 2006;

    public static final int NOT_IMPL = 2007;

    public static final int PARSE_ERROR = 2101;

    public static final int EXP_IS_TRIVIAL = 2110;

    public static final Object[] EMPTY_ARGS = new Object[0];

    public int error;

    public String message;

    public Object[] args;

    public Nack() {
    }

    public Nack(XidMessage inReplyTo, int error, String message) {
        this(inReplyTo, error, message, EMPTY_ARGS);
    }

    public Nack(XidMessage inReplyTo, int error, String message, Object... args) {
        super(inReplyTo);
        if (message == null) throw new NullPointerException("Message cannot be null");
        this.error = error;
        this.message = message;
        this.args = args;
    }

    @Override
    public int typeId() {
        return ID;
    }

    /**
   * Return the error text for the NACK error code.
   * 
   * @see #errorTextFor(int)
   */
    public String errorCodeText() {
        return errorTextFor(error);
    }

    /**
   * Generate a formatted message from the message template returned
   * by the router. e.g. expand the %1 and %2 in "%1: Expression '%2'
   * does not refer to a name" to the values in <tt>arg [0]</tt> and
   * <tt>arg [1]</tt>.
   */
    public String formattedMessage() {
        if (args.length == 0) {
            return message;
        } else {
            StringBuilder str = new StringBuilder(message);
            for (int i = 0; i < args.length; i++) replace(str, i + 1, args[i]);
            return str.toString();
        }
    }

    /**
   * Replace embedded arg reference(s) with a value.
   * 
   * @param str The string builder to modify.
   * @param argNumber The argument number (1..)
   * @param arg The arg value.
   */
    private static void replace(StringBuilder str, int argNumber, Object arg) {
        String tag = "%" + argNumber;
        int index;
        while ((index = str.indexOf(tag)) != -1) str.replace(index, index + tag.length(), arg.toString());
    }

    /**
   * Return the error text for a given NACK error code.
   */
    public static String errorTextFor(int error) {
        switch(error) {
            case PROT_INCOMPAT:
                return "Incompatible protocol";
            case PROT_ERROR:
                return "Communication protocol error";
            case NO_SUCH_SUB:
                return "Unknown subscription ID";
            case IMPL_LIMIT:
                return "Exceeded client connection resource limit";
            case NOT_IMPL:
                return "Feature not implemented";
            case PARSE_ERROR:
                return "Subscription parse error";
            case EXP_IS_TRIVIAL:
                return "Expression is trivial (constant)";
            default:
                return "Error code " + error;
        }
    }

    @Override
    public void encode(IoBuffer out) throws ProtocolCodecException {
        super.encode(out);
        out.putInt(error);
        putString(out, message);
        putObjects(out, args);
    }

    @Override
    public void decode(IoBuffer in) throws ProtocolCodecException {
        super.decode(in);
        error = in.getInt();
        message = getString(in);
        args = getObjects(in);
    }
}
