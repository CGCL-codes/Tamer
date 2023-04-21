package gwt.validator.client.check;

import gwt.validator.client.message.IssueMessages;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;

/**
 * Base class for all validators.
 * 
 * @author <a href="mailto:nikolaus.rumm@gmail.com">Nikolaus Rumm</a>
 * 
 */
public abstract class BaseCheck implements Check {

    /**
	 * The validation issues that were found by this check.
	 */
    private List issues;

    /**
	 * The label widget that holds the property's name
	 */
    private Label propertyNameLabel;

    /**
	 * The property's name
	 */
    private String propertyName;

    /**
	 * Constructor
	 * 
	 * @param aPropertyNameLabel
	 *            The label that holds the property's name
	 */
    public BaseCheck(Label aPropertyNameLabel) {
        super();
        checkNotNull(aPropertyNameLabel);
        propertyNameLabel = aPropertyNameLabel;
    }

    /**
	 * Constructor
	 * 
	 * @param aPropertyName
	 *            The property's names
	 */
    public BaseCheck(String aPropertyName) {
        super();
        checkNotNull(aPropertyName);
        propertyName = aPropertyName;
    }

    /**
	 * Performs the validation.
	 * 
	 * @return The list of validation issues. An empty list means that there
	 *         were no issues.
	 */
    public List validate() {
        return this.validateImpl();
    }

    /**
	 * Definition of the actual validation method.
	 * 
	 * Overwrite this with your specific implementation.
	 * 
	 * @return The list of validation issues. An empty list means that there
	 *         were no issues.
	 */
    protected abstract List validateImpl();

    /**
	 * Convenience method for checking that an argument is not null.
	 * 
	 * @param aParam
	 *            The argument
	 */
    protected void checkNotNull(Object aParam) {
        if (aParam == null) {
            throw new IllegalArgumentException("The argument must not be null.");
        }
    }

    /**
	 * Convenience mtehod for checking that an int argument is greater or equal
	 * than the given threshold.
	 * 
	 * @param aParam
	 *            The argument
	 * @param aMinValue
	 *            The minimum valid value of aParam
	 */
    protected void checkMinValue(int aParam, int aMinValue) {
        if (aParam < aMinValue) {
            throw new IllegalArgumentException("The argument must be greater or equal than " + aMinValue + ".");
        }
    }

    public Label getPropertyNameLabel() {
        return propertyNameLabel;
    }

    protected String getPropertyName() {
        String res = null;
        if (this.propertyName != null) {
            res = this.propertyName;
        } else if (this.propertyNameLabel != null) {
            res = this.propertyNameLabel.getText();
        } else {
            res = "field";
        }
        return res;
    }

    /**
	 * Constructs a new validation issue mesage factory.
	 * 
	 * TODO: This might be inefficient, but until now I didn't find a way to
	 * efficiently reuse it (static final didn't work).
	 * 
	 * @return
	 */
    protected IssueMessages getIssueMessageFactory() {
        return (IssueMessages) GWT.create(IssueMessages.class);
    }
}
