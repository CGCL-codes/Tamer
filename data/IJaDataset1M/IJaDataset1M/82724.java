package vidis.data.var;

import java.util.Set;
import vidis.data.var.vars.AVariable;

/**
 * This interface defines all methods used for handling our variable system
 * 
 * @author Dominik
 * 
 */
public interface IVariableContainer extends IVariableChangeProducer {

    /**
	 * get a variable usind its unique string identifier
	 * @param id the id to search for
	 * @return the variable associated with id or null if it does not exist
	 */
    public AVariable getVariableById(String id);

    /**
	 * get all variable ids defined in this variable container
	 * @return a set of variable unique string identifiers
	 */
    public Set<String> getVariableIds();

    /**
	 * register a new variable
	 * @param var the variable to register
	 */
    public void registerVariable(AVariable var);

    /**
	 * check if this variable container has defined a variable with
	 * the requested id
	 * @param id check for
	 * @return true or false
	 */
    public boolean hasVariable(String id);
}
