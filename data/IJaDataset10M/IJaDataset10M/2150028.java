package com.yubuild.coreman.lucene;

import com.yubuild.coreman.data.Document;
import com.yubuild.coreman.data.SubDocument;
import com.yubuild.coreman.lucene.exception.FileHandlerException;

public interface FileHandler {

    public abstract org.apache.lucene.document.Document getDocument(Document document) throws FileHandlerException;

    public abstract org.apache.lucene.document.Document getDocument(SubDocument subdocument) throws FileHandlerException;
}
