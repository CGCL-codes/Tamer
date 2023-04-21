package javax.swing.text.rtf;

import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import java.io.IOException;

/** 
 * This interface describes a class which defines a 1-1 mapping between
 * an RTF keyword and a SwingText attribute.
 */
interface RTFAttribute {

    static final int D_CHARACTER = 0;

    static final int D_PARAGRAPH = 1;

    static final int D_SECTION = 2;

    static final int D_DOCUMENT = 3;

    static final int D_META = 4;

    public int domain();

    public Object swingName();

    public String rtfName();

    public boolean set(MutableAttributeSet target);

    public boolean set(MutableAttributeSet target, int parameter);

    public boolean setDefault(MutableAttributeSet target);

    public boolean write(AttributeSet source, RTFGenerator target, boolean force) throws IOException;

    public boolean writeValue(Object value, RTFGenerator target, boolean force) throws IOException;
}
