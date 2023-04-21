package com.elibera.ccs.buttons.editorbuttons;

import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import com.elibera.ccs.app.MLEConfig;
import com.elibera.ccs.buttons.ButtonEditorTagRoot;
import com.elibera.ccs.dialog.DialogTextBoxEdit;
import com.elibera.ccs.res.Msg;
import com.elibera.ccs.tagdata.DataTextBox;

/**
 * @author meisi
 *
 */
public class ButtonEditorTagTextbox extends ButtonEditorTagRoot {

    public static final long serialVersionUID = 100000003;

    public ImageIcon icon;

    private MLEConfig conf;

    public ButtonEditorTagTextbox(DataTextBox data, MLEConfig conf) {
        super(data);
        this.conf = conf;
        this.setBackground(java.awt.Color.WHITE);
        this.setBorder(LineBorder.createBlackLineBorder());
        this.setBorderPainted(true);
        doUpdate();
    }

    public void doUpdate() {
        DataTextBox data = (DataTextBox) this.data;
        String v = data.value;
        int size = data.size;
        if (size <= 0) size = 7;
        if (v == null) v = "";
        if (v.length() <= 0) v = "TextBox";
        if (v.length() > size) v = v.substring(0, size);
        this.setText(" " + v + " ");
    }

    protected void doAction() {
        DialogTextBoxEdit.showDialog(Msg.getString("ButtonEditorTagTextbox.DIALOG_OPEN_TITEL"), (DataTextBox) data, conf, false);
    }
}
