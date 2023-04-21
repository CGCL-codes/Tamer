package jSimMacs.display;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

/**
 * @author sr
 *
 */
public class JTextAreaFIFO extends JTextArea implements DocumentListener {

    private int lines;

    public JTextAreaFIFO() {
        this(500);
    }

    public JTextAreaFIFO(int lines) {
        this.lines = lines;
        getDocument().addDocumentListener(this);
    }

    public void changedUpdate(DocumentEvent e) {
    }

    private void removeLines() {
        Element root = getDocument().getDefaultRootElement();
        while (root.getElementCount() > lines) {
            Element firstLine = root.getElement(0);
            try {
                getDocument().remove(0, firstLine.getEndOffset());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            setCaretPosition(getDocument().getLength());
        }
    }

    public void insertUpdate(DocumentEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                removeLines();
            }
        });
    }

    public void removeUpdate(DocumentEvent e) {
    }
}
