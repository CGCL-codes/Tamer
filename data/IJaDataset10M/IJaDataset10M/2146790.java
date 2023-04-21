package com.cromoteca.meshcms.client.ui.filemanager;

import com.cromoteca.meshcms.client.core.MeshCMS;
import com.cromoteca.meshcms.client.icons.FileTypesWithIcons;
import com.cromoteca.meshcms.client.server.CMSDirectoryItem;
import com.cromoteca.meshcms.client.server.FileInfo;
import com.cromoteca.meshcms.client.toolbox.Path;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import java.util.ArrayList;
import java.util.List;

public class DetailedList extends FlexTable implements FileList, ClickHandler {

    private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);

    private FileManager fileManager;

    private List<FileInfo> files;

    public DetailedList() {
        setStylePrimaryName("mesh-detailed-file-list");
        setWidth("95%");
        addClickHandler(this);
        setCellPadding(0);
        setCellSpacing(0);
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void onClick(ClickEvent event) {
        Cell cell = getCellForEvent(event);
        int row = cell.getRowIndex();
        RowFormatter rowFormatter = getRowFormatter();
        if (rowFormatter.getStyleName(row).contains(STYLE_SELECTED)) {
            rowFormatter.removeStyleName(row, STYLE_SELECTED);
        } else {
            rowFormatter.addStyleName(row, STYLE_SELECTED);
        }
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
        populateTable();
    }

    public void selectAll() {
        RowFormatter rowFormatter = getRowFormatter();
        for (int i = 0; i < getRowCount(); i++) {
            if (!rowFormatter.getStyleName(i).contains(STYLE_SELECTED)) {
                rowFormatter.addStyleName(i, STYLE_SELECTED);
            }
        }
    }

    public void selectNone() {
        RowFormatter rowFormatter = getRowFormatter();
        for (int i = 0; i < getRowCount(); i++) {
            if (rowFormatter.getStyleName(i).contains(STYLE_SELECTED)) {
                rowFormatter.removeStyleName(i, STYLE_SELECTED);
            }
        }
    }

    public void invertSelection() {
        RowFormatter rowFormatter = getRowFormatter();
        for (int i = 0; i < getRowCount(); i++) {
            if (rowFormatter.getStyleName(i).contains(STYLE_SELECTED)) {
                rowFormatter.removeStyleName(i, STYLE_SELECTED);
            } else {
                rowFormatter.addStyleName(i, STYLE_SELECTED);
            }
        }
    }

    public List<FileInfo> getSelectedFiles() {
        List<FileInfo> selectedFiles = new ArrayList<FileInfo>();
        RowFormatter rowFormatter = getRowFormatter();
        for (int i = 0; i < getRowCount(); i++) {
            if (rowFormatter.getStyleName(i).contains(STYLE_SELECTED)) {
                selectedFiles.add(files.get(i));
            }
        }
        return selectedFiles;
    }

    public FileInfo getSelectedFile() {
        List<FileInfo> selectedFiles = getSelectedFiles();
        return selectedFiles.size() == 1 ? selectedFiles.get(0) : null;
    }

    private void populateTable() {
        while (getRowCount() > 0) {
            removeRow(0);
        }
        for (FileInfo fileInfo : files) {
            ImageResource image;
            String label;
            if (fileInfo.isDirectory()) {
                CMSDirectoryItem cmsDirectoryItem = MeshCMS.SITE_INFO.getCMSDirectoryItem(fileInfo);
                image = cmsDirectoryItem.getIcon();
                label = cmsDirectoryItem.getLabel();
            } else {
                image = FileTypesWithIcons.getIconFile(fileInfo);
                label = fileInfo.getName();
            }
            int n = getRowCount();
            setWidget(n, 0, new Image(image));
            if (fileManager.isBrowser() || fileInfo.getPageInfo() == null) {
                setWidget(n, 1, new Label(label, false));
            } else {
                Path p = fileInfo.getPageInfo().isWelcome() ? fileInfo.getPath().getParent() : fileInfo.getPath();
                setWidget(n, 1, new HTML("<a href=\"" + MeshCMS.CONTEXT_PATH + p.asAbsolute() + "\" target=\"_top\">" + label + "</a>"));
            }
            setWidget(n, 2, new Label(fileInfo.isDirectory() ? "" : fileInfo.getLength(), false));
            setWidget(n, 3, new Label(dateTimeFormat.format(fileInfo.getLastModified()), false));
        }
    }

    public DetailedList asWidget() {
        return this;
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public ImageResource getIcon() {
        return MeshCMS.ICONS_BUNDLE.documentsStack();
    }
}
