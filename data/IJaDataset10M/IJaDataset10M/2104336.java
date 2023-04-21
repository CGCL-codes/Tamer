package coffea.tools.capacities;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.uml2.uml.Package;

/** Capacity of handling a {@link Package} */
public interface IPackageHandling extends IPackageableElementHandling, ITypesContainerHandling, IPackagesGroupHandling {

    public Package getUMLElement();

    /**
	 * Sets the element group handler
	 * @param gr
	 * Element group handler
	 */
    public void setGroupHandler(IPackagesGroupHandling gr);

    public IPackagesGroupHandling getContainerHandler();

    /** 
	 * Retrieves the package container from the package hierarchy ; 
	 * this aim to fetch the package hierarchy which has disappeared between 
	 * the source code directories and the code AST parsing.
	 * @see #getContainerHandler()
	 * @see #getSyntaxNode()
	 * @see ASTNode#getParent()
	 */
    public void retrieveContainerFromHierarchy();
}
