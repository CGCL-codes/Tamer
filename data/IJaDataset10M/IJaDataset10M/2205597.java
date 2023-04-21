package com.cromoteca.meshcms.client.ui.filemanager;

import com.cromoteca.meshcms.client.core.MeshCMS;
import com.cromoteca.meshcms.client.toolbox.Path;
import com.cromoteca.meshcms.client.ui.filemanager.FileManager.SelectionHandler;
import com.cromoteca.meshcms.client.ui.widgets.Popup;

public abstract class FileManagerDialogBox extends Popup implements SelectionHandler {

    private FileManager fileManager;

    public FileManagerDialogBox() {
        super(800, 800);
        setText(MeshCMS.CONSTANTS.homeFile());
        buildLayout();
    }

    private void buildLayout() throws UnsupportedOperationException {
        fileManager = new FileManager(new DetailedList(), this);
        setWidget(fileManager);
    }

    public abstract void handleSelection(Path path);

    public void close() {
        hide();
    }
}
