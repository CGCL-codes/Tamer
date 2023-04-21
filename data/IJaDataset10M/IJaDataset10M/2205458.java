package com.citep.formats.input.transactions;

import com.citep.formats.input.ImportException;
import java.io.InputStream;

public abstract class TransactionImportStrategy {

    protected InputStream input;

    public TransactionImportStrategy(InputStream stream) {
        this.input = stream;
    }

    public abstract void setStatementHandler(StatementImportSink handler);

    public abstract void start() throws ImportException;
}
