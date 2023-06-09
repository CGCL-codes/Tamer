package org.apache.poi.poifs.filesystem;

import java.util.*;
import org.apache.poi.poifs.dev.POIFSViewable;
import org.apache.poi.poifs.property.DocumentProperty;

/**
 * Simple implementation of DocumentEntry
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */
public class DocumentNode extends EntryNode implements DocumentEntry, POIFSViewable {

    private POIFSDocument _document;

    /**
     * create a DocumentNode. This method is not public by design; it
     * is intended strictly for the internal use of this package
     *
     * @param property the DocumentProperty for this DocumentEntry
     * @param parent the parent of this entry
     */
    DocumentNode(final DocumentProperty property, final DirectoryNode parent) {
        super(property, parent);
        _document = property.getDocument();
    }

    /**
     * get the POIFSDocument
     *
     * @return the internal POIFSDocument
     */
    POIFSDocument getDocument() {
        return _document;
    }

    /**
     * get the zize of the document, in bytes
     *
     * @return size in bytes
     */
    public int getSize() {
        return getProperty().getSize();
    }

    /**
     * is this a DocumentEntry?
     *
     * @return true if the Entry is a DocumentEntry, else false
     */
    public boolean isDocumentEntry() {
        return true;
    }

    /**
     * extensions use this method to verify internal rules regarding
     * deletion of the underlying store.
     *
     * @return true if it's ok to delete the underlying store, else
     *         false
     */
    protected boolean isDeleteOK() {
        return true;
    }

    /**
     * Get an array of objects, some of which may implement
     * POIFSViewable
     *
     * @return an array of Object; may not be null, but may be empty
     */
    public Object[] getViewableArray() {
        return new Object[0];
    }

    /**
     * Get an Iterator of objects, some of which may implement
     * POIFSViewable
     *
     * @return an Iterator; may not be null, but may have an empty
     * back end store
     */
    public Iterator getViewableIterator() {
        List components = new ArrayList();
        components.add(getProperty());
        components.add(_document);
        return components.iterator();
    }

    /**
     * Give viewers a hint as to whether to call getViewableArray or
     * getViewableIterator
     *
     * @return true if a viewer should call getViewableArray, false if
     *         a viewer should call getViewableIterator
     */
    public boolean preferArray() {
        return false;
    }

    /**
     * Provides a short description of the object, to be used when a
     * POIFSViewable object has not provided its contents.
     *
     * @return short description
     */
    public String getShortDescription() {
        return getName();
    }
}
