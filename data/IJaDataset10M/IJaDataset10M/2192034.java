package com.yeep.study.struts2.interceptor.sample;

public class ExecuteFunction implements ExecuteFunctionInterface {

    public void execute() {
        System.out.println("execute something...");
    }

    public void stop() {
        System.out.println("stop something...");
    }
}
