package com.metanology.mde.metaprogram;

/**
 * @author wwang
 */
public class CompileFailedException extends Exception {

    /**
	 * 
	 */
    public CompileFailedException() {
        super();
    }

    /**
	 * @param arg0
	 */
    public CompileFailedException(String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 */
    public CompileFailedException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public CompileFailedException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
