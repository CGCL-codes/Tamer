package edu.ucdavis.genomics.metabolomics.xdoclet.tag;

import xdoclet.XDocletTagSupport;

/**
 * @author wohlgemuth
 * provides a simple counter needed if you wanna get throw arrays
 * 
 * @xdoclet.taghandler   namespace="Counter"
 */
public class CounterTagHandler extends XDocletTagSupport {

    int value = 0;

    /**
	 * increse  by one
	 * @return
	 */
    public void increase() {
        value++;
    }

    /**
	 * decrese by one
	 * @return
	 */
    public void decrease() {
        value--;
    }

    /**
	 * returns the value
	 * @return
	 */
    public String current() {
        return String.valueOf(value);
    }

    /**
	 * reset the counter to zero
	 *
	 */
    public void reset() {
        value = 0;
    }

    /**
	 * 
	 */
    public CounterTagHandler() {
        super();
    }
}
