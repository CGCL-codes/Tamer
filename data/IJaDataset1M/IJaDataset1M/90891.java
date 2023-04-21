package org.goniolab.cogo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import javax.swing.SwingUtilities;
import org.almondframework.ui.AApplication;
import org.almondframework.ui.AMenuBar;
import org.almondframework.ui.AMenuBar.AMenu;
import org.almondframework.ui.AMenuBar.AMenuItem;
import org.almondframework.ui.AMenuBar.ASeparator;

/**
 *
 * @author Patrik Karlsson
 */
public class Application extends AApplication {

    public Application() {
        SwingUtilities.updateComponentTreeUI(getAApplicationFrame());
        getAApplicationFrame().setMinimumSize(new Dimension(400, 600));
        updateLocale();
        getAApplicationFrame().setVisible(true);
    }

    @Override
    public void initFramework() {
        AMenuBar menuBar = getAApplicationFrame().getAMenuBar();
        menuBar.showMenu(AMenu.EDIT, true);
        menuBar.showMenuItem(AMenuItem.ITEM_CUT, true);
        menuBar.showMenuItem(AMenuItem.ITEM_COPY, true);
        menuBar.showMenuItem(AMenuItem.ITEM_PASTE, true);
        menuBar.showMenu(AMenu.SETTINGS, true);
        menuBar.showMenuItem(AMenuItem.CBOX_MENUBAR, true);
        menuBar.showMenuItem(AMenuItem.CBOX_STAYONTOP, true);
        menuBar.showMenuItem(AMenuItem.ITEM_CONFIGURE, true);
        menuBar.showMenuItem(AMenuItem.ITEM_HANDBOOK, true);
        menuBar.showMenuSeparator(ASeparator.HELP_1, true);
    }

    @Override
    public void initApplication() {
        cogo = new Cogo(this);
        getAApplicationFrame().add(cogo, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof AMenuItem) {
            switch((AMenuItem) arg) {
                case ITEM_CONFIGURE:
                    cogo.showConfigure();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Application();
            }
        });
    }

    private Cogo cogo;
}
