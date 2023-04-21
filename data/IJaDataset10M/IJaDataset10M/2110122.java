package com.sun.jdi;

/**
 * The type of all primitive double values accessed in
 * the target VM. Calls to {@link Value#type} will return an
 * implementor of this interface.
 *
 * @see DoubleValue
 *
 * @author James McIlree
 * @since  1.3
 */
public interface DoubleType extends PrimitiveType {
}
