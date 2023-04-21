package com.googlecode.junit.ext.preconditions;

import com.googlecode.junit.ext.Precondition;
import java.util.Map;

public class SuccessfullyRan implements Precondition {

    private Map obj;

    public SuccessfullyRan() {
    }

    public SuccessfullyRan(Object obj) {
        this.obj = (Map) obj;
    }

    public void setup() {
        obj.put("SuccessfullyRan#setup", true);
    }

    public void teardown() {
        obj.put("SuccessfullyRan#teardown", true);
    }
}
