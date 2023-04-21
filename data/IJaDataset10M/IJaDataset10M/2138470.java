package edu.clemson.cs.r2jt.mathtype;

import edu.clemson.cs.r2jt.absyn.Exp;
import edu.clemson.cs.r2jt.typereasoning.TypeGraph;

public class MTSetRestriction extends MTAbstract<MTSetRestriction> {

    private MTType myBaseType;

    private String mySetVar;

    private Exp myRestriction;

    public MTSetRestriction(TypeGraph g, MTType baseType, String setVar, Exp restriction) {
        super(g);
        myBaseType = baseType;
        mySetVar = setVar;
        myRestriction = restriction;
    }

    @Override
    public boolean valueEqual(MTSetRestriction t) {
        return false;
    }

    @Override
    public boolean isKnownToContainOnlyMTypes() {
        return myBaseType.isKnownToContainOnlyMTypes();
    }

    @Override
    public boolean membersKnownToContainOnlyMTypes() {
        return myBaseType.membersKnownToContainOnlyMTypes();
    }

    @Override
    public String toString() {
        return "{" + mySetVar + " : " + myBaseType.toString() + " | " + myRestriction.toString() + "}";
    }
}
