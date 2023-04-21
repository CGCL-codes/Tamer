package meta.codeanywhere.manager.security;

/**
 * @author Biao Zhang
 * @version 11/16/2006
 *
 */
public abstract class Policy {

    public enum PolicyLevel {

        ClassLevel, MethodLevel, StatementLevel
    }

    protected PolicyLevel level;

    /**
	  * Check if the code is security
	  * @param block The code block
	  * @return True if passed else false
	  */
    public abstract boolean check(String block);

    /**
	 * Get the level of the policy
	 * @return level The level
	 */
    public PolicyLevel getLevel() {
        return level;
    }

    /**
	 * Set the level of the policy
	 * @param level The level to set
	 */
    public void setLevel(PolicyLevel level) {
        this.level = level;
    }
}
