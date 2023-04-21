package ru.ksu.niimm.cll.mocassin.search;

import java.io.InputStream;

public interface PDFIndexer {

    String BLANK_QUERY_PATTERN = "\"%s\"";

    /**
	 * parse a given PDF stream and save into the index
	 * 
	 * @param pdfInputStream
	 */
    void save(String pdfDocumentUri, InputStream pdfInputStream) throws PersistingDocumentException;

    /**
	 * 
	 * returns the number of a page that contains a segment corresponding a
	 * given full text query. Searching is performed within a document with
	 * given URI
	 * 
	 * @param pdfDocumentUri
	 * @param fullTextQuery
	 * @return
	 * @throws EmptyResultException
	 */
    int getPageNumber(String pdfDocumentUri, String fullTextQuery) throws EmptyResultException;
}
