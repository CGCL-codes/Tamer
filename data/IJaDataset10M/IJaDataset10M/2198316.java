package de.jcommandlineparser.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * An abstract extension for {@link AbstractOption}. This command line option
 * can have more than one value.
 * </p>
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-12-15
 * 
 * @param <T>
 *            type of value
 */
public abstract class AbstractMultiOption<T> extends AbstractOption<T> {

    /**
	 * <p>
	 * Delimiter between values.
	 * </p>
	 */
    public static final String DELIM_PATTERN = ",";

    private volatile List<T> values = new ArrayList<T>();

    protected AbstractMultiOption(Class<T> returnType, char identifierShort, String identifierLong, String description, boolean required) {
        super(returnType, identifierShort, identifierLong, description, required);
    }

    protected AbstractMultiOption(Class<T> returnType, char identifierShort, String identifierLong, String description, boolean required, Set<AbstractOption<?>> clashOptions) {
        super(returnType, identifierShort, identifierLong, description, required, clashOptions);
    }

    /**
	 * <p>
	 * Get list of values.
	 * </p>
	 */
    public final List<T> getValues() {
        if (this.isSet()) return values;
        throw new OptionNotSetException("option \"" + this + "\" not set");
    }

    public final void setValues(List<T> values) {
        this.values = values;
        this.setSet(true);
    }
}
