package org.aspectj.runtime.reflect;

import java.lang.reflect.Modifier;
import org.aspectj.lang.reflect.UnlockSignature;

class UnlockSignatureImpl extends SignatureImpl implements UnlockSignature {

    private Class parameterType;

    UnlockSignatureImpl(Class c) {
        super(Modifier.STATIC, "unlock", c);
        parameterType = c;
    }

    UnlockSignatureImpl(String stringRep) {
        super(stringRep);
    }

    protected String createToString(StringMaker sm) {
        if (parameterType == null) parameterType = extractType(3);
        return "unlock(" + sm.makeTypeName(parameterType) + ")";
    }

    public Class getParameterType() {
        if (parameterType == null) parameterType = extractType(3);
        return parameterType;
    }
}
