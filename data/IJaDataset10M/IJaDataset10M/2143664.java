package gwt.validator.client.check;

import java.util.List;

/**
 * <code>Check</code>s represent validation rules.
 * 
 * The implementation should be fine grained, meaning that a check should only
 * test a single requirement. Actions can be composed using
 * <code>ValidationScope</code>s.
 * 
 * @author <a href="mailto:nikolaus.rumm@gmail.com">Nikolaus Rumm</a>
 */
public interface Check {

    /**
	 * Performs the validation.
	 * 
	 * @return The list of validation issues. An empty list means that there
	 *         were no issues.
	 */
    public List validate();
}
