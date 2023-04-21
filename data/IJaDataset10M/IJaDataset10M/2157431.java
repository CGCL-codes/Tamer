package com.sun.gluegen;

import java.util.*;
import com.sun.gluegen.cgram.types.*;

public class ReferencedStructs implements TypeVisitor {

    private Set results = new HashSet();

    public void clear() {
        results.clear();
    }

    public Iterator results() {
        return results.iterator();
    }

    public void visitType(Type t) {
        if (t.isPointer()) {
            PointerType p = t.asPointer();
            if (p.hasTypedefedName()) {
                CompoundType c = p.getTargetType().asCompound();
                if (c != null && c.getName() == null) {
                    results.add(p);
                }
            }
        } else if (t.isCompound()) {
            results.add(t);
        }
    }
}
