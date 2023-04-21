package uk.icat3.exceptions;

import uk.icat3.util.ElementType;

/**
 *
 * @author cruzcruz
 */
public class NoElementTypeException extends ParameterSearchException {

    private static String msg = NoElementTypeException.class.getName();

    public NoElementTypeException(ElementType type) {
        super(msg + ": element type '" + type.name() + "' no supported");
    }
}
