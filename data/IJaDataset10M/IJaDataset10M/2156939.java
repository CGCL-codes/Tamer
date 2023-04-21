package net.cyan.util;

/**
 * <p>Title: 计数器</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author ccs
 * @version 1.0
 */
public class Increaser {

    int i = 0;

    public Increaser() {
    }

    public Increaser(int i) {
        this.i = i;
    }

    public void inc() {
        i++;
    }

    public void inc(int n) {
        i += n;
    }

    public int get() {
        return i;
    }
}
