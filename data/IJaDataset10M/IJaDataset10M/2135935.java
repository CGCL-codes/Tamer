package org.opendicomviewer.controller;

import org.opendicomviewer.ApplicationContext;

public class ControllerContext {

    private ApplicationContext applicationContext;

    public ControllerContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private FileController fileController;

    public FileController getFileController() {
        if (fileController == null) {
            fileController = new FileController(applicationContext);
        }
        return fileController;
    }

    private EditController editController;

    public EditController getEditController() {
        if (editController == null) {
            editController = new EditController(applicationContext);
        }
        return editController;
    }

    private HelpController helpController;

    public HelpController getHelpController() {
        if (helpController == null) {
            helpController = new HelpController(applicationContext);
        }
        return helpController;
    }

    private MiscellaneousController miscellaneousController;

    public MiscellaneousController getMiscellaneousController() {
        if (miscellaneousController == null) {
            miscellaneousController = new MiscellaneousController();
        }
        return miscellaneousController;
    }
}
