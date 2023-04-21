package gridunit.report.document.dom.xml;

import gridunit.report.document.TestResultDocument;
import gridunit.report.document.TestResultDocumentBuilder;

/**
 * @author Alexandro de Souza Soares - alexandro@lsd.ufcg.edu.br
 * 
 * Description: TODO Description for XMLTestResultDocumentBuilder.java
 * 
 * @version 1.0 Date: 12/09/2005
 * 
 * Copyright (c) 2002-2005 Universidade Federal de Campina Grande
 */
public class XMLTestResultDocumentBuilder implements TestResultDocumentBuilder {

    /**
     * A TestResultDocument.
     */
    private TestResultDocument document;

    protected TestResultDocument createDocument() {
        return new XMLTestResultDocument();
    }

    public void build() {
        document = createDocument();
    }

    public TestResultDocument getDocument() {
        return document;
    }
}
