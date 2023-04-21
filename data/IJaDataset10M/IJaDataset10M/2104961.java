package org.slizardo.madcommander.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.slizardo.madcommander.MadCommander;
import org.slizardo.madcommander.actions.HelpAction;
import org.slizardo.madcommander.components.localized.LocalizedMenuItem;
import org.slizardo.madcommander.dialogs.AboutDialog;
import org.slizardo.madcommander.dialogs.HelpDialog;
import org.slizardo.madcommander.resources.images.IconFactory;
import org.slizardo.madcommander.resources.languages.Translator;
import org.slizardo.madcommander.util.SystemUtil;
import org.slizardo.madcommander.util.UpdateManager;

public class HelpMenu extends JMenu implements ActionListener {

    private JMenuItem helpIndex;

    private LocalizedMenuItem keyboard;

    private LocalizedMenuItem visitJavaCommanderWebSite;

    private LocalizedMenuItem checkForUpdate;

    private LocalizedMenuItem aboutJavaCommander;

    public HelpMenu() {
        super(Translator.text("Help"));
        setMnemonic(KeyEvent.VK_H);
        helpIndex = new JMenuItem(new HelpAction());
        keyboard = new LocalizedMenuItem("Keyboard");
        keyboard.addActionListener(this);
        visitJavaCommanderWebSite = new LocalizedMenuItem("Visit_MadCommander_web_site");
        visitJavaCommanderWebSite.addActionListener(this);
        visitJavaCommanderWebSite.setIcon(IconFactory.newIcon("link.png"));
        checkForUpdate = new LocalizedMenuItem("Check_for_update...");
        checkForUpdate.setIcon(IconFactory.newIcon("check.png"));
        checkForUpdate.addActionListener(this);
        aboutJavaCommander = new LocalizedMenuItem("About_MadCommander");
        aboutJavaCommander.addActionListener(this);
        add(helpIndex);
        add(keyboard);
        add(visitJavaCommanderWebSite);
        addSeparator();
        add(checkForUpdate);
        add(aboutJavaCommander);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == keyboard) {
            HelpDialog helpDialog = new HelpDialog("help/keyboard.txt");
            helpDialog.setVisible(true);
        } else if (source == visitJavaCommanderWebSite) {
            SystemUtil.browse(this, MadCommander.APP_URL);
        } else if (source == checkForUpdate) {
            UpdateManager.checkForUpdate();
        } else if (source == aboutJavaCommander) {
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.setVisible(true);
        }
    }
}
