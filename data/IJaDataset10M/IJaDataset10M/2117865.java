package com.googlecode.junit.ext.preconditions;

import com.googlecode.junit.ext.Precondition;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class FailedToTearDown implements Precondition {

    private Map context;

    @Autowired
    SampleService service;

    public FailedToTearDown(Object context) {
        this.context = (Map) context;
    }

    public void setup() {
        this.context.put("FailedToTearDown#setup", true);
    }

    public void teardown() {
        this.context.put("FailedToTearDown#teardown", true);
        throw new RuntimeException("FailedToTearDown");
    }
}
