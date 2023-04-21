package unipi.virtuallab.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Un oggetto Document nel quale possono essere inseriti solamente numeri, punti e caratteri che esprimono il segno di un numero (+ o -)
 * @author Edoardo Canepa
 */
public class DoubleDocument extends PlainDocument {

    /**
     * Metodo standard del Document
     * @param offs 
     * @param str 
     * @param a 
     * @throws javax.swing.text.BadLocationException 
     */
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        char[] upper = str.toCharArray();
        StringBuffer clear = new StringBuffer();
        for (int i = 0; i < upper.length; i++) {
            if (Character.isDigit(upper[i]) || upper[i] == '-' || upper[i] == '.') {
                clear.append(upper[i]);
            }
        }
        super.insertString(offs, clear.toString(), a);
    }
}
