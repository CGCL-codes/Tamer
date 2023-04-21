package gov.usda.gdpc.browser;

import javax.swing.*;
import java.util.*;

/**
 *
 * @author terryc
 */
public class ViewMenu extends JMenu {

    private Action myUpdateAction = null;

    private JMenuItem myUpdateMenuItem = null;

    private Action myDeselectAction = null;

    private JMenuItem myDeselectMenuItem = null;

    private Action myLoadAction = null;

    private JMenuItem myLoadMenuItem = null;

    /**
     * ViewMenu constructor comment.
     */
    public ViewMenu(Browser browser) {
        super();
        try {
            setText("View");
            setMnemonic('V');
            myUpdateAction = UpdateAction.getInstance(browser);
            myUpdateMenuItem = this.add(myUpdateAction);
            myUpdateMenuItem.setMnemonic(UpdateAction.MNEMONIC_KEY);
            myLoadAction = LoadFromGroupAction.getInstance(browser);
            myLoadMenuItem = this.add(myLoadAction);
            myLoadMenuItem.setMnemonic('L');
            myDeselectAction = DeselectAction.getInstance(browser);
            myDeselectMenuItem = this.add(myDeselectAction);
            myDeselectMenuItem.setMnemonic('D');
        } catch (Throwable ivjExc) {
            ivjExc.printStackTrace();
        }
    }

    public Action getUpdateAction() {
        return myUpdateAction;
    }
}
