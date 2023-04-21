package com.tomczarniecki.s3.gui;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.concurrent.Executor;

class DeleteObjectAction extends AbstractAction {

    private final Controller controller;

    private final Executor executor;

    private final Display display;

    public DeleteObjectAction(Controller controller, Display display, Executor executor) {
        super("Delete File");
        this.controller = controller;
        this.executor = executor;
        this.display = display;
    }

    public void actionPerformed(ActionEvent e) {
        if (controller.isObjectSelected() && confirmDeletion()) {
            deleteObject();
        }
    }

    private void deleteObject() {
        executor.execute(new Runnable() {

            public void run() {
                controller.deleteCurrentObject();
            }
        });
    }

    private boolean confirmDeletion() {
        String text = "Are you sure that you want to delete file %s from folder %s?\nYou will not be able to undo this action.";
        String message = String.format(text, controller.getSelectedObjectKey(), controller.getSelectedBucketName());
        return display.confirmMessage("Just Checking", message);
    }
}
