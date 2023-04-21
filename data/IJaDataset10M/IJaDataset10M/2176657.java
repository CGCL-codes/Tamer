package net.sf.kerner.commons.collection;

/**
 *
 * TODO description
 * 
 * <p>
 * <b>Example:</b>
 * <pre>
 * TODO example
 * </pre>
 * </p>
 *
 * @autor <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version Sep 15, 2010
 *
 */
public interface VisitableModifingCollection<C, E> {

    C modifyAllElements(ModifingVisitor<E> visitor);
}
