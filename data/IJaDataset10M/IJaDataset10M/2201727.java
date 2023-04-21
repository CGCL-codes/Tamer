package com.arcucomp.util;

/**
 * Demo class for illustrating jcoverage and its usage
 */
public class Hello {

    private String hello;

    private int myInt;

    private String prop;

    private ExceptionThrower thrower;

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getHello() {
        return hello;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }

    public int getMyInt() {
        return myInt;
    }

    public int addInt(int newInt) {
        if (newInt > 0) {
            return myInt + newInt;
        } else {
            return myInt;
        }
    }

    public void setProperty(String prop) {
        if (prop == "mystring" || prop == "someotherstring") {
            this.prop = prop;
        } else {
            this.prop = "mystring" + prop;
        }
    }

    public String getProperty() {
        return this.prop;
    }

    public void setExceptionThrower(String prop) {
        thrower = new ExceptionThrower();
        try {
            thrower.setSomeOtherProp(prop);
        } catch (Exception e) {
        }
    }

    public String getExceptionThrowerProperty() {
        return thrower.getSomeOtherProp();
    }
}
