package serp.bytecode.lowlevel;

import java.io.*;
import serp.bytecode.visitor.*;

/**
 * A reference to a class method.
 *
 * @author Abe White
 */
public class MethodEntry extends ComplexEntry {

    /**
     * Default constructor.
     */
    public MethodEntry() {
    }

    /**
     * Constructor.
     *
     * @see ComplexEntry#ComplexEntry(int,int)
     */
    public MethodEntry(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    public int getType() {
        return Entry.METHOD;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterMethodEntry(this);
        visit.exitMethodEntry(this);
    }
}
