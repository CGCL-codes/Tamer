package com.justin.workaround;

public class ClassWithPrivateVariable {

    @SuppressWarnings("unused")
    private int counter;

    public void add() {
        counter++;
    }
}
