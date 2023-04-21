package org.eclipse.nebula.widgets.formattedtext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;

/**
 * Default formatter for the String class.<br>
 * This formatter is a kind of NullFormatter and do no formatting, allowing
 * the edit of simple String values, without constraints.<br>
 * It is registered in the DefaultFormatterFactory as the default formatter
 * for String values.
 */
public class StringFormatter extends AbstractFormatter {

    /**
   * Returns the current value formatted for display.<p>
   * There is no difference in this formatter between edit and display values.
   * So this method returns the edit string.
   * 
   * @return display string
   * @see ITextFormatter#getDisplayString()
   */
    public String getDisplayString() {
        return getEditString();
    }

    /**
   * Returns the current value formatted for editing.
   * This method is called by <code>FormattedText</code> when the <code>Text</code>
   * widget gains focus.<br>
   * This formatter has no formatting features. So it simply return the Text
   * widget content.
   * 
   * @return edit string
   * @see ITextFormatter#getEditString()
   */
    public String getEditString() {
        return text.getText();
    }

    /**
   * Returns the current value.<br>
   * This formatter has no formatting features. So it simply return the Text
   * widget content.
   * 
   * @return current string value
   * @see ITextFormatter#getValue()
   */
    public Object getValue() {
        return text.getText();
    }

    /**
   * Returns the type of value this {@link ITextFormatter} handles,
   * i.e. returns in {@link #getValue()}.<br>
   * A StringFormatter always returns a String value.
   * 
   * @return The value type.
   */
    public Class getValueType() {
        return String.class;
    }

    /**
   * Returns <code>true</code> if current edited value is empty, else returns
   * <code>false</code>.<br>
   * A string is empty if its length is 0.
   * 
   * @return true if empty, else false
   */
    public boolean isEmpty() {
        return text.getText().length() == 0;
    }

    /**
   * Returns <code>true</code> if current edited value is valid, else returns
   * <code>false</code>.<br>
   * StringFormatter always return true.
   * 
   * @return true
   * @see ITextFormatter#isValid()
   */
    public boolean isValid() {
        return true;
    }

    /**
   * Sets the value to edit. The value provided must be a <code>String</code>.
   * The Text widget is simply updated with the value.
   * 
   * @param value string value
   * @throws IllegalArgumentException if not a string
   * @see ITextFormatter#setValue(java.lang.Object)
   */
    public void setValue(Object value) {
        if (!(value instanceof String)) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        text.setText((String) value);
    }

    /**
   * Handles a <code>VerifyEvent</code> sent when the text is about to be modified.
   * This method is the entry point of all operations of formatting.<br>
   * This formatter has no formatting features. So this method do nothing.
   * 
   * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
   */
    public void verifyText(VerifyEvent e) {
    }
}
