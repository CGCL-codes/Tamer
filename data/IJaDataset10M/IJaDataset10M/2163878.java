package com.citep.web.admin.transactions.client;

import com.citep.web.gwt.module.CitepModule;
import com.citep.web.gwt.module.Presentation;

public class TransactionEntryModule implements CitepModule {

    static TransactionEntryModule singleton;

    protected TransactionEntryPresentation presentation;

    protected TransactionEntryApplication application;

    public static TransactionEntryModule getInstance() {
        if (singleton == null) singleton = new TransactionEntryModule();
        return singleton;
    }

    protected TransactionEntryModule() {
        presentation = new TransactionEntryPresentation();
        application = new TransactionEntryApplication();
        presentation.setApplication(application);
        application.setPresentation(presentation);
        application.refreshEntryTypes();
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public TransactionEntryApplication getApplication() {
        return application;
    }

    public void init(Object param) {
        application.refreshAccountList();
        if (param != null) {
            int accountId = ((Integer) param).intValue();
            application.openAccount(accountId);
        }
    }

    public void onHide() {
    }

    public void onShow() {
    }
}
