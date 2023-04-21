package todopad.ui.handler;

import java.text.*;
import java.util.*;
import javax.swing.text.*;

public class InsertDateHandler {

    public InsertDateHandler(JTextComponent aTextComponent) {
        int lSelectionStart = aTextComponent.getSelectionStart();
        SimpleDateFormat lDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = lDateFormat.format(new Date());
        try {
            aTextComponent.getDocument().insertString(lSelectionStart, dateString, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
