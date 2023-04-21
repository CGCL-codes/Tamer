package org.ttt.salt.editor;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;
import org.flyingtitans.xml.ObservableDocument;
import org.ttt.salt.TBXFile;

/**
 * This is a container that will hold the TBXFile in an observable
 * enviroment.
 *
 * @author Lance Finn Helsten
 * @version $Id: TBXDocument.java 1 2008-05-23 03:51:58Z lanhel $
 */
public class TBXDocument extends ObservableDocument implements ObservableDocument.NodeListener {

    /** SCM information. */
    private static final String RCSID = "$Id: TBXDocument.java 1 2008-05-23 03:51:58Z lanhel $";

    /** */
    public static final String AUTO_TERMENTRY_ID_PREFIX = "AUTO_termEntry_ID_";

    /** */
    public static final int TYPE_UNKNOWN = 0;

    /** */
    public static final int TYPE_CORRUPT = 1;

    /** */
    public static final int TYPE_DTD = 2;

    /** */
    public static final int TYPE_SCHEMA = 3;

    /** */
    private static final Logger LOGGER = Logger.getLogger("org.ttt.salt.editor");

    /** The master TBXFile. */
    private TBXFile tbxFile;

    /** Flag to mark the change status of this document. */
    private boolean dirty;

    /**
     *
     * @param file The file to read the XML data from.
     * @throws IOException Any unhandled I/O exceptions.
     */
    public TBXDocument(File file) throws IOException {
        this(new TBXFile(file));
        addObserver(this);
    }

    /**
     *
     * @param file The file to read the XML data from.
     * @param sysid The systemId from which to search for entities.
     * @throws IOException Any unhandled I/O exceptions.
     */
    public TBXDocument(File file, String sysid) throws IOException {
        this(new TBXFile(file));
        addObserver(this);
    }

    /**
     *
     * @param tfile The TBXFile to attach to.
     * @throws IOException Any unhandled I/O exceptions.
     */
    public TBXDocument(TBXFile tfile) throws IOException {
        super();
        tbxFile = tfile;
        tbxFile.parseAndValidate();
        setDocument(tbxFile.getTBXDocument());
    }

    /**
     * Get the main TBXFile.
     *
     * @return TBXFile that is backing this TBXDocument.
     */
    public TBXFile getTBXFile() {
        return tbxFile;
    }

    /**
     * Find out if the document has been changed.
     *
     * @return true => The document has been changed.
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     *
     * @param file The file to write this document to.
     * @throws IOException Any unhandled I/O exceptions.
     */
    public void write(File file) throws IOException {
        tbxFile.write(file);
        dirty = false;
    }

    /**
     * Utility function to get the body element.
     *
     * @return The element that is the main body element.
     */
    public Element getBodyElement() {
        Element root = getDocumentElement();
        Element text = (Element) root.getElementsByTagName("text").item(0);
        Element body = (Element) text.getElementsByTagName("body").item(0);
        return body;
    }

    /** {@inheritDoc} */
    public void attributeChange(ObservableDocument doc, Node node, String oldvalue) {
        dirty = true;
        if (node.getParentNode() instanceof Element) {
            Element elem = (Element) node.getParentNode();
            if (elem.getTagName().equals("termEntry")) {
                tbxFile.getTermEntryMap().remove(oldvalue);
                String id = elem.getAttribute("id");
                if (id.equals("")) {
                    id = tbxFile.getTermEntryAutoId();
                    elem.setAttribute("id", id);
                }
                tbxFile.getTermEntryMap().put(id, elem);
            }
        }
    }

    /** {@inheritDoc} */
    public void valueChange(ObservableDocument doc, Node node) {
        dirty = true;
    }

    /** {@inheritDoc} */
    public void prefixChange(ObservableDocument doc, Node node) {
        dirty = true;
    }

    /** {@inheritDoc} */
    public void childAdded(ObservableDocument doc, Node node, Node child) {
        dirty = true;
        if (child instanceof Element) {
            Element elem = (Element) child;
            if (elem.getTagName().equals("termEntry")) {
                String id = elem.getAttribute("id");
                if (id.equals("")) {
                    id = tbxFile.getTermEntryAutoId();
                    elem.setAttribute("id", id);
                }
                tbxFile.getTermEntryMap().put(id, elem);
            }
        }
    }

    /** {@inheritDoc} */
    public void childRemoved(ObservableDocument doc, Node node, Node child) {
        dirty = true;
        if (child instanceof Element) {
            Element elem = (Element) child;
            if (elem.getTagName().equals("termEntry")) {
                String id = elem.getAttribute("id");
                tbxFile.getTermEntryMap().remove(id);
            }
        }
    }

    /** {@inheritDoc} */
    public void parentAdded(ObservableDocument doc, Node node, Node parent) {
        dirty = true;
    }

    /** {@inheritDoc} */
    public void parentRemoved(ObservableDocument doc, Node node, Node parent) {
        dirty = true;
    }

    /** {@inheritDoc} */
    public void nodeNormalize(ObservableDocument doc, Node node) {
    }

    /** {@inheritDoc} */
    public void nodeMoved(ObservableDocument doc, Node node) {
        dirty = true;
    }

    /** {@inheritDoc} */
    public void textContent(ObservableDocument doc, Node node, String textContent) {
    }

    /** {@inheritDoc} */
    public void setUserData(ObservableDocument doc, Node node, String key, Object data, UserDataHandler handler) {
    }
}
