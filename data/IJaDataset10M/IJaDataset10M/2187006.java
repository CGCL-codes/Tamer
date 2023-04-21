package net.sf.bddbddb.ir.highlevel;

import net.sf.bddbddb.Relation;
import net.sf.bddbddb.ir.Operation;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDDFactory.BDDOp;

/**
 * Union
 * 
 * @author jwhaley
 * @version $Id: Union.java,v 1.1 2004/10/16 02:44:58 joewhaley Exp $
 */
public class Union extends BooleanOperation {

    /**
     * @param r0
     * @param r1
     * @param r2
     */
    public Union(Relation r0, Relation r1, Relation r2) {
        super(r0, r1, r2);
    }

    public String getName() {
        return "union";
    }

    public Object visit(HighLevelOperationVisitor i) {
        return i.visit(this);
    }

    public BDDOp getBDDOp() {
        return BDDFactory.or;
    }

    public Operation copy() {
        return new Union(r0, r1, r2);
    }
}
