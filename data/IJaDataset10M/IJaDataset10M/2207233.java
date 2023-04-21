package org.systemsbiology.apps.gui.client.widget.project;

import org.systemsbiology.apps.gui.client.constants.FileType;
import org.systemsbiology.apps.gui.client.constants.PipelineStep;
import org.systemsbiology.apps.gui.client.controller.IController;
import org.systemsbiology.apps.gui.client.controller.IRequestErrorHandler;
import org.systemsbiology.apps.gui.client.controller.request.FileImportRequest;
import org.systemsbiology.apps.gui.client.data.project.IProjectModel;
import org.systemsbiology.apps.gui.client.widget.WidgetsMediator;
import org.systemsbiology.apps.gui.client.widget.fileselector.IFileListDisplayer;
import org.systemsbiology.apps.gui.client.widget.general.BrowseButton;
import com.google.gwt.user.client.ui.HTMLTable;

public class FileImportWidget implements IRequestErrorHandler, IFileListDisplayer {

    private BrowseButton importButton;

    private IController controller;

    private FileType fileType;

    private final IProjectModel projModel;

    private final PipelineStep step;

    private final WidgetsMediator mediator;

    public FileImportWidget(IProjectModel projModel, PipelineStep step, FileType fileType, String title, HTMLTable table, WidgetsMediator mediator, int row) {
        this.fileType = fileType;
        this.projModel = projModel;
        this.step = step;
        this.mediator = mediator;
        importButton = new BrowseButton(this, title);
        table.setWidget(row, 1, importButton);
    }

    public void setController(IController controller) {
        this.controller = controller;
        importButton.setController(controller);
    }

    public void setEditable(boolean editable) {
        importButton.setEnabled(editable);
    }

    public void setVisible(boolean visible) {
        importButton.setVisible(visible);
    }

    /**
	 * Hides the FileBrowserDialog (if visible) associated with this widget.
	 */
    public void reset() {
        importButton.reset();
    }

    public void handleRequestFailed(Throwable caught) {
        mediator.propagateException(caught);
    }

    public void addFile(String file, boolean notify) {
        if (file != null && file.length() > 0) controller.handleRequest(new FileImportRequest(projModel.getProjectName(), step, fileType, file), FileImportWidget.this);
    }

    public void addFiles(String[] files, boolean notify) {
        if (files != null && files.length > 0) addFile(files[0], notify);
    }

    public void clearFiles() {
    }

    public FileType getFileType() {
        return (this.fileType == null) ? FileType.ALL_FILES : this.fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public void removeFile(String file, boolean notify) {
    }

    public void removeFiles(String[] files, boolean notify) {
    }
}
