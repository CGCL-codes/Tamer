package de.beas.explicanto.template;

/**
 * TemplateException is rised by any error related to the CSDE template
 * 
 * @author marius.staicu
 * @version 1.0
 *  
 */
public class TemplateException extends Exception {

    /**
	 * Default constructor
	 */
    public TemplateException() {
        super();
    }

    /**
	 * @param arg0
	 */
    public TemplateException(String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public TemplateException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
	 * @param arg0
	 */
    public TemplateException(Throwable arg0) {
        super(arg0);
    }
}
