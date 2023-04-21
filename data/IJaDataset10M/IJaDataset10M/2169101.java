package org.sgodden.echo.ext20;

/**
 * A html editor field.
 * <p/>
 * TODO - listeners on value change
 * <p/>
 * NOTE - this ext component does not seem to work too well across browsers.  Look at
 * implementing a TinyMCE component instead.
 * 
 * @author simon
 *
 */
@SuppressWarnings({ "serial" })
public class HtmlEditor extends ExtComponent {

    public static final String PROPERTY_FIELD_LABEL = "fieldLabel";

    public static final String PROPERTY_TEXT_CHANGED = "text";

    /**
     * Creates a new empty html editor.
     */
    public HtmlEditor() {
        super();
    }

    /**
     * Creates a new html editor with the passed HTML.
     * @param text the html content for the editor.
     */
    public HtmlEditor(String text) {
        setText(text);
    }

    /**
     * Returns the field label.
     * @return the field label.
     */
    public String getFieldLabel() {
        return (String) get(PROPERTY_FIELD_LABEL);
    }

    /**
     * Sets the field label to be used in forms.
     * @param fieldLabel the field label to be used in forms.
     */
    public void setFieldLabel(String fieldLabel) {
        set(PROPERTY_FIELD_LABEL, fieldLabel);
    }

    /**
     * Sets the HTML content.
     * @param text the html content.
     */
    public void setText(String text) {
        set(PROPERTY_TEXT_CHANGED, text);
    }

    /**
     * Returns the HTML content.
     * @return the HTML content.
     */
    public String getText() {
        return (String) get(PROPERTY_TEXT_CHANGED);
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        if (PROPERTY_TEXT_CHANGED.equals(inputName)) {
            setText((String) inputValue);
        }
    }
}
