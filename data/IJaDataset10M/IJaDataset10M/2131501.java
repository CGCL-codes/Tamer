package groupscheme.grewp;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import jsint.Procedure;
import jscheme.JS;

public class PlainDocumentWrapper extends PlainDocument {

    Procedure insertProc, deleteProc;

    JS js;

    public PlainDocumentWrapper(Procedure insertProc, Procedure deleteProc) {
        super();
        this.insertProc = insertProc;
        this.deleteProc = deleteProc;
        this.js = new JS();
    }

    /**
     * This is called when someone attempts to insert a string in the textarea based on this document.
     * It does not read or modify the document at all, rather it calls the insertProc on the arguments
     **/
    public synchronized void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        if (insertProc != null) js.call(insertProc, new Integer(offset), str, a);
    }

    /**
     * This is called when someone attempts to delete a string from the textarea based on this document.
     * It does not read or modify the document at all, rather it calls the deleteProc on the arguments
     **/
    public synchronized void remove(int offset, int len) throws BadLocationException {
        Segment s = new Segment();
        this.getText(offset, len, s);
        if (deleteProc != null) js.call(deleteProc, new Integer(offset), new Integer(len), s);
    }

    /**
     * this is an alias for the insertString of the superclass. It is called when we really want to
     * insert something into the textarea. The 'Procedure' argument is deprecated and is no longer used.
     * This grabs a write lock before inserting. According to the Java docs, t is thread-safe.
     **/
    public synchronized void insertStringNoSend(int offset, String str, AttributeSet a, Procedure p) throws BadLocationException {
        super.insertString(offset, str, a);
        if (p != null) js.call(p, new Integer(offset), str, a);
    }

    /**
     * this is an alias for the insertString of the superclass. It is called when we really want to
     * insert something into the textarea. The 'Procedure' argument is deprecated and is no longer used
     * This grabs a write lock before deleting. According to the Java docs, t is thread-safe.
     **/
    public synchronized void removeNoSend(int offset, int len, Procedure p) throws BadLocationException {
        super.remove(offset, len);
        if (p != null) js.call(p, new Integer(offset), new Integer(len));
    }
}
