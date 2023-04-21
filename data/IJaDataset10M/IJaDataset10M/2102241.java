package client.gui;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Komponent chat okna
 * @author Guldan
 */
public class RespondPanel extends JPanel {

    private JScrollPane messageScroll;

    private JTextPane respondField = new JTextPane();

    private ChatFrame frame;

    private StyledDocument doc;

    public RespondPanel(int xsize, ChatFrame frame) {
        this.frame = frame;
        messageScroll = new JScrollPane(respondField);
        doc = respondField.getStyledDocument();
        addStylesToDocument(doc);
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScroll.setPreferredSize(new Dimension(xsize - 18, 130));
        add(messageScroll);
        listeners();
    }

    /**
     * Inicializácia špecifických štýlov
     * @param doc
     */
    protected void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
    }

    /**
     * Vymazanie obsahu komponentu
     */
    public void restart() {
        respondField.setText("");
    }

    /**
     * Pridanie do okna správu
     * @param str správa
     */
    public void append(String str) {
        try {
            doc.insertString(respondField.getText().length(), str, doc.getStyle("regular"));
        } catch (BadLocationException ex) {
        }
    }

    /**
     * Získanie správy z okna
     * @return správa
     */
    public String getMessage() {
        return respondField.getText();
    }

    /**
     * Inicializácia funkčnosti tlačidla
     */
    private void listeners() {
        respondField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.send();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restart();
                }
            }
        });
    }
}
