package org.magnesia.client.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.magnesia.client.gui.ClientConnection;
import org.magnesia.client.gui.components.ImageLabel;
import org.magnesia.client.gui.data.CachedJImage;
import static org.magnesia.client.misc.Utils.i18n;

public class RenameImage extends AbstractAction {

    private static final long serialVersionUID = 7776668582371184335L;

    private CachedJImage img;

    private ImageLabel il;

    public RenameImage(CachedJImage img, ImageLabel il) {
        super(i18n("action_rename"));
        this.img = img;
        this.il = il;
    }

    public void actionPerformed(ActionEvent arg0) {
        Object newName = JOptionPane.showInputDialog(null, i18n("action_rename_text"), i18n("action_rename_caption"), JOptionPane.PLAIN_MESSAGE, null, null, img.getName());
        if ((newName != null) && !"".equals(newName) && !img.getName().equals(newName)) {
            ClientConnection cc = ClientConnection.getConnection();
            if (cc.rename(img.getPath() + "/" + img.getName(), img.getPath() + "/" + newName)) {
                img.setName("" + newName);
                il.refreshName();
            } else {
                JOptionPane.showMessageDialog(null, i18n("action_rename_failed"), i18n("action_rename_failed_caption"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
