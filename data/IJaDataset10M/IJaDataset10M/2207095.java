package org.cubictest.model;

/**
 * @author skyttere
 */
public class CommonTransition extends Transition {

    /**
	 * @param start
	 * @param end
	 */
    public CommonTransition(Common start, Page end) {
        super(start, end);
    }

    public void connect() {
        ((Common) getStart()).addOutTransition(this);
        ((Page) getEnd()).addCommonTransition(this);
    }

    public void disconnect() {
        getStart().removeOutTransition(this);
        ((Page) getEnd()).removeCommonTransition(this);
    }
}
