package org.simfony.database;

import org.simfony.StrictDocument;
import org.simfony.document.SimpleDocument;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * MemoryDatabase stores all documents in-memory. Only Document data is stored
 * in the database but not the original Document object.
 * <p>
 * Only one copy of the same document can be stored in the database.
 * <p>
 * Default MemoryDatabase storage is ArrayList.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public class MemoryDatabase implements Database, View {

    protected static final int DOCUMENT_ADDED = 0;

    protected static final int DOCUMENT_REMOVED = 1;

    List _documents;

    List _listeners;

    /**
    * Creates MemoryDatabase with no documents. Default storage is used.
    */
    public MemoryDatabase() {
        this(null);
    }

    /**
    * Creates MemoryDatabase with no documents. Specified list is used as
    * document storage. Default storage is used if no list is specified (null).
    * List is emptied before usage.
    *
    * @param list List where documents are stored.
    */
    public MemoryDatabase(List list) {
        if (list == null) {
            list = new ArrayList();
        } else {
            list.clear();
        }
        _documents = list;
    }

    /**
    * Adds DatabaseListener to the list of this database listeners.
    *
    * @param listener DatabaseListener to add.
    */
    public void addListener(DatabaseListener listener) {
        if (_listeners == null) {
            _listeners = new ArrayList();
        }
        _listeners.add(listener);
    }

    /**
    * Finds document in this database which is equal to the specified document.
    *
    * @return document in this database or null if no equal document found.
    */
    private StrictDocument findDocument(StrictDocument document) {
        for (Iterator i = getDocuments(); i.hasNext(); ) {
            MemoryDocument m_document = (MemoryDocument) i.next();
            if (m_document.equals(document)) {
                return m_document;
            }
        }
        return null;
    }

    /**
    * Returns iterator over all documents in the database.
    *
    * @return iterator over all documents in the database
    */
    public Iterator getDocuments() {
        return _documents.iterator();
    }

    /**
    * Informs listeners on Document change in the database.
    *
    * @param type Document change type.
    * @param document Document which has changed.
    */
    protected void informListeners(int type, StrictDocument document) {
        if (_listeners == null) {
            return;
        }
        if (type == DOCUMENT_ADDED) {
            for (Iterator i = _listeners.iterator(); i.hasNext(); ) {
                DatabaseListener listener = (DatabaseListener) i.next();
                listener.documentAdded(this, document);
            }
        } else if (type == DOCUMENT_REMOVED) {
            for (Iterator i = _listeners.iterator(); i.hasNext(); ) {
                DatabaseListener listener = (DatabaseListener) i.next();
                listener.documentRemoved(this, document);
            }
        }
    }

    /**
    * Loads (stores) documents from the view into this database.
    *
    * @param View View containing documents to load.
    */
    public void loadDocuments(View view) {
        if (view == null) {
            return;
        }
        for (Iterator i = view.getDocuments(); i.hasNext(); ) {
            setDocument((StrictDocument) i.next());
        }
    }

    /**
    * Removes document from the Database.
    *
    * @param document Document to remove.
    */
    public void removeDocument(StrictDocument document) {
        document = findDocument(document);
        if (document != null) {
            _documents.remove(document);
            informListeners(DOCUMENT_REMOVED, document);
        }
    }

    /**
    * Removes specified listener from the list of this database listeners.
    *
    * @param listener DatabaseListener to remove.
    */
    public void removeListener(DatabaseListener listener) {
        if (_listeners != null && listener != null) {
            _listeners.remove(listener);
        }
    }

    /**
    * Stores document in the database.
    *
    * @param document Document to store.
    * @throws DatabaseException if error occured when saving document.
    */
    public void setDocument(StrictDocument document) throws DatabaseException {
        document = findDocument(document);
        if (document == null) {
            document = new MemoryDocument(document);
            _documents.add(document);
            informListeners(DOCUMENT_ADDED, document);
        }
    }

    class MemoryDocument extends SimpleDocument {

        MemoryDocument(StrictDocument document) {
            for (Iterator i = document.getAttributeNames(); i.hasNext(); ) {
                String name = (String) i.next();
                addValues(name, document.getValues(name));
            }
        }

        /**
       * Returns if this document has the same attributes and values set.
       *
       * @param   obj   the reference object with which to compare.
       * @return  <code>true</code> if this object is the same as the obj
       *          argument; <code>false</code> otherwise.
       */
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof StrictDocument)) {
                return false;
            }
            StrictDocument document = (StrictDocument) obj;
            if (document.getSize() != getSize()) {
                return false;
            }
            for (Iterator i = getAttributeNames(); i.hasNext(); ) {
                String name = (String) i.next();
                Object[] v1 = getValues(name);
                Object[] v2 = getValues(name);
                if (v1 != v2) {
                    if (v1 == null || v2 == null) {
                        return false;
                    }
                    if (v1.length != v2.length) {
                        return false;
                    }
                    for (int j = 0; j < v1.length; j++) {
                        if (v1[j] != null && !v1.equals(v2)) {
                            return false;
                        } else if (v2[j] != null) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
}
