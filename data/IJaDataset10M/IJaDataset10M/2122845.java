package com.volantis.mcs.protocols.widgets.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wizard widget attributes.
 */
public final class WizardAttributes extends WidgetAttributes {

    /**
     * List of attibutes of launchers elements  
     */
    private final ArrayList launchers = new ArrayList();

    /**
     * indicate to popup cancel widget id attrobute defined by author   
     */
    private String cancelDialog;

    public List getLaunchers() {
        return Collections.unmodifiableList(launchers);
    }

    public void addLaunch(LaunchAttributes attrs) {
        launchers.add(attrs);
    }

    public String getCancelDialog() {
        return cancelDialog;
    }

    public void setCancelDialog(String cancelDialog) {
        this.cancelDialog = cancelDialog;
    }
}
