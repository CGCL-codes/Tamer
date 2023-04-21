package net.sf.kerner.commons.collection;

import java.util.Collection;
import net.sf.kerner.commons.Modifier;

/**
 * 
 * TODO description
 * 
 * <p>
 * <b>Example:</b><br>
 *
 * </p>
 * <p>
 * <pre>
 * TODO example
 * </pre>
 * </p>
 *
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version Dec 6, 2010
 *
 * @param <T>
 * @param <V>
 * @deprecated
 */
public interface CollectionModifier<T, V extends T> extends Modifier<Collection<? extends T>, Collection<? extends V>> {
}
