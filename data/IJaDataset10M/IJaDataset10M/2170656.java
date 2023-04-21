package jphotoshop.action;

import java.awt.event.ActionEvent;
import jphotoshop.AppCore;
import jphotoshop.util.ResourceBundleUtil;

/**
 * @author liuke
 * @email:  soulnew@gmail.com
 */
public class LayerFrameShow {

    public static final String ID = "LayerFrame";

    AppCore jp;

    public LayerFrameShow(AppCore jp) {
        this.jp = jp;
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("resource.LocalMenuBar");
    }

    public void actionPerformed(ActionEvent e) {
    }
}
