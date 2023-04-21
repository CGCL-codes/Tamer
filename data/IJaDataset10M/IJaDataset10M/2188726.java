package jaxil.impl;

import java.io.Serializable;

/**
 * Link is an element which contains 2 addresses and one status
 * 
 * @since 23/10/2008
 * @version 1.0
 * @author gael
 */
public class Link implements Serializable {

    private static final long serialVersionUID = 6420565898113478669L;

    private String x;

    private String y;

    private int status;

    public Link(String x, String y, int status) {
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
