package calculator.interpreter;

import net.sf.etl.parsers.SourceLocation;

/**
 * The evaluation environment
 */
public class Environment {

    /** the previous environment or null for top one */
    private final Environment previous;

    /** the variable */
    private final Variable var;

    /**
   *The constructor for the context
   * 
   * @param previous the previous context (might be null)
   * @param var the variable
   */
    public Environment(Environment previous, Variable var) {
        super();
        this.previous = previous;
        this.var = var;
    }

    /**
   * The constructor for initial environment
   */
    public Environment() {
        var = null;
        previous = null;
    }

    /**
   * Find a variable
   * 
   * @param name the name to lookup
   * @return the variable or null if it was not found.
   */
    public Variable find(String name) {
        for (Environment env = this; env.previous != null; env = env.previous) {
            if (env.var.name.equals(name)) {
                return env.var;
            }
        }
        return null;
    }

    /**
   * Find variable or fail with exception
   * 
   * @param location the location of the node that requested the the variable
   * @param name the name to lookup
   * @return the found variable
   * @throws EvalException if variable is not found
   */
    public Variable findDefined(SourceLocation location, String name) {
        Variable rc = find(name);
        if (rc == null) {
            throw new EvalException(location, "No variable with " + name + " is defined.");
        }
        return rc;
    }

    /**
   * Check if the variable is free
   * 
   * @param location the location of the check
   * @param name the name to check
   */
    public void checkFree(SourceLocation location, String name) {
        Variable previousDef = find(name);
        if (previousDef != null) {
            throw new EvalException(location, "Then name '" + name + "' has been already defined at " + previousDef.defineLocation.toShortString());
        }
    }
}
