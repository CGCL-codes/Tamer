package org.rubypeople.rdt.refactoring.core.formatsource;

import org.rubypeople.rdt.refactoring.core.IRefactoringConfig;
import org.rubypeople.rdt.refactoring.documentprovider.DocumentProvider;
import org.rubypeople.rdt.refactoring.documentprovider.IDocumentProvider;

public class FormatSourceConfig implements IRefactoringConfig {

    private IDocumentProvider documentProvider;

    public FormatSourceConfig(DocumentProvider documentProvider) {
        this.documentProvider = documentProvider;
    }

    public IDocumentProvider getDocumentProvider() {
        return documentProvider;
    }

    public void setDocumentProvider(IDocumentProvider doc) {
        this.documentProvider = doc;
    }
}
