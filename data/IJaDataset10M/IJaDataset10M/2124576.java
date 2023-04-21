package com.codemobiles.droidslator;

/**
 * This class is responsible for encapsulating information and object bound at a region
 * @author jaboho
 *
 */
public class RegionSpec {

    private String ID;

    private int X = 0;

    private int Y = 0;

    private int beginX = 0;

    private int beginY = 0;

    private int width = 0;

    private int height = 0;

    private Object boundObject;

    public RegionSpec(int X, int Y, int beginX, int beginY, int width, int height) {
        this.X = X;
        this.Y = Y;
        this.beginX = beginX;
        this.beginY = beginY;
        this.width = width;
        this.height = height;
    }

    public RegionSpec(int X, int Y, int width, int height) {
        this.X = X;
        this.Y = Y;
        this.beginX = X;
        this.beginY = Y;
        this.width = width;
        this.height = height;
    }

    public boolean isCurrentBoundObject(Object obj) {
        return obj.equals(boundObject);
    }

    public Object getBoundObject() {
        return boundObject;
    }

    public void setBoundObject(Object boundObject) {
        this.boundObject = boundObject;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iamgeID) {
        this.ID = iamgeID;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getBeginX() {
        return beginX;
    }

    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }
}
