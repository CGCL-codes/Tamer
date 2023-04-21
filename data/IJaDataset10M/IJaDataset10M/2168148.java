package org.in4ama.editor.xui.dialog;

import java.io.File;
import javax.swing.JFileChooser;
import net.xoetrope.swing.XEdit;

public class OOLocater extends BaseDialogPage {

    private XEdit txtOODir;

    public void pageCreated() {
        txtOODir = (XEdit) findComponent("txtOODir");
    }

    public void setOODir(String path) {
        txtOODir.setText(path);
    }

    public String getOODir() {
        return txtOODir.getText();
    }

    public void browse() {
        JFileChooser dlg = new JFileChooser();
        dlg.setSelectedFile(new File(txtOODir.getText()));
        int result = dlg.showOpenDialog(this);
        File selected = dlg.getSelectedFile();
        if (selected != null) {
            txtOODir.setText(selected.getAbsolutePath());
        }
    }
}
