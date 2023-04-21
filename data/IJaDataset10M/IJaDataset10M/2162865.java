package com.dustedpixels.asm.test;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class InnerClass {

    private int sum;

    public int add(int a, int b) {
        sum = 0;
        inc(a);
        inc(b);
        return sum;
    }

    private void inc(int a) {
        sum += a;
    }
}
