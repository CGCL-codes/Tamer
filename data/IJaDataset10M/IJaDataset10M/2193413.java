package org.progeeks.meta.annotate;

/**
 *  This exception is thrown by an annotation map when it
 *  doesn't support adding new elements.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public class ImmutableAnnotationMapException extends RuntimeException {

    static final long serialVersionUID = 1;

    public ImmutableAnnotationMapException() {
    }

    public ImmutableAnnotationMapException(String message) {
        super(message);
    }
}
