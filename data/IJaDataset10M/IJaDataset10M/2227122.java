package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * <code>Synthetic_attribute</code>.
 */
public class SyntheticAttribute extends AttributeInfo {

    /**
     * The name of this attribute <code>"Synthetic"</code>.
     */
    public static final String tag = "Synthetic";

    SyntheticAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    /**
     * Constructs a Synthetic attribute.
     *
     * @param cp                a constant pool table.
     */
    public SyntheticAttribute(ConstPool cp) {
        super(cp, tag, new byte[0]);
    }

    /**
     * Makes a copy.
     *
     * @param newCp     the constant pool table used by the new copy.
     * @param classnames        should be null.
     */
    public AttributeInfo copy(ConstPool newCp, Map classnames) {
        return new SyntheticAttribute(newCp);
    }
}
