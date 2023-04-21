package com.sebulli.fakturama.editors;

import java.util.ArrayList;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Gerd Bartelt
 *
 */
public class Suggestion implements VerifyListener {

    private CCombo combo = null;

    private Text text = null;

    private boolean textCorrected = false;

    String[] suggestions = null;

    /**
 	 * Constructor for combo boxes
 	 * 
	 * @param combo
	 * 			The combo box
	 * @param suggestions
	 * 			List with all the possible strings
	 */
    public Suggestion(CCombo combo, String[] suggestions) {
        this.combo = combo;
        this.suggestions = suggestions;
    }

    /**
 	 * Constructor for text fields
 	 * 
	 * @param text
	 * 			The text field
	 * @param suggestions
	 * 			List with all the possible strings
	 */
    public Suggestion(Text text, String[] suggestions) {
        this.text = text;
        this.suggestions = suggestions;
    }

    /**
	 * Search for the "base" string in the list and get those part of the string
	 * that was found in the list. If there are more than one entry that starts
	 * with the same sequence, return the sequence, that is equal in all strings
	 * of the list.
	 * 
	 * @param base
	 *            String to search for
	 * @return Result string
	 */
    public String getSuggestion(String base) {
        if (base.isEmpty()) return "";
        ArrayList<String> resultStrings = new ArrayList<String>();
        for (int i = 0; i < suggestions.length; i++) {
            if (suggestions[i].toLowerCase().startsWith(base.toLowerCase())) resultStrings.add(suggestions[i]);
        }
        if (resultStrings.isEmpty()) return "";
        String tempResult = resultStrings.get(0);
        String result = "";
        for (String resultString : resultStrings) {
            int length = tempResult.length();
            if (resultString.length() < length) length = resultString.length();
            for (int i = 0; i < length; i++) {
                if (tempResult.substring(0, i + 1).equalsIgnoreCase(resultString.substring(0, i + 1))) result = tempResult.substring(0, i + 1);
            }
            tempResult = result;
        }
        return result;
    }

    @Override
    public void verifyText(VerifyEvent e) {
        if ((e.keyCode == 8) || (e.keyCode == 127)) textCorrected = true;
        if (!e.text.isEmpty() && !textCorrected) {
            String s = "";
            if (combo != null) s = combo.getText() + e.text;
            if (text != null) s = text.getText() + e.text;
            String suggestion = getSuggestion(s);
            if (!suggestion.isEmpty()) {
                if (combo != null) combo.setText("");
                if (text != null) text.setText("");
                e.text = suggestion;
            }
        }
    }
}
