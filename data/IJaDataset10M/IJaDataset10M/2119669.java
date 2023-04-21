package fbench.dom;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import fbench.LibraryElementView;
import fbench.dom.events.ElementSelectionEvent;
import fbench.tree.DOMTree;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.events.MutationEventImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;

/**
 * A model for displaying and navigating in a JTextArea the XML source code of
 * an IEC 61499 LibraryElement whose underlying model is a DOM Document.
 * 
 * @author JHC
 * @version 20080515/DG - Moved Xml writer to its own class.
 * @version 20051115/JHC - Made a DOM EventListener displaying only selected
 *          Element.
 * @version 20050420/JHC
 */
public class DOMTextModel extends ElementModel implements EventListener, DOMWriterListener {

    protected JTextArea textarea;

    protected Hashtable<Element, Point> selectionRanges;

    protected Document document;

    private boolean modified = false;

    public DOMTextModel(JTextArea textArea, Document document) {
        super();
        this.textarea = textArea;
        this.textarea.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent evt) {
            }

            public void keyReleased(KeyEvent evt) {
            }

            public void keyTyped(KeyEvent evt) {
                setModified(true);
            }
        });
        this.selectionRanges = new Hashtable<Element, Point>();
        setDocument(document);
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean mod) {
        modified = mod;
    }

    /** Set the document and refresh the display. */
    public void setDocument(Document doc) {
        if (this.document != null) {
            ((DocumentImpl) document).removeEventListener(MutationEventImpl.DOM_ATTR_MODIFIED, this, false);
            ((DocumentImpl) document).removeEventListener(MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED, this, false);
            ((DocumentImpl) document).removeEventListener(MutationEventImpl.DOM_NODE_INSERTED, this, false);
            ((DocumentImpl) document).removeEventListener(MutationEventImpl.DOM_NODE_REMOVED, this, false);
            ((DocumentImpl) document).removeEventListener("ElementSelectionEvent", this, false);
        }
        this.document = doc;
        this.selectionRanges.clear();
        ((DocumentImpl) document).addEventListener(MutationEventImpl.DOM_ATTR_MODIFIED, this, false);
        ((DocumentImpl) document).addEventListener(MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED, this, false);
        ((DocumentImpl) document).addEventListener(MutationEventImpl.DOM_NODE_INSERTED, this, false);
        ((DocumentImpl) document).addEventListener(MutationEventImpl.DOM_NODE_REMOVED, this, false);
        ((DocumentImpl) document).addEventListener("ElementSelectionEvent", this, false);
        try {
            textarea.setText(DOMWriter.writeXmlString(this.document, this));
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }

    public void scrollToLine(int lineNum) {
        try {
            textarea.setCaretPosition(textarea.getLineStartOffset(lineNum));
        } catch (BadLocationException blex) {
            blex.printStackTrace();
        }
    }

    public void handleEvent(Event evt) {
        if (evt instanceof MutationEvent) {
            try {
                MutationEvent mevt = (MutationEvent) evt;
                Element el = null;
                if (mevt.getType().equals(MutationEventImpl.DOM_ATTR_MODIFIED)) {
                    AttrImpl attr = (AttrImpl) mevt.getRelatedNode();
                    el = attr.getOwnerElement();
                } else el = (Element) mevt.getRelatedNode();
                this.rewrite();
                Point p = selectionRanges.get(el);
                textarea.setCaretPosition(p.x);
                modified = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (evt instanceof ElementSelectionEvent) {
            Element e = ((ElementSelectionEvent) evt).getSelectedElement();
            Point p = selectionRanges.get(e);
            textarea.requestFocus();
            textarea.setCaretPosition(p.x);
        }
    }

    public void setElement(Element el) {
        super.setElement(el);
        ((org.w3c.dom.events.EventTarget) el).addEventListener("MutationEvent", this, false);
        if (el == document.getDocumentElement()) setDocument(document); else {
            try {
                textarea.setText(DOMWriter.writeXmlString(el, this));
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
    }

    public void rewrite() {
        try {
            selectionRanges.clear();
            textarea.setText(DOMWriter.writeXmlString(this.document, this));
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }

    public String getText(boolean forSave) {
        if (forSave) this.modified = false;
        return textarea.getText();
    }

    @Override
    public void beginElement(Element element, int position) {
        selectionRanges.put(element, new Point(position, 0));
    }

    @Override
    public void endElement(Element element, int position) {
        Point p = selectionRanges.get(element);
        if (p != null) p.y = position;
    }
}
