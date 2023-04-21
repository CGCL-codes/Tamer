package org.monet.kernel.model;

import org.monet.kernel.components.ComponentPersistence;

public class HistoryStoreProvider implements HistoryStoreLink {

    private ComponentPersistence componentPersistence;

    private static HistoryStoreProvider instance;

    private HistoryStoreProvider() {
        this.componentPersistence = ComponentPersistence.getInstance();
    }

    public static synchronized HistoryStoreProvider getInstance() {
        if (instance == null) instance = new HistoryStoreProvider();
        return instance;
    }

    @Override
    public TermList getTerms(String codeThesaurus, DataRequest dataRequest) {
        return this.componentPersistence.getHistoryStoreTerms(codeThesaurus, dataRequest);
    }

    @Override
    public void addTerm(String codeThesaurus, String code, String value) {
        this.componentPersistence.addHistoryStoreTerm(codeThesaurus, code, value);
    }
}
