package org.aspectbench.runtime.reflect;

import org.aspectj.lang.reflect.MemberSignature;

public abstract class MemberSignatureImpl extends SignatureImpl implements MemberSignature {

    MemberSignatureImpl(int modifiers, String name, Class declaringType) {
        super(modifiers, name, declaringType);
    }

    public MemberSignatureImpl(String stringRep) {
        super(stringRep);
    }
}
