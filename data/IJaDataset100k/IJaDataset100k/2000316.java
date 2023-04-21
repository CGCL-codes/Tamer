package com.intel.util.sets;

/**
 * @author Dmitry Ivanov
 * @version $Id: Triple.java,v 1.1 2006/09/14 08:53:58 daivanov Exp $
 */
public class Triple<T1, T2, T3> {

    private T1 m1;

    private T2 m2;

    private T3 m3;

    public Triple(T1 m1, T2 m2, T3 m3) {
        super();
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
    }

    public T1 getM1() {
        return m1;
    }

    public void setM1(T1 m1) {
        this.m1 = m1;
    }

    public T2 getM2() {
        return m2;
    }

    public void setM2(T2 m2) {
        this.m2 = m2;
    }

    public T3 getM3() {
        return m3;
    }

    public void setM3(T3 m3) {
        this.m3 = m3;
    }
}
