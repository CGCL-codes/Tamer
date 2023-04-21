package bill.util.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

/**
 * This class is a special subclass of the JTextFieldMaxCharsDocument 
 * class. It checks to make sure characters being inserted into the
 * document (and therefor the text field) are contained in the filter string.
 * If we are using the class in include mode and they are not, they are
 * discarded. If we are using the class in exclude mode and they are, they
 * are discarded.
 */
public class JTextFieldFilterDocument extends JTextFieldMaxCharsDocument {

    /** The filter to apply to the input string */
    private String _filter;

    /** Indicates the include mode. If <code>true</code> then characters
       entered that are in the filter string are kept and others are
       dropped, if <code>false</code> then vice versa. */
    private boolean _include;

    /**
    * Creates a new class instance with the specified filter that uses
    * include mode and limits the number of characters entered.
    *
    * @param textField The text field that we are putting the filter on.
    */
    public JTextFieldFilterDocument(JTextFieldFilterInterface textField) {
        super((JTextFieldMaxChars) textField);
        if (textField.getFilter() != null) _filter = textField.getFilter(); else _filter = new String();
        _include = true;
    }

    /**
    * Sets the filter string to the specified value. We do not allow the
    * filter to be set to <code>null</code> so if the user passes this
    * in we set the filter to an empty string.
    *
    * @param filter String to set the filter to.
    */
    public void setFilter(String filter) {
        if (filter != null) _filter = filter; else _filter = new String();
    }

    /**
    * Retrieves the current filter string value.
    *
    * @return The filter string value.
    */
    public String getFilter() {
        return _filter;
    }

    /**
    * Sets the include mode of the filter. If the include mode is <code>
    * true</code> then characters entered that are in the filter string
    * are kept and others are dropped, if <code>false</code> then vice
    * versa.
    *
    * @param include Value to set include mode to.
    */
    public void setInclude(boolean include) {
        _include = include;
    }

    /**
    * Retrieves the include mode for this instance.
    *
    * @return <code>true</code> if we are using include mode, <code>false
    * </code> if we are using exclude mode.
    */
    public boolean isInclude() {
        return (_include);
    }

    /**
    * Called when a character (or characters) are added to the
    * document. Applies the filter criteria and updates the document
    * as appropriate.
    *
    * @param offs The starting offset to insert at (>=0).
    * @param str The string to insert, does nothing with null/empty strings.
    * @param a The attributes for the inserted content.
    */
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        int count = 0;
        char[] unfiltered;
        StringBuffer filtered = new StringBuffer();
        if (str == null) return;
        unfiltered = str.toCharArray();
        for (count = 0; count < unfiltered.length; count++) {
            if (_include) {
                if (_filter.indexOf(unfiltered[count]) >= 0) filtered.append(unfiltered[count]);
            } else {
                if (_filter.indexOf(unfiltered[count]) < 0) filtered.append(unfiltered[count]);
            }
        }
        if (filtered.length() > 0) super.insertString(offs, filtered.toString(), a);
    }
}
