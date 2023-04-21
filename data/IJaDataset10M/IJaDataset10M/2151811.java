package com.ibm.wala.ipa.callgraph;

import com.ibm.wala.analysis.typeInference.PointType;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.util.debug.Assertions;

/**
 * misc utilities for dealing with contexts
 */
public class ContextUtil {

    /**
   * @param c a context
   * @return If this is an object-sensitive context that identifies a unique class for the receiver object, then return the unique
   *         class. Else, return null.
   * @throws IllegalArgumentException if c is null
   */
    public static IClass getConcreteClassFromContext(Context c) {
        if (c == null) {
            throw new IllegalArgumentException("c is null");
        }
        ContextItem item = c.get(ContextKey.RECEIVER);
        if (item == null) {
            return null;
        } else {
            if (item instanceof PointType) {
                return ((PointType) item).getIClass();
            } else if (item instanceof InstanceKey) {
                return ((InstanceKey) item).getConcreteType();
            } else {
                Assertions.UNREACHABLE("Unexpected: " + item.getClass());
                return null;
            }
        }
    }
}
