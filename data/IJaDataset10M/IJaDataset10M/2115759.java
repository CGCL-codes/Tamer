package ca.uhn.hl7v2.model;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.EncodingCharacters;

/**
 * An abstract Type that provides a default implementation of getName(). 
 * 
 * @author Bryan Tripp
 */
public abstract class AbstractType implements Type {

    private static final long serialVersionUID = -6976260024197429201L;

    private final ExtraComponents extra;

    private final Message message;

    /** 
     * Creates a new instance of AbstractType
     * @param message message to which this type belongs 
     */
    public AbstractType(Message message) {
        extra = new ExtraComponents(message);
        this.message = message;
    }

    /** Returns the name of the type (used in XML encoding and profile checking)  */
    public String getName() {
        String longClassName = this.getClass().getName();
        return longClassName.substring(longClassName.lastIndexOf('.') + 1);
    }

    /** @see Type#getExtraComponents */
    public ExtraComponents getExtraComponents() {
        return this.extra;
    }

    /**
     * @return the message to which this Type belongs
     */
    public Message getMessage() {
        return message;
    }

    /**
     * {@inheritDoc }
     */
    public void parse(String string) throws HL7Exception {
        clear();
        getMessage().getParser().parse(this, string, EncodingCharacters.getInstance(getMessage()));
    }

    /**
     * {@inheritDoc }
     */
    public String encode() throws HL7Exception {
        return getMessage().getParser().doEncode(this, EncodingCharacters.getInstance(getMessage()));
    }

    /**
	 * {@inheritDoc }
	 */
    public void clear() {
        extra.clear();
    }

    /**
	 * Returns the datatype and attempts to pipe-encode it. For example, a string implementation
	 * might return "ST[Value^Value2]". This is only intended for logging/debugging purposes.
	 */
    @Override
    public String toString() {
        return toString(this);
    }

    /**
	 * Returns the datatype and attempts to pipe-encode it. For example, a string implementation
	 * might return "ST[Value^Value2]". This is only intended for logging/debugging purposes.
	 */
    static String toString(Type theType) {
        StringBuilder b = new StringBuilder();
        b.append(theType.getClass().getSimpleName());
        b.append("[");
        try {
            b.append(theType.encode());
        } catch (HL7Exception e) {
            b.append("Unable to encode");
        }
        b.append("]");
        return b.toString();
    }
}
