package jpatch.boundary.action;

import javax.swing.*;
import java.awt.event.*;
import jpatch.boundary.*;
import jpatch.entity.*;

/**
 * @author sascha
 *
 */
public class CopyPoseAction extends AbstractAction {

    private OLDModel model;

    public CopyPoseAction(OLDModel model) {
        super("copy pose");
        this.model = model;
    }

    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getAnimation().getClipboardPose(model).setPose();
        MainFrame.getInstance().getJPatchScreen().update_all();
    }
}
