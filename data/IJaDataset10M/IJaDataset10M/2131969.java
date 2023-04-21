package dr.inference.model;

/**
 * @author Alexei Drummond
 * @version $Id: ParameterListener.java,v 1.2 2005/05/24 20:26:00 rambaut Exp $
 */
public interface VariableListener {

    void variableChangedEvent(Variable variable, int index, Variable.ChangeType type);
}
