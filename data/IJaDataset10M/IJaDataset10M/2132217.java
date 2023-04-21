package com.lts.swing.thread;

public abstract class SimpleActionThread extends ActionThread {

    protected abstract void action();

    @Override
    public void run() {
        action();
    }
}
