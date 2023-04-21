package com.onehao.dynamicproxy;

import java.util.Date;

public class RealSubject implements Subject {

    @Override
    public void request() {
        System.out.println("From real subject!");
    }

    @Override
    public void requestWithParams(String name, Date date) {
        System.out.println("Hello: " + name + " on " + date);
    }
}
