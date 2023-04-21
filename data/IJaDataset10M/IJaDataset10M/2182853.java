package com.memoire.vainstall.uui;

import java.io.*;
import com.memoire.vainstall.VAGlobals;
import com.memoire.vainstall.VAStep;
import com.memoire.vainstall.VAWizardInterface;

public class UuiWizard implements VAWizardInterface {

    private static UuiWizard UNIQUE_WIZARD = null;

    private VAStep step_;

    private int actions_;

    protected UuiWizard() {
        super();
        init();
    }

    protected void init() {
    }

    public static VAWizardInterface createWizard() {
        if (UNIQUE_WIZARD == null) UNIQUE_WIZARD = new UuiWizard(); else UNIQUE_WIZARD.init();
        return UNIQUE_WIZARD;
    }

    public void setActionEnabled(int _actions) {
        actions_ = _actions;
    }

    public void setStep(VAStep _step) {
        step_ = _step;
    }

    public void show() {
        while (true) {
            step_.nextAction();
        }
    }

    public void dispose(boolean _confirm) {
    }
}
