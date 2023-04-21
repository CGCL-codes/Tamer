package net.sf.bddbddb.ir.highlevel;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.bddbddb.Attribute;
import net.sf.bddbddb.Relation;
import net.sf.bddbddb.ir.Operation;

/**
 * Rename
 * 
 * @author jwhaley
 * @version $Id: Rename.java,v 1.1 2004/10/16 02:44:58 joewhaley Exp $
 */
public class Rename extends HighLevelOperation {

    Relation r0, r1;

    Map renames;

    /**
     * @param r0
     * @param r1
     */
    public Rename(Relation r0, Relation r1, Map renames) {
        super();
        this.r0 = r0;
        this.r1 = r1;
        this.renames = renames;
    }

    public String toString() {
        return r0.toString() + " = " + getExpressionString();
    }

    public String getExpressionString() {
        StringBuffer sb = new StringBuffer();
        sb.append("rename(");
        sb.append(r1.toString());
        for (Iterator i = renames.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry p = (Map.Entry) i.next();
            sb.append(',');
            Attribute a1 = (Attribute) p.getKey();
            sb.append(a1.getRelation());
            sb.append('.');
            sb.append(a1.toString());
            sb.append("->");
            Attribute a2 = (Attribute) p.getValue();
            sb.append(a2.getRelation());
            sb.append('.');
            sb.append(a2.toString());
        }
        sb.append(")");
        return sb.toString();
    }

    public Object visit(HighLevelOperationVisitor i) {
        return i.visit(this);
    }

    public Relation getRelationDest() {
        return r0;
    }

    public List getSrcs() {
        return Collections.singletonList(r1);
    }

    /**
     * @return Returns the source relation.
     */
    public Relation getSrc() {
        return r1;
    }

    /**
     * @return  the rename map
     */
    public Map getRenameMap() {
        return renames;
    }

    public Operation copy() {
        return new Rename(r0, r1, renames);
    }

    public void replaceSrc(Relation r_old, Relation r_new) {
        if (r1 == r_old) r1 = r_new;
    }

    public void setRelationDest(Relation r0) {
        this.r0 = r0;
    }
}
