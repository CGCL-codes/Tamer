package com.sodad.weka.gui.graphvisualizer;

/**
 * This is the Exception thrown by BIFParser, if there
 * was an error in parsing the XMLBIF string or input
 * stream.
 *
 * @author Ashraf M. Kibriya (amk14@cs.waikato.ac.nz)
 * @version $Revision: 1.5 $ - 24 Apr 2003 - Initial version (Ashraf M. Kibriya)
 */
public class BIFFormatException extends Exception {

    /** for serialization */
    private static final long serialVersionUID = -4102518086411708140L;

    public BIFFormatException(String s) {
        super(s);
    }
}
