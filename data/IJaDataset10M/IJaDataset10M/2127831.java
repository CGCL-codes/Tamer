package no.uio.ifi.kjetilos.javatraits.visitors;

import java.util.ArrayList;
import java.util.Collection;
import no.uio.ifi.kjetilos.javatraits.model.RequiredMethod;
import no.uio.ifi.kjetilos.javatraits.parser.ASTRequiredMethodDeclaration;

/**
 * Finds and visits all the required methods inside a trait. 
 * 
 * @author Kjetil �ster�s
 *
 */
public class RequiredMethodsVisitor extends EmptyVisitor {

    Collection<RequiredMethod> requiredMethods = new ArrayList<RequiredMethod>();

    /**
	 * Only visits the required methods.
	 */
    @Override
    public void visit(ASTRequiredMethodDeclaration methodDeclaration) {
        RequiredMethod requiredMethod = new RequiredMethod();
        requiredMethod.setName(methodDeclaration.getName());
        requiredMethod.setMethodDeclaration(methodDeclaration);
        requiredMethods.add(requiredMethod);
    }

    /**
	 * Gets the collection of required methods.
	 * 
	 * @return a collection of requirements
	 */
    public Collection<RequiredMethod> getRequiredMethods() {
        return requiredMethods;
    }
}
