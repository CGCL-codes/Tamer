package net.sourceforge.coffea.tools;

import net.sourceforge.coffea.tools.capacities.IElementHandling;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.uml2.uml.Element;

/** 
 * Capacity of a code modification runnable with progress operating from an 
 * UML modeled objective
 * @param <U>
 * Type of the UML element to take as objective  
 * @param <R>
 * Type of the handler to use for the modification result
 */
public interface IUMLToCodeModificationRunnable<U extends Element, R extends IElementHandling> extends IRunnableWithProgress {

    /**
	 * Sets the modification objective
	 * @param newClass
	 * UML element modeling the code objective to get as result of the 
	 * runnable
	 */
    public abstract void setObjective(U newClass);

    /**
	 * Returns modification objective
	 * @return UML element modeling the code objective to get as result of the 
	 * runnable
	 */
    public abstract U getObjective();

    /**
	 * Returns a handler for the modification result
	 * @return Handler for the modification
	 */
    public abstract R getResult();
}
