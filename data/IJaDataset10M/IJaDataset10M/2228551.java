package org.dengues.commons.ui.swt.viewers;

import org.dengues.commons.utils.FileUtils;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 1 2006-09-29 17:06:40Z qiang.zhang $
 * 
 */
public class FileCellEditor extends DialogCellEditor {

    private String orgPath;

    private String[] filters;

    /**
     * Qiang.Zhang.Adolf@gmail.com FileCellEditor constructor comment.
     * 
     * @param parent
     */
    public FileCellEditor(Composite parent) {
        super(parent);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com FileCellEditor constructor comment.
     * 
     * @param parent
     * @param orgPath
     */
    public FileCellEditor(Composite parent, String orgPath) {
        this(parent);
        this.orgPath = orgPath;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com FileCellEditor constructor comment.
     * 
     * @param parent
     * @param orgPath
     * @param filters
     */
    public FileCellEditor(Composite parent, String orgPath, String[] filters) {
        this(parent);
        this.orgPath = orgPath;
        this.filters = filters;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com FileCellEditor constructor comment.
     * 
     * @param parent
     * @param style
     */
    public FileCellEditor(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        FileDialog dialog = new FileDialog(cellEditorWindow.getShell());
        if (orgPath != null && !"".equals(orgPath)) {
            dialog.setFileName(FileUtils.getOSPath(orgPath));
        }
        if (filters != null && filters.length > 0) {
            dialog.setFilterExtensions(filters);
        }
        String path = dialog.open();
        if (path != null) {
            path = FileUtils.getPortablePath(path);
        }
        return path;
    }
}
