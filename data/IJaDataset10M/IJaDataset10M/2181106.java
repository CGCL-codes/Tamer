package eval.swing;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class SwingStyledText extends JTextPane {

    public SwingStyledText() {
        setEditable(false);
        setFont(new Font("Courier", Font.PLAIN, 12));
        Style paragraph = addStyle("Paragraph", null);
        StyleConstants.setForeground(paragraph, Color.red);
        StyleConstants.setLeftIndent(paragraph, 10f);
        StyleConstants.setSpaceAbove(paragraph, 20f);
        JTextPane inline = new JTextPane();
        inline.setBorder(new LineBorder(Color.green));
        Style s = inline.addStyle("S", null);
        StyleConstants.setBold(s, true);
        Document doc = getDocument();
        try {
            doc.insertString(0, "Hello \n\n\n", paragraph);
            setCaretPosition(doc.getLength() - 2);
            System.out.println(getCaretPosition());
            insertComponent(inline);
            doc.insertString(doc.getLength(), "...", paragraph);
            inline.getDocument().insertString(0, ".........", s);
        } catch (BadLocationException ex) {
        }
    }
}
