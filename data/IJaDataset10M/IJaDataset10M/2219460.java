package net.sf.bddbddb.ir.highlevel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import net.sf.bddbddb.Relation;
import net.sf.bddbddb.ir.Operation;

/**
 * Project
 * 
 * @author jwhaley
 * @version $Id: Project.java,v 1.2 2005/02/21 02:32:50 cs343 Exp $
 */
public class Project extends HighLevelOperation {

    Relation r0, r1;

    List attributes;

    /**
     * @param r0
     * @param r1
     */
    public Project(Relation r0, Relation r1) {
        super();
        this.r0 = r0;
        this.r1 = r1;
        this.attributes = new LinkedList(r1.getAttributes());
        this.attributes.removeAll(r0.getAttributes());
    }

    public String toString() {
        return r0.toString() + " = " + getExpressionString();
    }

    public String getExpressionString() {
        return "project(" + r1.toString() + "," + attributes.toString() + ")";
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
     * @return  list of attributes being projected
     */
    public List getAttributes() {
        return attributes;
    }

    public Operation copy() {
        return new Project(r0, r1);
    }

    public void replaceSrc(Relation r_old, Relation r_new) {
        if (r1 == r_old) r1 = r_new;
    }

    public void setRelationDest(Relation r0) {
        this.r0 = r0;
    }
}
