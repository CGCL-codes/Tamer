package org.dcm4chee.docstore.spi;

public abstract class DocumentStorageProviderSPI {

    public abstract String getStorageType();

    public abstract DocumentStorage getDocumentStorage();

    public abstract DocumentStorage getDocumentStorage(String name);

    public abstract DocumentStorage detachDocumentStorage(String name);
}
