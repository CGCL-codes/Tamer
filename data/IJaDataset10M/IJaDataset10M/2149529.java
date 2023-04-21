package jpdl.accessplan.cache;

import java.util.EnumSet;
import java.util.HashMap;
import jpdl.joinpoints.Access;
import jpdl.joinpoints.JoinPoint;
import jpdl.joinpoints.TypeJoinPoint;
import jpdl.joinpoints.references.TypeReference;
import jpdl.types.TypePrimitive;
import jpdl.types.UnaryType;
import junit.framework.Assert;

public class TypeCache implements AccessCache<TypeReference, TypeJoinPoint> {

    private HashMap<TypeReference, TypeJoinPoint> typeMap = new HashMap<TypeReference, TypeJoinPoint>();

    public void cache(JoinPoint jp) {
        if (jp instanceof TypeJoinPoint) {
            TypeJoinPoint type = (TypeJoinPoint) jp;
            typeMap.put(type.getReference(), type);
        }
    }

    public UnaryType getAcceptedType() {
        return new UnaryType(TypePrimitive.TYPE);
    }

    public TypeJoinPoint get(TypeReference key) {
        return typeMap.get(key);
    }

    public boolean contains(TypeReference key) {
        return typeMap.containsKey(key);
    }
}
