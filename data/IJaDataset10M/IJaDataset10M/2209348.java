package net.sourceforge.coffea.tools.capacities;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Capacity of handling an element groupable in a handled group 
 * ({@link IGroupHandling})
 * @param <S>
 * Handled syntax node type
 * @param <J>
 * Handled syntax node Java element
 */
public interface IGroupableElementHandling<S extends ASTNode, J extends IJavaElement> extends IContainableElementHandling<S, J> {

    public IGroupHandling getContainerHandler();
}
