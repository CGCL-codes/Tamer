package ru.yandex.strictweb.scriptjava.base;

@Native
public abstract class BrowserHistory {

    public int length;

    public abstract void back();

    public abstract void forward();

    public abstract void go(int delta);
}
