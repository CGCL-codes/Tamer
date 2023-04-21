package gridunit.report;

import gridunit.report.document.TestResultDocumentBuilder;

/**
 * @author Alexandro de Souza Soares - alexandro@lsd.ufcg.edu.br
 * 
 * Description: TODO Description for Report.java
 * 
 * @version 1.0 Date: 12/09/2005
 * 
 * Copyright (c) 2002-2005 Universidade Federal de Campina Grande
 */
public class ReportConstructor {

    /**
     * Constructs a test result document.
     * @param builder a test result document builder.
     */
    public void construct(TestResultDocumentBuilder builder) {
        builder.build();
    }
}
