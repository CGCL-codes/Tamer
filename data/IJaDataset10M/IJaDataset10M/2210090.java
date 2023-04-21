package de.shandschuh.jaolt.gui;

import java.awt.Dimension;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import de.shandschuh.jaolt.gui.actions.NewAuctionAction;
import de.shandschuh.jaolt.gui.mainmenubar.EditJMenu;
import de.shandschuh.jaolt.gui.mainmenubar.FileJMenu;
import de.shandschuh.jaolt.gui.mainmenubar.HelpJMenu;
import de.shandschuh.jaolt.gui.mainmenubar.UserJMenu;

public class MainJMenuBar extends JMenuBar {

    /** Default serial version uid */
    private static final long serialVersionUID = 1L;

    private FileJMenu fileJMenu;

    private EditJMenu editJMenu;

    private UserJMenu userJMenu;

    private HelpJMenu helpJMenu;

    private NewAuctionAction newAuctionComponent;

    public MainJMenuBar() {
        super();
        fileJMenu = new FileJMenu();
        editJMenu = new EditJMenu();
        userJMenu = new UserJMenu();
        helpJMenu = new HelpJMenu();
        newAuctionComponent = new NewAuctionAction(null);
        this.add(fileJMenu);
        this.add(editJMenu);
        this.add(userJMenu);
        this.add(helpJMenu);
        JMenuItem item = new JMenuItem(newAuctionComponent);
        item.setMaximumSize(new Dimension(item.getPreferredSize().width, item.getMaximumSize().height));
        add(item);
    }

    public void setMemberActive(boolean active) {
        fileJMenu.setMemberActive(active);
        editJMenu.setActive(active);
        userJMenu.setActive(active);
        helpJMenu.setActive(active);
    }

    public void setMainJPanel(MainJPanel mainJPanel) {
        if (mainJPanel != null) {
            newAuctionComponent.setAuctionListJPanel(mainJPanel.getEditAuctionsListJPanel());
        } else {
            newAuctionComponent.setAuctionListJPanel(null);
        }
    }
}
