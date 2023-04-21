package net.sourceforge.coffea.tools;

import java.lang.reflect.InvocationTargetException;
import net.sourceforge.coffea.tools.MemberHandler;
import net.sourceforge.coffea.tools.PropertyHandler;
import net.sourceforge.coffea.tools.capacities.IAttributeHandling;
import net.sourceforge.coffea.tools.capacities.IClassHandling;
import net.sourceforge.coffea.tools.capacities.ITypeHandling;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.VisibilityKind;

/** Tool for working with an attribute */
@SuppressWarnings("restriction")
public class PropertyHandler extends MemberHandler<Property, FieldDeclaration, IField> implements IAttributeHandling {

    /**
	 * Java class simple name extraction from the corresponding UML element
	 * @param p
	 * UML element from which the name will be extracted
	 * @return Extracted simple name
	 */
    public static String codeSimpleNameExtraction(Property p) {
        String simpleName = null;
        if (p != null) {
            simpleName = p.getName();
        }
        return simpleName;
    }

    /**
	 * Construction
	 * @param p
	 * 	Value of {@link #container}
	 * @param stxNode
	 * 	Value of {@link #syntaxTreeNode}
	 */
    protected PropertyHandler(FieldDeclaration stxNode, IClassHandling<?, ?> p) {
        super(stxNode, p);
    }

    /**
	 * Construction
	 * @param p
	 * 	Value of {@link #container}
	 * @param jEl
	 * 	Value of {@link #javaElement}
	 */
    protected PropertyHandler(IField jEl, IClassHandling<?, ?> p) {
        super(jEl, p);
    }

    public String getSimpleName() {
        String simpleName = null;
        if (syntaxTreeNode != null) {
            String declaredName = syntaxTreeNode.fragments().get(0).toString();
            if (declaredName.lastIndexOf('=') != -1) declaredName = declaredName.substring(0, declaredName.indexOf('='));
            simpleName = declaredName;
        } else if (javaElement != null) {
            simpleName = javaElement.getElementName();
        }
        return simpleName;
    }

    public IClassHandling<?, ?> getContainerHandler() {
        return (IClassHandling<?, ?>) super.getContainerHandler();
    }

    public ITypeHandling<?, ?> resolveTypeHandler() {
        String typeName = null;
        ITypeHandling<?, ?> tHandler = null;
        if (syntaxTreeNode != null) {
            if (syntaxTreeNode.getType() instanceof SimpleType) {
                SimpleType supplierType = (SimpleType) syntaxTreeNode.getType();
                ITypeBinding binding = supplierType.resolveBinding();
                if (binding != null) {
                    typeName = binding.getQualifiedName();
                }
            }
        } else if (javaElement != null) {
            try {
                IImportDeclaration[] imports = null;
                if (container instanceof IClassHandling<?, ?>) {
                    IClassHandling<?, ?> cl = (IClassHandling<?, ?>) container;
                    imports = cl.getJavaElement().getCompilationUnit().getImports();
                }
                typeName = javaElement.getTypeSignature();
                if (typeName != null) {
                    if (typeName.startsWith("Q")) {
                        typeName = typeName.substring(1, typeName.length() - 1);
                        IImportDeclaration imp = null;
                        if (imports != null) {
                            for (int i = 0; i < imports.length; i++) {
                                imp = imports[i];
                                if (imp.getElementName().endsWith(typeName)) {
                                    typeName = imp.getElementName();
                                    break;
                                }
                            }
                            typeName = "java.lang." + typeName;
                        }
                    }
                }
            } catch (JavaModelException e) {
                e.printStackTrace();
            }
        }
        if (typeName != null) {
            tHandler = getModelHandler().resolveTypeHandler(typeName);
        }
        return tHandler;
    }

    public void setUpUMLModelElement() {
        if (umlModelElement == null) {
            if (getContainerHandler() != null) {
                ITypeHandling<?, ?> tHandler = resolveTypeHandler();
                Type t = null;
                if (tHandler != null) {
                    t = tHandler.getUMLElement();
                }
                umlModelElement = getContainerHandler().getUMLElement().createOwnedAttribute(getSimpleName(), t);
                getUMLElement().setVisibility(getVisibility());
            }
        }
    }

    public void resourceSetChanged(ResourceSetChangeEvent evt) {
    }

    @Override
    public void acceptModelChangeNotification(Notification nt) {
        if ((nt != null) && (nt.getNotifier() != null)) {
            Object feature = nt.getFeature();
            if (feature instanceof EAttribute) {
                EAttribute attr = (EAttribute) feature;
                String attrName = attr.getName();
                Object newValue = nt.getNewValue();
                if (attrName != null) {
                    if (attrName.equals("name")) {
                        if (newValue instanceof String) {
                            String newName = (String) newValue;
                            rename(newName);
                        }
                    } else if (attrName.equals("visibility")) {
                        if (newValue != null) {
                            if (newValue.equals(VisibilityKind.PRIVATE)) {
                            }
                        }
                    }
                }
            }
        }
    }

    public void rename(String nm) {
        RenamingRunnable renameRunnable = new RenamingRunnable(nm);
        try {
            PlatformUI.getWorkbench().getProgressService().run(true, true, renameRunnable);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Renaming runnable */
    public class RenamingRunnable implements IRunnableWithProgress {

        /** New simple name */
        protected String newName;

        /**
		 * Renaming runnable construction
		 * @param nm
		 * Value of {@link #newName}
		 */
        public RenamingRunnable(String nm) {
            newName = nm;
        }

        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            if ((javaElement != null) && (newName != null)) {
                RenameFieldProcessor p;
                try {
                    p = new RenameFieldProcessor(javaElement);
                    p.setNewElementName(newName);
                    Refactoring r = new RenameRefactoring(p);
                    PerformRefactoringOperation op = new PerformRefactoringOperation(r, CheckConditionsOperation.FINAL_CONDITIONS);
                    op.run(monitor);
                    IJavaElement parent = javaElement.getParent();
                    if (parent instanceof IType) {
                        IType t = (IType) parent;
                        IField[] fields = t.getFields();
                        if (fields != null) {
                            IField f = null;
                            for (int i = 0; i < fields.length; i++) {
                                f = fields[i];
                                if (f != null) {
                                    String n = f.getElementName();
                                    if ((n != null) && (n.equals(newName))) {
                                        javaElement = f;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
