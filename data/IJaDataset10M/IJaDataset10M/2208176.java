package com.luxoft.fitpro.htmleditor.plugin.htmleditor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import com.luxoft.fitpro.htmleditor.io.IFileSystem;
import com.luxoft.fitpro.htmleditor.plugin.messages.PluginMessages;

public class FileSelector implements IFileSelector {

    private final Shell shell;

    private IFileSystem fileSystem;

    public FileSelector(Shell shell, IFileSystem fileSystem) {
        this.shell = shell;
        this.fileSystem = fileSystem;
    }

    public String selectFixtureFile(String location) {
        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
        fileDialog.setText(PluginMessages.getMessage("htmleditor.select_fit_fixture"));
        fileDialog.setFilterPath(location);
        fileDialog.setFilterExtensions(new String[] { "*" + fileSystem.getFixtureExtension() });
        return fileDialog.open();
    }

    public IFileSystem getFileSystemAdapter() {
        return fileSystem;
    }
}
