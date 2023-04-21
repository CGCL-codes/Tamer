package org.joogie.boogie.expressions;

import org.joogie.boogie.BoogieHelpers;
import org.joogie.boogie.types.BoogieType;
import org.joogie.boogie.types.BoogieTypeFactory;
import soot.Local;

/**
 * @author schaef
 *
 */
public class Variable extends Expression {

    private String varName = "TestVar";

    private BoogieType varType;

    private boolean constUnique = false;

    public boolean isBound = false;

    public Variable(String name, BoogieType t) {
        varName = name;
        varType = t;
    }

    public Variable(String name, BoogieType t, boolean constunique) {
        varName = name;
        varType = t;
        constUnique = constunique;
    }

    public String getName() {
        return varName;
    }

    public boolean isConstUnique() {
        return constUnique;
    }

    @Override
    public Variable clone() {
        return this;
    }

    public BoogieType getType() {
        return varType;
    }

    public Variable(Local local) {
        varName = BoogieHelpers.getQualifiedName(local);
        varType = BoogieTypeFactory.lookupBoogieType(local.getType());
    }

    @Override
    public String toBoogie() {
        return varName;
    }
}
