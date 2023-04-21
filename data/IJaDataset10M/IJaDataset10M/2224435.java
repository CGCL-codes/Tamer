package com.schinzer.fin.med;

public abstract class RefundStrategy {

    public abstract void init();

    public abstract float process(float amount);
}
