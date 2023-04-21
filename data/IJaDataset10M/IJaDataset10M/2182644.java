package net.sourceforge.coffea.tools;

import net.sourceforge.coffea.tools.MemberHandler;
import net.sourceforge.coffea.tools.OperationHandler;
import net.sourceforge.coffea.tools.capacities.IMethodHandling;
import net.sourceforge.coffea.tools.capacities.ITypeHandling;
import net.sourceforge.coffea.tools.capacities.ITypesOwnerContainableHandling;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Type;

/** Tool for working with a method */
public class OperationHandler extends MemberHandler<Operation, MethodDeclaration, IMethod> implements IMethodHandling {

    /**
	 * Operation handler construction
	 * @param p
	 * Value of {@link #container}, <code>null</code> if 
	 * {@link #syntaxTreeNode} is a root
	 * @param stxNode
	 * Value of {@link #syntaxTreeNode}
	 */
    protected OperationHandler(MethodDeclaration stxNode, ITypeHandling<?, ?> p) {
        super(stxNode, p);
    }

    /**
	 * Operation handler construction given an existing <em>UML</em> element
	 * @param stxNode
	 * Value of {@link #syntaxTreeNode}
	 * @param own
	 * Value of {@link #container}
	 * @param ume
	 * Value of {@link #umlModelElement}
	 */
    protected OperationHandler(MethodDeclaration stxNode, ITypesOwnerContainableHandling own, Operation ume) {
        super(stxNode, own, ume);
    }

    /**
	 * Operation handler construction
	 * @param jEl
	 * Value of {@link #javaElement}
	 * @param p
	 * Value of {@link #container}, <code>null</code> if {@link #javaElement} 
	 * is a root
	 */
    protected OperationHandler(IMethod jEl, ITypeHandling<?, ?> p) {
        super(jEl, p);
    }

    /**
	 * Operation handler construction given an existing <em>UML</em> element
	 * @param jEl
	 * Value of {@link #javaElement}
	 * @param own
	 * Value of {@link #container}
	 * @param ume
	 * Value of {@link #umlModelElement}
	 */
    protected OperationHandler(IMethod jEl, ITypesOwnerContainableHandling own, Operation ume) {
        super(jEl, own, ume);
    }

    @Override
    public ITypeHandling<?, ?> getContainerHandler() {
        return (ITypeHandling<?, ?>) container;
    }

    public ITypeHandling<?, ?> resolveReturnTypeHandler() {
        ITypeHandling<?, ?> rType = null;
        String typeName = null;
        if (syntaxTreeNode != null) {
            if (syntaxTreeNode.getReturnType2() instanceof SimpleType) {
                SimpleType supplierType = (SimpleType) syntaxTreeNode.getReturnType2();
                ITypeBinding binding = supplierType.resolveBinding();
                if (binding != null) {
                    typeName = binding.getQualifiedName();
                }
            }
        } else if (javaElement != null) {
            try {
                if (javaElement.getReturnType() != null) {
                    String simpleName = javaElement.getReturnType();
                    if (simpleName != null) {
                        if (simpleName.indexOf('<') >= 0) {
                            simpleName = simpleName.substring(0, simpleName.indexOf('<'));
                        }
                        ITypeHandling<?, ?> cont = getContainerHandler();
                        String[][] namesParts = cont.getJavaElement().resolveType(simpleName);
                        String name = cont.nameReconstruction(namesParts);
                        if (name != null) {
                            typeName = name;
                        }
                    }
                }
            } catch (JavaModelException e) {
                e.printStackTrace();
            }
        }
        rType = getModelHandler().resolveTypeHandler(typeName);
        return rType;
    }

    public void setUpUMLModelElement() {
        if (umlModelElement == null) {
            if ((getContainerHandler() != null) && (getContainerHandler() instanceof InterfaceHandler)) {
                InterfaceHandler clParent = (InterfaceHandler) getContainerHandler();
                ITypeHandling<?, ?> rType = resolveReturnTypeHandler();
                Type rTypeElement = null;
                if (rType != null) {
                    rTypeElement = rType.getUMLElement();
                }
                umlModelElement = clParent.getUMLElement().createOwnedOperation(getSimpleName(), null, null, rTypeElement);
                umlModelElement.setVisibility(getVisibility());
            }
        }
    }

    public String getSimpleName() {
        String name = null;
        if ((syntaxTreeNode != null) && (syntaxTreeNode.getName() != null)) {
            name = syntaxTreeNode.getName().toString();
        } else if (javaElement != null) {
            name = javaElement.getElementName();
        }
        return name;
    }

    public String toString() {
        String toString = null;
        if ((syntaxTreeNode != null) && (syntaxTreeNode.getName() != null)) {
            toString = syntaxTreeNode.getName().getFullyQualifiedName();
        } else {
            toString = super.getFullName();
        }
        return toString;
    }

    public void rename(String nm) {
        try {
            javaElement.rename(nm, false, new NullProgressMonitor());
        } catch (JavaModelException e) {
            e.printStackTrace();
        }
    }
}
