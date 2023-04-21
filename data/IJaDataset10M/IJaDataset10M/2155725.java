package com.jeronimo.eko.core.beans;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

@Encodable(messageClassId = 7755, versionId = 1)
@SuppressWarnings(value = { "Bx", "Dm", "UwF", "HE", "USM" })
public class TestEmptyBean {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestEmptyBean) return true;
        return super.equals(obj);
    }
}
