package org.specrunner.util.composite;

import java.util.List;

/**
 * Basic composite interface.
 * 
 * @author Thiago Santos.
 * 
 * @param <P>
 *            The parent type.
 * @param <T>
 *            The children type.
 */
public interface IComposite<P extends IComposite<P, T>, T> {

    boolean isEmpty();

    List<T> getChildren();

    P add(T child);
}
