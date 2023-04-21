package net.da.processor.a.io;

import net.da.processor.IOResponse;

public class A01Response implements IOResponse {

    private String field01;

    private String field03;

    public String getField01() {
        return field01;
    }

    public void setField01(String field01) {
        this.field01 = field01;
    }

    public String getField03() {
        return field03;
    }

    public void setField03(String field03) {
        this.field03 = field03;
    }
}
