package com.manning.aip.proguard;

public class Bomb {

    public void explode() {
        throw new RuntimeException("Boom!");
    }
}
