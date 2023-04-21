package jpdl.pointcuts.predicates;

import jpdl.joinpoints.Access;
import jpdl.joinpoints.JoinPoint;
import jpdl.joinpoints.TypeJoinPoint;
import jpdl.pointcuts.PredicatePointCut;
import jpdl.types.TypePrimitive;
import jpdl.types.UnaryType;

public class InterfacePredicate extends PredicatePointCut {

    private static final UnaryType matchingType = new UnaryType(TypePrimitive.TYPE);

    public UnaryType getType() {
        return matchingType;
    }

    @Override
    public boolean matches(JoinPoint joinpoint) {
        if (!getType().contains(joinpoint.getTypePrim())) {
            return false;
        }
        if (joinpoint instanceof TypeJoinPoint) {
            return ((TypeJoinPoint) joinpoint).getAccess().contains(Access.INTERFACE);
        } else {
            return false;
        }
    }
}
