package util;

import java.io.Serializable;

public class MySecondClass implements Serializable {

    private static final long serialVersionUID = 1L;

    String s;

    public MySecondClass(String s) {
        this.s = s;
    }

    public String toString() {
        return "s=" + s;
    }
}
