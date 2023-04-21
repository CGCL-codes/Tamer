package com.elibera.ccs.buttons.action;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import com.elibera.ccs.buttons.ButtonAllRootActionCommand;
import com.elibera.ccs.dialog.DialogVideoEdit;
import com.elibera.ccs.img.HelperBinary;
import com.elibera.ccs.img.ImageReader;
import com.elibera.ccs.parser.InterfaceDocContainer;
import com.elibera.ccs.res.Msg;
import com.elibera.ccs.tagdata.DataVideo;

/**
 * @author meisi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ButtonVideoInsert extends ButtonAllRootActionCommand {

    private boolean isMLEConf = false;

    public ButtonVideoInsert(InterfaceDocContainer conf, JPanel panel) {
        super(conf, panel);
        if (conf.equals(conf.getMLEConfig())) isMLEConf = true;
    }

    protected ImageIcon getButtonImageIcon(InterfaceDocContainer conf) {
        return new ImageIcon(ImageReader.openImageJar(conf, HelperBinary.EDITOR_IMG_VIDEO));
    }

    protected String getButtonTitel() {
        return Msg.getMsg("INSERT_BUTTON_VIDEO_TITEL");
    }

    protected String getHelpText() {
        return Msg.getMsg("INSERT_BUTTON_VIDEO_TOOLTIP");
    }

    protected void doActionForThisButton() {
        try {
            if (isMLEConf) container.getMLEConfig().expertButtonDialog.setVisible(false);
            DataVideo data = new DataVideo(container, null);
            DialogVideoEdit.showDialog(Msg.getMsg("DIALOG_INSERT_VIDEO_TITEL"), data, false);
        } catch (Exception e) {
            System.out.println("doActionForThisButton:");
            e.printStackTrace();
        }
    }
}
