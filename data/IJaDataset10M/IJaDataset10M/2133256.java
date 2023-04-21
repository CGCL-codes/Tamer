package ch.nostromo.lib.swing;

import javax.swing.JPasswordField;
import javax.swing.text.Document;
import ch.nostromo.lib.swing.text.TextFieldFilter;

/**
 * Base password field containing some wrappers for the NosTextFieldFilter.
 * 
 * @author    Bernhard von Gunten <bvg_dev@nostromo.ch>
 */
public class NosPasswordField extends JPasswordField {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The text field filter. */
    private TextFieldFilter textFieldFilter = null;

    /**
   * Constructor for the NosPasswordField object.
   */
    public NosPasswordField() {
        super();
        this.init();
    }

    /**
   * Description of the Method.
   */
    private void init() {
        this.textFieldFilter = new TextFieldFilter();
        this.setDocument(this.textFieldFilter);
    }

    /**
   * Constructor for the NosPasswordField object.
   * 
   * @param doc the doc
   * @param text the text
   * @param columns the columns
   */
    public NosPasswordField(Document doc, String text, int columns) {
        super(doc, text, columns);
        this.init();
    }

    /**
   * Constructor for the NosPasswordField object.
   * 
   * @param columns the columns
   */
    public NosPasswordField(int columns) {
        super(columns);
        this.init();
    }

    /**
   * Constructor for the NosPasswordField object.
   * 
   * @param text the text
   */
    public NosPasswordField(String text) {
        super(text);
        this.init();
    }

    /**
   * Constructor for the NosPasswordField object.
   * 
   * @param text the text
   * @param columns the columns
   */
    public NosPasswordField(String text, int columns) {
        super(text, columns);
        this.init();
    }

    /**
   * Sets the acceptedChars attribute of the NosPasswordField object.
   * 
   * @param acceptedChars the accepted chars
   */
    public void setAcceptedChars(String acceptedChars) {
        this.textFieldFilter.setAcceptedCharacters(acceptedChars);
    }

    /**
   * Gets the acceptedChars attribute of the NosPasswordField object.
   * 
   * @return    The acceptedChars value
   */
    public String getAcceptedChars() {
        return this.textFieldFilter.getAcceptedCharacters();
    }

    /**
   * Sets the maxLength attribute of the NosPasswordField object.
   * 
   * @param maxLength the max length
   */
    public void setMaxLength(int maxLength) {
        this.textFieldFilter.setMaxLength(maxLength);
    }

    /**
   * Gets the maxLength attribute of the NosPasswordField object.
   * 
   * @return    The maxLength value
   */
    public int getMaxLength() {
        return this.textFieldFilter.getMaxLength();
    }
}
