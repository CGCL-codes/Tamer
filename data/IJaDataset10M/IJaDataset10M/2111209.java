package net.sourceforge.acelogger.level.filter;

import net.sourceforge.acelogger.level.LogLevel;

/**
 * A filter that accepts no levels.
 * 
 * @author Zardi (https://sourceforge.net/users/daniel_zardi)
 * @version 1.0.0
 * @since 1.0.0
 */
public class AcceptNoneLevelFilter implements LevelFilter {

    /**
	 * The identifier of this filter.
	 */
    private String identifier;

    /**
	 * Constructs a new link AcceptNoneLevelFilter with the supplied identifier.
	 * 
	 * @param identifier
	 *            The identifier for this filter.
	 * @since 1.0.0
	 */
    public AcceptNoneLevelFilter(String identifier) {
        setIdentifier(identifier);
    }

    /** {@inheritDoc} */
    public final String getIdentifier() {
        return identifier;
    }

    /**
	 * Sets the string that identifies this object.
	 * 
	 * @param identifier
	 *            The identifier of this object.
	 * @since 1.0.0
	 */
    private void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /** {@inheritDoc} */
    public boolean isSuitable(LogLevel level) {
        return false;
    }
}
