package com.wumpus.projects.tellthem.elements;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

public class DocumentList implements Serializable, Cloneable {

    private static final long serialVersionUID = 1;

    private Vector<Document> documents = null;

    public DocumentList() {
        documents = new Vector<Document>();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        DocumentList dl = new DocumentList();
        Iterator<Document> di = this.getDocuments();
        while (di.hasNext()) {
            dl.addDocument((Document) di.next().clone());
        }
        return dl;
    }

    public void addDocument(Document aDocument) {
        documents.add(aDocument);
    }

    public Iterator<Document> getDocuments() {
        return documents.iterator();
    }

    public int size() {
        return documents.size();
    }
}
