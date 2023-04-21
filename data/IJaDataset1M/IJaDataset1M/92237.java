package org.omnidoc.process;

import org.omnidoc.Document;

public interface DocumentSpace extends Iterable<Document> {

    public void addDocument(Document doc);

    public void removeDocument(Document doc);

    public void copyTo(DocumentSpace target);
}
