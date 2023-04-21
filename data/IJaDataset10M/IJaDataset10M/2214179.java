package jmemorize.gui.swing.actions;

import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import jmemorize.core.Main;
import jmemorize.gui.Localization;
import jmemorize.gui.swing.MainFrame;

/**
 * Toggles the category tree (showing it when its currently hidden and vice
 * versa).
 * 
 * @author djemili
 */
public class ShowCategoryTreeAction extends AbstractAction {

    public ShowCategoryTreeAction() {
        putValue(NAME, Localization.getString("MainFrame.CATEGORY_TREE"));
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/resource/icons/tree.gif")));
        putValue(SHORT_DESCRIPTION, Localization.getString("MainFrame.CATEGORY_TREE_DESC"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, 0));
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        MainFrame frame = Main.getInstance().getFrame();
        frame.showCategoryTree(!frame.isShowCategoryTree());
    }
}
