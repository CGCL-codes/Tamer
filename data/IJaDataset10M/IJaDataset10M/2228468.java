package net.sf.openforge.verilog.model;

import java.util.*;

/**
 * MemoryDeclaration declares a memory. All {@link Register Registers} added
 * to the declaration must of the same bit-width and address depth.
 * <P>
 * Example:<BR>
 * <CODE>
 * reg [31:0] memA [255:0]; // declares a 32-bit wide memory which is 256 words deep
 * </CODE>
 *
 * <P>
 *
 * Created: Fri Feb 09 2001
 *
 * @author abk
 * @version $Id: MemoryDeclaration.java 2 2005-06-09 20:00:48Z imiller $
 */
public class MemoryDeclaration extends NetDeclaration implements Statement {

    private static final String _RCS_ = "RCS_REVISION: $Rev: 2 $";

    int upper;

    int lower;

    /**
     * Constucts a new MemoryDeclaration for a Register with specified
     * upper and lower addresses.
     *
     * @param memory the Register base for the memory
     * @param upper the upper address limit
     * @param lower the lower address limit
     */
    public MemoryDeclaration(Register memory, int upper, int lower) {
        super(memory);
        this.upper = upper;
        this.lower = lower;
    }

    public MemoryDeclaration(Register[] memories, int upper, int lower) {
        this(memories[0], upper, lower);
        for (int i = 1; i < memories.length; i++) {
            add(memories[i]);
        }
    }

    public MemoryDeclaration(InitializedMemory mem) {
        this(mem, mem.depth() - 1, 0);
    }

    public void add(Register memory) {
        super.add(memory);
    }

    public Lexicality lexicalify() {
        Lexicality lex = new Lexicality();
        lex.append(type);
        if (width > 1) {
            lex.append(new Range(msb, lsb));
        }
        for (Iterator it = nets.iterator(); it.hasNext(); ) {
            lex.append(((Net) it.next()).getIdentifier());
            lex.append(new Range(upper, lower));
            if (it.hasNext()) lex.append(Symbol.COMMA);
        }
        lex.append(Symbol.SEMICOLON);
        return lex;
    }

    public String toString() {
        return lexicalify().toString();
    }
}
