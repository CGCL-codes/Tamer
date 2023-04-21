package org.strophe.sph;

import java.util.Vector;

final class SphTypeNative extends SphTypeCode {

    String className;

    int index;

    public SphTypeNative(String sourceFile, int sourceLine, String className, int index) {
        super(sourceFile, sourceLine);
        this.className = className;
        this.index = index;
    }

    public final void sphCode_setCodeScope(SphCodeScope parentCodeScope) {
    }

    public void sphCode_validate() {
    }

    public void sphCode_link() {
    }

    public boolean sphType_isPrimary() {
        return true;
    }

    public SphType sphType_call(SphDefinition[] tipDefinitions, SphType[] tipTypes) {
        return this;
    }

    public SphClass sphType_getClass() {
        return null;
    }

    public String sphType_toString() {
        return "standard.lang." + className;
    }

    public void sphType_getTipsForInstance(SphType atypInstance, Vector avctTipDefinitions, Vector avctTipTypes) {
    }
}
