package org.scilab.forge.jlatexmath;

/**
 * Signals that an unknown symbol type constant or a symbol of the wrong type was used.
 * 
 * @author Kurt Vermeulen
 */
public class InvalidSymbolTypeException extends JMathTeXException {

    protected InvalidSymbolTypeException(String msg) {
        super(msg);
    }
}
