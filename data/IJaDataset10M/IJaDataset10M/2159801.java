package com.sun.org.apache.bcel.internal.generic;

import java.io.IOException;
import com.sun.org.apache.bcel.internal.util.ByteSequence;

/** 
 * LDC_W - Push item from constant pool (wide index)
 *
 * <PRE>Stack: ... -&gt; ..., item.word1, item.word2</PRE>
 *
 * @version $Id: LDC_W.java,v 1.1.2.1 2005/07/31 23:45:57 jeffsuttor Exp $
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public class LDC_W extends LDC {

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LDC_W() {
    }

    public LDC_W(int index) {
        super(index);
    }

    /**
   * Read needed data (i.e., index) from file.
   */
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        setIndex(bytes.readUnsignedShort());
        opcode = com.sun.org.apache.bcel.internal.Constants.LDC_W;
    }
}
