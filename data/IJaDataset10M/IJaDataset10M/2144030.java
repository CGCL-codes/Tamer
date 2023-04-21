package UMLAA.impl;

import UMLAA.Dependency;
import UMLAA.UMLAAPackage;
import org.eclipse.emf.ecore.EClass;

public class DependencyImpl extends RelationshipImpl implements Dependency {

    protected DependencyImpl() {
        super();
    }

    @Override
    protected EClass eStaticClass() {
        return UMLAAPackage.Literals.DEPENDENCY;
    }
}
