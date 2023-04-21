package error;

import java.awt.event.*;
import userinterface.*;

/**A text area to display error message
  *
  *@author Tan Hong Cheong
  *@version 20041210
  */
public abstract class ErrorTextArea extends ExtendetJTextArea implements MouseListener {

    /**Constructor
      */
    public ErrorTextArea() {
        super();
        textArea.addMouseListener(this);
    }

    /**As defined in the interface MouseListener
      */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            int pos = textArea.getCaretPosition();
            StringBuffer b = new StringBuffer(textArea.getText());
            int i = pos - 1;
            int start = 0;
            int end = b.length();
            boolean found = false;
            while ((i > 0) && (!found)) {
                if (b.charAt(i) == '\n') {
                    found = true;
                    start = i + 1;
                } else {
                    i--;
                }
            }
            i = pos;
            found = false;
            while ((i < b.length()) && (!found)) {
                if (b.charAt(i) == '\n') {
                    found = true;
                    end = i;
                } else {
                    i++;
                }
            }
            String s = b.substring(start, end);
            textArea.setSelectionStart(start);
            textArea.setSelectionEnd(end);
            processMessage(s);
        }
    }

    /**process the line of text where user double clicked
      *@param s the message
      */
    protected abstract void processMessage(String s);

    /**As defined in the interface MouseListener
      */
    public void mouseEntered(MouseEvent e) {
    }

    /**As defined in the interface MouseListener
      */
    public void mouseExited(MouseEvent e) {
    }

    /**As defined in the interface MouseListener
      */
    public void mousePressed(MouseEvent e) {
    }

    /**As defined in the interface MouseListener
      */
    public void mouseReleased(MouseEvent e) {
    }
}
