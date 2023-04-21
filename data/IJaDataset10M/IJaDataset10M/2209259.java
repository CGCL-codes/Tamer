package siouxsie.mvc.impl;

/**
 * Simple action only for screen chaining.
 * The <code>next</code> property is returned
 * by the <code>execute</code> method.
 * @author Arnaud Cogoluegnes
 * @version $Id: ForwardAction.java 100 2007-10-07 19:53:32Z acogo $
 */
public class ForwardAction {

    /**
	 * Default method.
	 * @return the next property
	 */
    public String execute() {
        return next;
    }

    /** where to go */
    private String next;

    /**
	 * 
	 * @return
	 */
    public String getNext() {
        return next;
    }

    /**
	 * 
	 * @param next
	 */
    public void setNext(String next) {
        this.next = next;
    }
}
