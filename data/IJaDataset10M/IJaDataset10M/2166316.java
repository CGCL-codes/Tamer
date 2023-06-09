package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.Iterator;
import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.type.Type;
import edu.clemson.cs.r2jt.analysis.TypeResolutionException;

public class TupleTy extends Ty {

    /** The location member. */
    private Location location;

    /** The fields member. */
    private List<Ty> fields;

    public TupleTy() {
    }

    ;

    public TupleTy(Location location, List<Ty> fields) {
        this.location = location;
        this.fields = fields;
    }

    /** Returns the value of the location variable. */
    public Location getLocation() {
        return location;
    }

    /** Returns the value of the fields variable. */
    public List<Ty> getFields() {
        return fields;
    }

    /** Sets the location variable to the specified value. */
    public void setLocation(Location location) {
        this.location = location;
    }

    /** Sets the fields variable to the specified value. */
    public void setFields(List<Ty> fields) {
        this.fields = fields;
    }

    /** Accepts a ResolveConceptualVisitor. */
    public void accept(ResolveConceptualVisitor v) {
        v.visitTupleTy(this);
    }

    /** Accepts a TypeResolutionVisitor. */
    public Type accept(TypeResolutionVisitor v) throws TypeResolutionException {
        return v.getTupleTyType(this);
    }

    /** Returns a formatted text string of this class. */
    public String asString(int indent, int increment) {
        StringBuffer sb = new StringBuffer();
        printSpace(indent, sb);
        sb.append("TupleTy\n");
        if (fields != null) {
            sb.append(fields.asString(indent + increment, increment));
        }
        return sb.toString();
    }

    public void prettyPrint() {
        Iterator<Ty> it = fields.iterator();
        System.out.print("(");
        if (it.hasNext()) it.next().prettyPrint();
        while (it.hasNext()) {
            System.out.print(", ");
            it.next().prettyPrint();
        }
        System.out.print(")");
    }

    public Ty copy() {
        Iterator<Ty> it = fields.iterator();
        List<Ty> newFields = new List<Ty>();
        while (it.hasNext()) {
            newFields.add(it.next().copy());
        }
        return new TupleTy(null, newFields);
    }
}
