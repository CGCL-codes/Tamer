package com.luxoft.fitpro.plugin.editors.suite.controller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import com.luxoft.fitpro.plugin.editors.suite.ui.ErrorMessage;
import com.luxoft.fitpro.plugin.messages.PluginMessages;

public class DialogManager {

    private Shell shell;

    public DialogManager(Shell shell) {
        super();
        this.shell = shell;
    }

    public String[] getMultiplePaths(String filterPath, String[] filterExtensions) {
        FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN | SWT.MULTI);
        fileDialog.setText(PluginMessages.getMessage("fit.runner.fit_file_selection"));
        fileDialog.setFilterPath(filterPath);
        fileDialog.setFilterExtensions(filterExtensions);
        fileDialog.open();
        IPath basePath = new Path(fileDialog.getFilterPath());
        String[] relativePaths = fileDialog.getFileNames();
        int nFiles = relativePaths == null ? 0 : relativePaths.length;
        String[] fullPaths = new String[nFiles];
        for (int i = 0; i < nFiles; i++) {
            fullPaths[i] = basePath.append(relativePaths[i]).makeAbsolute().toString();
        }
        return fullPaths;
    }

    public String getFilePath(String filterPath, String[] filterExtensions) {
        FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
        fileDialog.setText(PluginMessages.getMessage("fit.runner.fit_file_selection"));
        fileDialog.setFilterPath(filterPath);
        fileDialog.setFilterExtensions(filterExtensions);
        String fullFilePath = fileDialog.open();
        return fullFilePath;
    }

    public void reportCyclicDependency(String baseDirPath, String fullFilePath) {
        ErrorMessage message = new ErrorMessage(getShell());
        message.openDialog(PluginMessages.getMessage("fit.runner.fit_suite_editor_error"), PluginMessages.getMessage("fit.runner.you_are_attempting_to_create_a_cyclic_depend_by_adding{0}to{1}", fullFilePath, baseDirPath));
    }

    public void reportPreexistingCyclicDependency(String fullFilePath) {
        ErrorMessage message = new ErrorMessage(getShell());
        message.openDialog(PluginMessages.getMessage("fit.runner.fit_suite_editor_error"), PluginMessages.getMessage("fit.runner.{0}contains_a_circular_reference", fullFilePath));
    }

    public void reportUnsupportedFIleExtensions() {
        ErrorMessage message = new ErrorMessage(getShell());
        message.openDialog(PluginMessages.getMessage("fit.runner.fit_suite_editor_error"), PluginMessages.getMessage("fit.runner.only_fit_and_suite_files_may_be_added_to_a_fit_suite"));
    }

    public Shell getShell() {
        return shell;
    }
}
