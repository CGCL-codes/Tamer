package org.ccpo.common.api;

import org.apache.commons.lang.RandomStringUtils;
import org.ccpo.common.api.EvoConstants;

public abstract class Identifiable {

    private String uid = RandomStringUtils.randomAlphanumeric(EvoConstants.UID_LENGTH);

    public String getUID() {
        return uid;
    }
}
