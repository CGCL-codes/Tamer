package com.wle.client.gui.action.file;

import com.google.gwt.core.client.GWT;
import com.wle.client.core.api.ClientAPIService;
import com.wle.client.core.api.LatexFile;
import com.wle.client.gui.action.ActionBase;
import com.wle.client.gui.customgwt.edit.LatexTabPanel;

public class SaveCurrentFileAction extends ActionBase {

    private static SaveCurrentFileAction singleton = new SaveCurrentFileAction();

    public static SaveCurrentFileAction getInstance() {
        return singleton;
    }

    private SaveCurrentFileAction() {
    }

    public void execute() {
        GWT.log("save current file", null);
        LatexFile currentFile = LatexTabPanel.getInstance().getCurrentFile();
        if (currentFile != null) {
            ClientAPIService.getInstance().saveDocument(currentFile);
        }
    }
}
