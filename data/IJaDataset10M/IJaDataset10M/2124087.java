package de.bea.domingo.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import org.apache.commons.httpclient.HttpStatus;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import de.bea.domingo.DBase;
import de.bea.domingo.DBaseItem;
import de.bea.domingo.DDatabase;
import de.bea.domingo.DDocument;
import de.bea.domingo.DItem;
import de.bea.domingo.DNotesMonitor;
import de.bea.domingo.DNotesRuntimeException;
import de.bea.domingo.DView;

/**
 * Http implementation of a Domingo document.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DocumentHttp extends BaseDocumentHttp implements DDocument {

    /** Value for item <tt>tmpSendOptions</tt>: Indicates to only send when sending a document. */
    private static final String SEND = "1";

    /** Value for item <tt>tmpSendOptions</tt>: Indicates to also save when sending a document. */
    private static final String SEND_AND_SAVE = "2";

    /** Value for item <tt>tmpSendOptions</tt>: Indicates to also only save as draft save when sending a document. */
    private static final String SEND_SAVE_AS_DRAFT = "3";

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1484836582323425207L;

    private String fUniversalId;

    private boolean fResponse;

    private boolean fEncryptOnSend;

    private boolean fSaveOnSend;

    private boolean fSignOnSend;

    private String fParentDocumentUNID;

    private boolean fNewNote;

    private boolean fSignOnSave;

    /**
     * Private Constructor for this class.
     *
     * @param factory the controlling factory
     * @param parent the parent object
     * @param unid the universal ID of the document
     * @param monitor the monitor that handles logging
     * @see lotus.domino.Database
     */
    protected DocumentHttp(final NotesHttpFactory factory, final DBase parent, final String unid, final DNotesMonitor monitor) {
        super(factory, parent, unid, monitor);
        this.fUniversalId = unid;
        if (unid != null && unid.length() > 0) {
            readDocument();
        }
        fResponse = hasItem("$Ref");
        fParentDocumentUNID = getItemValueString("$Ref");
        fEncryptOnSend = "1".equals(getItemValueString("Encrypt"));
        fSaveOnSend = "1".equals(getItemValueString("SaveOptions"));
        fSignOnSend = "1".equals(getItemValueString("Sign"));
        fNewNote = false;
    }

    /**
     * Private Constructor for this class.
     *
     * @param factory the controlling factory
     * @param parent the parent object
     * @param monitor the monitor that handles logging
     * @see lotus.domino.Database
     */
    protected DocumentHttp(final NotesHttpFactory factory, final DBase parent, final DNotesMonitor monitor) {
        super(factory, parent, monitor);
        this.fUniversalId = "";
        fNewNote = true;
    }

    /**
     * Factory method for instances of this class.
     *
     * @param theFactory the controlling factory
     * @param theParent the session that produced the database
     * @param unid the universal id of a Notes document
     * @param monitor the monitor that handles logging
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static DDocument getInstance(final NotesHttpFactory theFactory, final DBase theParent, final String unid, final DNotesMonitor monitor) {
        return new DocumentHttp(theFactory, theParent, unid, monitor);
    }

    /**
     * Factory method for instances of this class.
     *
     * @param factory the controlling factory
     * @param parent the session that produced the database
     * @param monitor the monitor that handles logging
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static DDocument getInstance(final NotesHttpFactory factory, final DBase parent, final DNotesMonitor monitor) {
        return new DocumentHttp(factory, parent, monitor);
    }

    private void readDocument() {
        if (getDSession().isDomingoAvailable()) {
            readDocumentByXML();
        } else {
            readDocumentByHtml();
        }
    }

    /**
     * Reads a document in HTML format directly from the Http server.
     */
    private void readDocumentByHtml() {
        BaseHttp parent = getParent();
        String viewName;
        if (parent instanceof DView) {
            viewName = ((DView) parent).getName();
            parent = parent.getParent();
        } else {
            viewName = "0";
        }
        if (!(parent instanceof DDatabase)) {
            throw new NotesHttpRuntimeException("Unsupported parent: " + parent.getClass().getName());
        }
        String databaseName = ((DDatabase) parent).getFileName();
        String url = databaseName + "/" + viewName + "/" + this.fUniversalId + "?EditDocument" + "?OpenDocument";
        try {
            final String bs = executeUrl(url);
        } catch (IOException e) {
            throw new NotesHttpRuntimeException(e);
        }
    }

    /**
     * Reads a document in XML format from the Domingo database.
     */
    private void readDocumentByXML() {
        try {
            final String bs = execute("cmd=ReadDocument&unid=" + fUniversalId);
            final SAXParser parser = getDSession().getFactory().getSAXParserFactory().newSAXParser();
            final DocumentParser documentParser = new DocumentParser();
            parser.parse(new ByteArrayInputStream(bs.getBytes()), documentParser);
        } catch (IOException e) {
            throw new NotesHttpRuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new NotesHttpRuntimeException(e);
        } catch (SAXException e) {
            throw new NotesHttpRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#isNewNote()
     */
    public boolean isNewNote() {
        return fNewNote;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getUniversalID()
     */
    public String getUniversalID() {
        return fUniversalId;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getNoteID()
     */
    public String getNoteID() {
        throw new UnsupportedOperationException("getNoteID()");
    }

    /**
     * {@inheritDoc}
     * @throws IOException
     *
     * @see de.bea.domingo.DBaseDocument#save(boolean, boolean)
     */
    public boolean save(final boolean force, final boolean makeresponse) throws DNotesRuntimeException {
        final String path = getParentDatabase().getFilePath();
        final String args = "&sign=" + fSignOnSave;
        final String object = isNewNote() ? ((DItem) getFirstItem("Form")).getValueString() : getUniversalID();
        final String command = isNewNote() ? "CreateDocument" : "SaveDocument";
        final String pathInfo = path + "/" + object + "?" + command + args;
        final DominoPostMethod method = getDSession().createPostMethod(pathInfo);
        final Iterator itemIterator = getItems();
        while (itemIterator.hasNext()) {
            final DBaseItem item = (DBaseItem) itemIterator.next();
            if (item instanceof DItem) {
                final Iterator valueIterator = ((DItem) item).getValues().iterator();
                while (valueIterator.hasNext()) {
                    addParameter(method, item.getName(), valueIterator.next());
                }
            }
        }
        try {
            final int statusCode = getDSession().executeMethod(method);
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
                getMonitor().error("Http request failed: " + method.getStatusLine());
            }
            return statusCode == HttpStatus.SC_OK;
        } catch (IOException e) {
            getMonitor().error(e.getLocalizedMessage(), e);
            throw new NotesHttpRuntimeException(e.getLocalizedMessage(), e);
        } finally {
            method.releaseConnection();
        }
    }

    private void addParameter(final DominoPostMethod method, final String name, final Object value) {
        if (value instanceof Calendar) {
            method.addParameter(name, value.toString());
        } else if (value instanceof Number) {
            method.addParameter(name, value.toString());
        } else if (value instanceof String) {
            method.addParameter(name, value.toString());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#send(java.lang.String)
     */
    public void send(final String recipient) {
        final List list = new ArrayList();
        list.add(recipient);
        send(list);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#send(java.util.List)
     */
    public void send(final List recipients) {
        replaceItemValue("WebSubject", getItemValue("Subject"));
        replaceItemValue("tmpSendOptions", fSaveOnSend ? SEND_AND_SAVE : SEND);
        replaceItemValue("__Click", getItemValue("0"));
        final String path = getParentDatabase().getFilePath();
        final String args = "&encrypt=" + fEncryptOnSend + "&save=" + fSaveOnSend + "&sign=" + fSignOnSend;
        try {
            String result = execute(path, args);
            getMonitor().debug(result);
        } catch (IOException e) {
            throw new NotesHttpRuntimeException("Cannot save document: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#copyToDatabase(de.bea.domingo.DDatabase)
     */
    public DDocument copyToDatabase(final DDatabase database) {
        throw new UnsupportedOperationException("copyToDatabase()");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#isResponse()
     */
    public boolean isResponse() {
        return fResponse;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getParentDocumentUNID()
     */
    public String getParentDocumentUNID() {
        return fParentDocumentUNID;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getParentDocument()
     */
    public DDocument getParentDocument() {
        return getInstance(getFactory(), getParent(), getParentDocumentUNID(), getMonitor());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#makeResponse(de.bea.domingo.DDocument)
     */
    public void makeResponse(final DDocument doc) {
        throw new UnsupportedOperationException("makeResponse()");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getResponses()
     */
    public Iterator getResponses() {
        throw new UnsupportedOperationException("getResponses()");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#setSaveMessageOnSend(boolean)
     */
    public void setSaveMessageOnSend(final boolean flag) {
        fSaveOnSend = flag;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#setEncryptOnSend(boolean)
     */
    public void setEncryptOnSend(final boolean flag) {
        fEncryptOnSend = flag;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#setSignOnSend(boolean)
     */
    public void setSignOnSend(final boolean flag) {
        fSignOnSend = flag;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#sign()
     */
    public void sign() {
        fSignOnSave = true;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#replaceHTML(java.lang.String,
     *      java.lang.String)
     */
    public void replaceHTML(final String name, final String value) {
        throw new UnsupportedOperationException("replaceHTML()");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getFTSearchScore()
     */
    public int getFTSearchScore() {
        throw new UnsupportedOperationException("getFTSearchScope()");
    }

    /**
     * SAX parser for documents.
     */
    private class DocumentParser extends BaseHandler {

        private String fName;

        private boolean fNames;

        private boolean fReaders;

        private boolean fAuthors;

        public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes atts) throws SAXException {
            if ("item".equals(qName)) {
                fName = atts.getValue("name");
                String n = atts.getValue("names");
                fNames = n != null && n.equals("true");
                String r = atts.getValue("readers");
                fReaders = r != null && r.equals("true");
                String a = atts.getValue("authors");
                fAuthors = a != null && a.equals("true");
                reset();
            } else {
                super.startElement(namespaceURI, localName, qName, atts);
            }
        }

        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            if ("item".equals(qName)) {
                final DItem item = replaceItemValue(fName, getValues());
                item.setNames(fNames);
                item.setReaders(fReaders);
                item.setAuthors(fAuthors);
            } else {
                super.endElement(uri, localName, qName);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getParentView()
     */
    public DView getParentView() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getFolderReferences()
     */
    public List getFolderReferences() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#putInFolder(java.lang.String)
     */
    public void putInFolder(final String s) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#putInFolder(java.lang.String, boolean)
     */
    public void putInFolder(final String s, final boolean flag) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#removeFromFolder(java.lang.String)
     */
    public void removeFromFolder(final String s) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getURL()
     */
    public String getURL() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getNotesURL()
     */
    public String getNotesURL() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.DDocument#getHttpURL()
     */
    public String getHttpURL() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }
}
