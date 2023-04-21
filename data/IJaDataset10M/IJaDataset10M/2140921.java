package org.frapuccino.addresscombobox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.frapuccino.autocomplete.AbstractAutoCompleter;

/**
 * Editable JComboBox with autocompletion functionality.
 * <p>
 * Additionally, its able to edit a comma-separated list of items, where the
 * autocompletion intelligently detects individual items to complete.
 * <p>
 * Known bug: Using the mouse in the popup-window confuses the autocomplete.
 * 
 * @author fdietz
 */
public class PatternSeparatedAutoCompleter extends AbstractAutoCompleter {

    protected List completionList;

    protected Pattern separatorPattern;

    protected boolean ignoreCase;

    public PatternSeparatedAutoCompleter(JTextComponent comp, List completionList, Pattern separatorPattern, boolean ignoreCase) {
        super(comp);
        this.completionList = completionList;
        this.ignoreCase = ignoreCase;
        this.separatorPattern = separatorPattern;
        list.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent arg0) {
                popup.setVisible(false);
                acceptedListItem((String) list.getSelectedValue());
            }
        });
    }

    protected boolean updateListData() {
        int caretPosition = textComp.getCaretPosition();
        if (caretPosition > textComp.getText().length() - 1) caretPosition = textComp.getText().length() - 1;
        if (caretPosition == -1) caretPosition = 0;
        String value = TextParser.getItemAt(textComp.getText(), separatorPattern, caretPosition);
        value = value.trim();
        int substringLen = value.length();
        if (substringLen == 0) return false;
        List possibleStrings = new ArrayList();
        Iterator iter = completionList.iterator();
        while (iter.hasNext()) {
            String listEntry = (String) iter.next();
            if (substringLen >= listEntry.length()) continue;
            if (ignoreCase) {
                if (value.equalsIgnoreCase(listEntry.substring(0, substringLen))) possibleStrings.add(listEntry);
            } else if (listEntry.startsWith(value)) possibleStrings.add(listEntry);
        }
        list.setListData(possibleStrings.toArray());
        return true;
    }

    protected void acceptedListItem(Object selected) {
        if (selected == null) return;
        int caret = textComp.getCaretPosition();
        String value = TextParser.getItemAt(textComp.getText(), separatorPattern, textComp.getCaretPosition());
        try {
            Document doc = textComp.getDocument();
            int startingPos = caret - value.length();
            doc.remove(startingPos, value.length());
            textComp.getDocument().insertString(startingPos, selected.toString(), null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        popup.setVisible(false);
    }

    /**
	 * @param separatorPattern The separatorPattern to set.
	 */
    public void setSeparatorPattern(Pattern separatorPattern) {
        this.separatorPattern = separatorPattern;
    }
}
