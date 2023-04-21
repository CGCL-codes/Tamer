package com.common.search;

import org.apache.lucene.document.Document;
import com.common.search.exeption.DocumentHandlerException;

public interface DocumentHandler {

    public Document getDocument() throws DocumentHandlerException;

    public static final String ID = "id";
}
