package org.melati.util;

/**
  * Allows us to slow things down.
  */
public class Waiter {

    public String wait(Integer length) {
        try {
            java.lang.Thread.sleep(length.intValue());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
