package com.germinus.xpression.cms.jcr.conversions;

public abstract class AbstractJCRTypeConverter implements JCRTypeConverter {

    public boolean isCompatible(Object sourceValue) {
        return true;
    }
}
