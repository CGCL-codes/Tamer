package com.google.jguery.client.index;

import com.google.jguery.client.analyzer.Analyzer;

public class Index {

    Table termTable;

    TermReader termReader;

    public Index(Table termTable) {
        this.termTable = termTable;
        termReader = new TermReader(termTable);
    }

    public DocReader getDocReader() {
        return null;
    }

    public TermReader getTermReader() {
        return termReader;
    }

    public void update(int docNum, String text, Analyzer analyzer) {
        termTable.update(docNum, text, analyzer);
    }
}
