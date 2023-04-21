package com.safi.workshop.sqlexplorer.sqleditor.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import com.safi.workshop.sqlexplorer.Messages;
import com.safi.workshop.sqlexplorer.plugin.SQLExplorerPlugin;
import com.safi.workshop.sqlexplorer.plugin.editors.SQLEditor;
import com.safi.workshop.sqlexplorer.util.ImageUtil;

public class OpenFileAction extends AbstractEditorAction {

    private ImageDescriptor img = ImageUtil.getDescriptor("Images.OpenFileIcon");

    public OpenFileAction(SQLEditor editor) {
        super(editor);
    }

    @Override
    public String getText() {
        return Messages.getString("Open_1");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void run() {
        FileDialog dlg = new FileDialog(_editor.getSite().getShell(), SWT.OPEN | SWT.MULTI);
        dlg.setFilterExtensions(new String[] { "*.sql;*.txt" });
        String path = dlg.open();
        if (path != null) {
            String[] files = dlg.getFileNames();
            loadFiles(files, dlg.getFilterPath());
        }
    }

    /**
   * Load one or more files into the editor.
   * 
   * @param files
   *          string[] of relative file paths
   * @param filePath
   *          path where all files are found
   */
    public void loadFiles(String[] files, String filePath) {
        BufferedReader reader = null;
        try {
            StringBuffer all = new StringBuffer();
            String str = null;
            for (String file : files) {
                String path = "";
                if (filePath != null) {
                    path += filePath + File.separator;
                }
                path += file;
                reader = new BufferedReader(new FileReader(path));
                while ((str = reader.readLine()) != null) {
                    all.append(str);
                    all.append('\n');
                }
                if (files.length > 1) {
                    all.append('\n');
                }
            }
            _editor.setText(all.toString());
        } catch (Throwable e) {
            SQLExplorerPlugin.error("Error loading document", e);
        } finally {
            try {
                reader.close();
            } catch (java.io.IOException e) {
            }
        }
    }

    @Override
    public String getToolTipText() {
        return Messages.getString("Open_2");
    }

    @Override
    public ImageDescriptor getHoverImageDescriptor() {
        return img;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return img;
    }

    ;
}
