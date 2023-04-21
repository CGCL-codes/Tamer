package com.gtech.iarc.testcase.ischedule.support.task;

import com.gtech.iarc.ischedule.AbstractSpringBeanTask;

public class SimpleTestTask extends AbstractSpringBeanTask {

    public SimpleTestTask() {
    }

    @Override
    public String getProxyBeanName() {
        return "simpleTestScheduledWork";
    }
}
