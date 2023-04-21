package com.googlecode.javatips4u.effectivejava.singleton;

public class StaticFinalFieldSingleton {

    public static final StaticFinalFieldSingleton INSTANCE = new StaticFinalFieldSingleton();

    private StaticFinalFieldSingleton() {
    }
}
