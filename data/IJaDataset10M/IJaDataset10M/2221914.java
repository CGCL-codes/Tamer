package org.sodeja.sil.runtime;

public class SILClass extends SILDefaultObject {

    protected SILClass() {
        super(5, 0);
    }

    public SILObject getSuperclass() {
        return namedVars[0];
    }

    public SILObject getInstanceSpecification() {
        return namedVars[1];
    }

    public SILObject getMethodDictionary() {
        return namedVars[2];
    }

    public SILObject getSubclasses() {
        return namedVars[3];
    }

    public SILObject getInstances() {
        return namedVars[4];
    }

    protected void setSuperclass(SILObject superclass) {
        namedVars[0] = superclass;
    }

    protected void setInstanceSpecification(SILObject instanceSpecification) {
        namedVars[1] = instanceSpecification;
    }

    protected void setMethodDictionary(SILObject methodDictionary) {
        namedVars[2] = methodDictionary;
    }

    protected void setSubclasses(SILObject subclasses) {
        namedVars[3] = subclasses;
    }

    protected void setInstances(SILObject instances) {
        namedVars[4] = instances;
    }
}
