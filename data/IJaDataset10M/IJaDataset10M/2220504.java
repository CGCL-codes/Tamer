package com.ibm.wala.ipa.callgraph.propagation;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.callgraph.CGNode;

/**
 * An instance key which represents a unique set for ALL allocation sites of a
 * given type in a CGNode
 */
public class SmushedAllocationSiteKey extends AbstractAllocationSiteKey {

    public SmushedAllocationSiteKey(CGNode node, IClass type) {
        super(node, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SmushedAllocationSiteKey) {
            SmushedAllocationSiteKey other = (SmushedAllocationSiteKey) obj;
            return getNode().equals(other.getNode()) && getConcreteType().equals(other.getConcreteType());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getNode().hashCode() * 8293 + getConcreteType().hashCode();
    }

    @Override
    public String toString() {
        return "SMUSHED " + getNode() + " : " + getConcreteType();
    }
}
