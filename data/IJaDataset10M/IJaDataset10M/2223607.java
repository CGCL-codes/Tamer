package net.nohaven.proj.javeau.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import net.nohaven.proj.javeau.ui.fw.GenericCompanion;
import net.nohaven.proj.javeau.ui.fw.MenuItemClickListener;
import net.nohaven.proj.javeau.ui.generated.FileManager;

public class FileManagerCompanion extends GenericCompanion {

    private static final String VERSION = "0.2";

    private static final String ABOUT_1 = "Javeau v. " + VERSION;

    private static final String ABOUT_2 = "Your private caveau, with a fancy 'J'";

    private static final String ABOUT_3 = "Copyright (c) 2008, Germano Rizzo <projects@nohaven.net>";

    protected static final int LEFT_FS = 0;

    protected static final int RIGHT_FS = 1;

    private static final ImageIcon APP_ICO = new ImageIcon(FSTableManager.class.getResource("icon/monkey.png"));

    public FileManager gui;

    public FSPanelCompanion[] panels = new FSPanelCompanion[2];

    public FileManagerCompanion() {
        gui = new FileManager();
        gui.setTitle(ABOUT_1 + " - " + ABOUT_2);
        gui.setIconImage(APP_ICO.getImage());
        gui.setLocationByPlatform(true);
        gui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        gui.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                dispose();
            }
        });
        setFS(LEFT_FS);
        setFS(RIGHT_FS);
        gui.spFilesystems.setDividerLocation(0.5);
        gui.miAbout.addActionListener(new MenuItemClickListener() {

            public void action() {
                JOptionPane.showMessageDialog(gui, ABOUT_1 + "\n\n" + ABOUT_2 + "\n\n" + ABOUT_3, "About", JOptionPane.OK_OPTION, APP_ICO);
            }
        });
        gui.miExit.addActionListener(new MenuItemClickListener() {

            public void action() {
                dispose();
            }
        });
        gui.setVisible(true);
    }

    public void dispose() {
        if (!askConfirmation(gui, "Are you sure you want to quit?")) return;
        gui.setVisible(false);
        panels[LEFT_FS].dispose();
        panels[RIGHT_FS].dispose();
        gui.dispose();
        System.exit(0);
    }

    private void setFS(int whichOne) {
        if (panels[whichOne] != null) panels[whichOne].dispose();
        panels[whichOne] = new FSPanelCompanion(this, whichOne);
        switch(whichOne) {
            case LEFT_FS:
                gui.spFilesystems.setLeftComponent(panels[LEFT_FS].gui);
                break;
            case RIGHT_FS:
                gui.spFilesystems.setRightComponent(panels[RIGHT_FS].gui);
                break;
        }
    }

    protected FSPanelCompanion getOtherPanel(int whichOneAmI) {
        return panels[1 - whichOneAmI];
    }
}
