package org.moonshot.cvm;

public class CVMUnknown implements I_ChattyClass, I_ChattyCompatible {

    private final Object obj;

    public CVMUnknown(Object aObj) {
        obj = aObj;
    }

    public Object callInstance(ChattyVM aVM, Object aInstance, String aMethod, ChattyStack aStack, int nParams) throws CVMRuntimeException, CVMBlockReturnException {
        if (aMethod == "toString") {
            if (aInstance instanceof CVMUnknown) return ((CVMUnknown) aInstance).obj.toString(); else return "CVMUnknown(" + obj.hashCode() + ")";
        }
        return getSuperclass(aVM).callInstance(aVM, aInstance, aMethod, aStack, nParams);
    }

    public I_ChattyClass getSuperclass(ChattyVM aVM) {
        return aVM.classFor(ChattyBasic.class);
    }

    public I_ChattyClass identifySelfClass(ChattyVM aVM) {
        return this;
    }
}
