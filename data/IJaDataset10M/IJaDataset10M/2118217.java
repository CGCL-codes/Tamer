package com.onehao.annotation;

public class MyTargetTest {

    @MyTarget("hello")
    public void dosomething() {
        System.out.println("hello world");
    }
}
