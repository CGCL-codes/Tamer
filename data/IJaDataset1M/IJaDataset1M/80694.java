package com.idna.trace.core.common;

import net.icdpublishing.dream.domain.DomainField;
import net.icdpublishing.dream.domain.DomainObject;

public class DomainObjectUtil {

    private DomainObjectUtil() {
    }

    public static boolean isMarriage(DomainObject domain) {
        if ((Boolean) domain.get(DomainField.IS_MARRIAGE)) return true;
        return false;
    }

    public static boolean isDeath(DomainObject domain) {
        if ((Boolean) domain.get(DomainField.IS_DEATH)) return true;
        return false;
    }

    public static boolean isBirth(DomainObject domain) {
        if ((Boolean) domain.get(DomainField.IS_BIRTH)) return true;
        return false;
    }

    public static boolean isPerson(DomainObject domain) {
        if ((Boolean) domain.get(DomainField.IS_PERSON)) return true;
        return false;
    }

    public static boolean isBusiness(DomainObject domain) {
        if ((Boolean) domain.get(DomainField.IS_BUSINESS)) return true;
        return false;
    }

    public static boolean isLandRegistry(DomainObject domain) {
        if ((Boolean) domain.get(DomainField.IS_LANDREGISTRY)) return true;
        return false;
    }
}
