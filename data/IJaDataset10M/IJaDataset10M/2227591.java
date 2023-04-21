package org.gwtoolbox.commons.generator.rebind;

/**
 * @author Uri Boness
 */
public interface Criteria<T> {

    boolean check(T t);
}
