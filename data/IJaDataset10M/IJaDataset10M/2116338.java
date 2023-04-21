package com.elibera.ccs.buttons.editorbuttons;

import javax.swing.ImageIcon;
import com.elibera.ccs.buttons.ButtonEditorTagRoot;
import com.elibera.ccs.dialog.DialogResultEdit;
import com.elibera.ccs.img.HelperBinary;
import com.elibera.ccs.res.Msg;
import com.elibera.ccs.tagdata.DataResult;

/**
 * @author meisi
 *
 */
public class ButtonEditorTagResult extends ButtonEditorTagRoot {

    public static final long serialVersionUID = 100000002;

    public ImageIcon icon;

    public ButtonEditorTagResult(DataResult data) {
        super(data);
        icon = new ImageIcon(data.container.getBinaryContainer().getInternalImage(HelperBinary.IMAGE_QUESTION_RESULT + "").getImage());
        this.setIcon(icon);
    }

    protected void doAction() {
        DialogResultEdit.showDialog(Msg.getString("ButtonEditorTagResult.DIALOG_OPEN_TITEL"), (DataResult) data, false);
    }

    public void doUpdate() {
    }
}
