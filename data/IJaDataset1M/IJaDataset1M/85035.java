package org.progeeks.meta.annotate;

/**
 *  This exception is thrown by an annotation map when it
 *  doesn't support a certain type of key.  This would occur
 *  when attempting to store property specific annotations for
 *  a type that doesn't support that or when attempting to store
 *  generic key-based meta-class annotations for types that don't
 *  support that.  Some AnnotationMaps are algorithmic in nature
 *  and will only support certain kinds of access since they will
 *  be using the key values in special ways.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public class UnsupportedKeyException extends RuntimeException {

    static final long serialVersionUID = 1;

    public UnsupportedKeyException() {
    }

    public UnsupportedKeyException(String message) {
        super(message);
    }
}
