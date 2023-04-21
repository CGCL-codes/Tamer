package net.sourceforge.coffea.uml2.model;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Service for an element which can belong to a group
 * @param <S>
 * Type of the element handled by the service as AST node
 * @param <J>
 * Type of the element handled by the service as Java element
 * @see IGroupService
 */
public interface IGroupableElementService<S extends ASTNode, J extends IJavaElement> extends IContainableElementService<S, J> {

    /**
	 * Returns the service for the group to which the handled element belongs
	 * @return Service for the group to which the handled element belongs
	 */
    public IGroupService getContainerService();
}
