package org.tzi.use.parser.use;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tzi.use.parser.AST;
import org.tzi.use.parser.Context;
import org.tzi.use.parser.MyToken;
import org.tzi.use.uml.mm.MMultiplicity;

/**
 * Node of the abstract syntax tree constructed by the parser.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public class ASTMultiplicity extends AST {

    private MyToken fStartToken;

    private List fRanges;

    public ASTMultiplicity(MyToken t) {
        fStartToken = t;
        fRanges = new ArrayList();
    }

    public void addRange(ASTMultiplicityRange mr) {
        fRanges.add(mr);
    }

    public MMultiplicity gen(Context ctx) {
        MMultiplicity mult = ctx.modelFactory().createMultiplicity();
        try {
            if (fRanges.isEmpty()) {
                addRange(new ASTMultiplicityRange(MMultiplicity.UNDEFINED));
            }
            Iterator it = fRanges.iterator();
            while (it.hasNext()) {
                ASTMultiplicityRange mr = (ASTMultiplicityRange) it.next();
                mult.addRange(mr.fLow, mr.fHigh);
            }
        } catch (IllegalArgumentException ex) {
            ctx.reportError(fStartToken, ex);
        }
        return mult;
    }

    public String toString() {
        return "FIXME";
    }
}
