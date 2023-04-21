package com.citep.formats.input.transactions;

public interface StatementImportSink {

    public void onImportStatement(ImportedStatement statement);

    public void onDocumentEnd();
}
