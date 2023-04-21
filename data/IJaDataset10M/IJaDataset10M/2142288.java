package org.powerfolder.workflow.model.script;

import java.io.Serializable;
import java.util.ArrayList;

public class InitializeScriptTagErrorHolder implements Serializable {

    private ArrayList errorDescriptions = null;

    InitializeScriptTagErrorHolder() {
        this.errorDescriptions = new ArrayList();
    }

    public void registerError(String inDescription) {
        this.errorDescriptions.add(inDescription);
    }

    public int getErrorCount() {
        return this.errorDescriptions.size();
    }

    public String getErrorDescription(int inIndex) {
        return (String) this.errorDescriptions.get(inIndex);
    }
}
