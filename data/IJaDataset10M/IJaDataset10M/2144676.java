package org.easybi.chart.impl;

/**
 * �趨�ĸ��ߵĴ�С
 * @author steve
 *
 */
public class InsetImpl {

    int top = 0;

    int left = 0;

    int bottom = 0;

    int right = 0;

    public InsetImpl(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public int getLeft() {
        return this.left;
    }

    public int getRight() {
        return this.right;
    }

    public int getTop() {
        return this.top;
    }

    public int getBottom() {
        return this.bottom;
    }
}
