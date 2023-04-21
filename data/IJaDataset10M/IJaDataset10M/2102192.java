package boccaccio.andrea.mySimpleSynchronizer.guiFrontend.control;

import java.awt.Component;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

/**
 * @author Andrea Boccaccio
 *
 */
public class RegexDocumentListener extends AbsDocumentListener {

    public RegexDocumentListener() {
        super();
        this.setStrToDo("Regex");
    }

    public RegexDocumentListener(Component view) {
        this();
        this.setView(view);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        try {
            this.getIfm().setStrRegex(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.changedUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.changedUpdate(e);
    }
}
