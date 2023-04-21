package org.itsnat.manual.comp;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;

public class CompExampleProcessor implements EventListener, DocumentListener {

    protected ItsNatHTMLDocument itsNatDoc;

    protected ItsNatHTMLInputText inputComp;

    public CompExampleProcessor(ItsNatHTMLDocument itsNatDoc) {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load() {
        ItsNatHTMLComponentManager compMgr = itsNatDoc.getItsNatHTMLComponentManager();
        this.inputComp = (ItsNatHTMLInputText) compMgr.addItsNatComponentById("inputId");
        inputComp.setText("Change this text and lost the focus");
        inputComp.addEventListener("change", this);
        PlainDocument dataModel = (PlainDocument) inputComp.getDocument();
        dataModel.addDocumentListener(this);
        inputComp.focus();
        inputComp.select();
    }

    public void handleEvent(Event evt) {
        log("Text has been changed");
    }

    public void insertUpdate(DocumentEvent e) {
        javax.swing.text.Document docModel = e.getDocument();
        int offset = e.getOffset();
        int len = e.getLength();
        try {
            log("Text inserted: " + offset + "-" + len + " chars,\"" + docModel.getText(offset, len) + "\"");
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void removeUpdate(DocumentEvent e) {
        int offset = e.getOffset();
        int len = e.getLength();
        log("Text removed: " + offset + "-" + len + " chars");
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public void log(String msg) {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element noteElem = doc.createElement("p");
        noteElem.appendChild(doc.createTextNode(msg));
        doc.getBody().appendChild(noteElem);
    }
}
