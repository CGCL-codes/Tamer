package org.amse.ak.schemebuilder.view;

class WidthInformation {

    private int myRightWidth = 0;

    private int myLeftWidth = 0;

    public WidthInformation() {
    }

    public WidthInformation(int rightWidth, int leftWidth) {
        myRightWidth = rightWidth;
        myLeftWidth = leftWidth;
    }

    public int getLeftWidth() {
        return myLeftWidth;
    }

    public void setLeftWidth(int leftWidth) {
        myLeftWidth = leftWidth;
    }

    public int getRightWidth() {
        return myRightWidth;
    }

    public void setRightWidth(int rightWidth) {
        myRightWidth = rightWidth;
    }
}
