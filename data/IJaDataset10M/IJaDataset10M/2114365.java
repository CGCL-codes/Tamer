package pl.ericpol.flying.logic;

import java.io.Serializable;

public class LessThanZeroException extends java.lang.Exception implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public LessThanZeroException(java.lang.String text) {
        System.out.println(text);
    }

    public LessThanZeroException() {
    }
}
