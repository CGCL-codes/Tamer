package jphotoshop.select;

import java.awt.event.ActionEvent;
import jphotoshop.plug.AbstractJPhotoShopPlugin;
import jphotoshop.ui.EditFrame;

/**
 * @author: liuke
 * @email:  soulnew@gmail.com
 */
public class UnselectAction extends AbstractJPhotoShopPlugin {

    public static String ID = "UnselectAction";

    EditFrame editFrame;

    public UnselectAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editFrame.getModel().getSelectPath().clear();
    }
}
