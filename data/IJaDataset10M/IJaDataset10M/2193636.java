package gwt.validator.client.check;

import gwt.validator.client.exception.ValidationIssue;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * Validator for checking that a String's content is an email address.
 * 
 * @author <a href="mailto:nikolaus.rumm@gmail.com">Nikolaus Rumm</a>
 */
public class EmailAddressCheck extends StringPatternCheck {

    public static final String EMAIL_PATTERN = "^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z])?[a-zA-Z]*)*\\s+<(" + "\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,4})>$|^(\\w[-._\\w]*\\w" + "@\\w[-._\\w]*\\w\\.\\w{2,4})$";

    /**
	 * Constructor
	 * 
	 * @param aTextBox
	 *            The text box that displays the text to be validated
	 * @param aLabel
	 *            The label that displays the property's name
	 */
    public EmailAddressCheck(TextBoxBase aTextBox, Label aLabel) {
        super(aTextBox, aLabel, EMAIL_PATTERN);
    }

    /**
	 * Constructor
	 * 
	 * @param aTextBox
	 *            The text box that displays the text to be validated
	 * @param aPropertyName
	 *            The property's name
	 */
    public EmailAddressCheck(TextBoxBase aTextBox, String aPropertyName) {
        super(aTextBox, aPropertyName, EMAIL_PATTERN);
    }

    public List validateImpl() {
        List res = new ArrayList();
        String txt = this.getTextBox().getText();
        if (txt != null && txt.replaceFirst(this.getPattern(), "").length() > 0) {
            res.add(new ValidationIssue(this.getIssueMessageFactory().stringDoesNotMatchEmailAddressPattern(this.getPropertyName())));
        }
        return res;
    }
}
