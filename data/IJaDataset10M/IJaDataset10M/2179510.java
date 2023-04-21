package toxTree.core;

import java.awt.Component;
import toxTree.exceptions.DecisionMethodException;

public interface IDecisionMethodEditor extends IToxTreeEditor {

    /**
	 * Each method {@link IDecisionMethod} should provides an editor, which is a class, 
	 * implementing this interface.
	 * The idea is to provide an user interface for visualization and modification of
	 * various method settings.
	 * @author Nina Jeliazkova nina@acad.bg
	 * <b>Modified</b> 2005-11-11
	 */
    public void setMethod(IDecisionMethod method);

    public IDecisionMethod getMethod();

    public IDecisionMethod edit(Component owner, IDecisionMethod method) throws DecisionMethodException;
}
