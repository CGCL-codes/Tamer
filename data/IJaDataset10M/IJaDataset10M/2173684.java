package com.memomics.cytoscape_plugin.tasks;

import java.io.File;
import com.alitora.asapi.publicModel.ASAPI_Response;
import com.alitora.asapi.publicModel.ASAPI_Status;
import com.memomics.cytoscape_plugin.dialogs.AnnotateDialog;
import com.memomics.cytoscape_plugin.model.Application;
import cytoscape.task.Task;
import cytoscape.task.TaskMonitor;

public class UploadFileTask implements Task {

    private static final long serialVersionUID = -5769255813455829766L;

    private TaskMonitor taskMonitor;

    private boolean isInterrupted;

    private File file;

    private AnnotateDialog parentDialog;

    public UploadFileTask(AnnotateDialog parent, File file) {
        this.parentDialog = parent;
        this.file = file;
    }

    public String getTitle() {
        return "Uploading file \"" + file.getAbsolutePath() + "\"";
    }

    public void halt() {
        isInterrupted = true;
    }

    public void run() {
        taskMonitor.setPercentCompleted(-1);
        taskMonitor.setStatus("Checking file...");
        if (!file.canRead()) {
            taskMonitor.setStatus("File can't be read.");
            return;
        }
        taskMonitor.setStatus("Uploading file...");
        ASAPI_Response fileUploadStatus = Application.get().uploadFile(file);
        if (fileUploadStatus.getStatusCode().equals(ASAPI_Status.STATUS_OK)) {
            taskMonitor.setPercentCompleted(100);
            taskMonitor.setStatus("File successfully uploaded.");
            parentDialog.setFileToken((String) fileUploadStatus.getFirstObject());
            parentDialog.refresh();
        } else {
            taskMonitor.setPercentCompleted(0);
            taskMonitor.setStatus("Error uploading file: " + fileUploadStatus.getStatusCode());
        }
    }

    public void setTaskMonitor(TaskMonitor arg0) throws IllegalThreadStateException {
        this.taskMonitor = arg0;
    }
}
