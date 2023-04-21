package pmp.applet;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import pmp.gui.*;
import static pmp.gui.Const.*;

/**
 * Macroprocessor applet.
 * <p>
 * Title: PMP: Macroprocessor
 * </p>
 * <p>
 * Description: Java macroprocessor
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * @author Luděk Hlaváček
 * @version 1.0
 */
public class PMPApplet extends JApplet implements ActionListener {

    static final long serialVersionUID = -1L;

    public PMPApplet() throws HeadlessException {
        super();
    }

    /**
     * Returns info about applet.
     * 
     * @return same string as "About..." dialog.
     * @see AboutFrame#ABOUT_INFO
     */
    @Override
    public String getAppletInfo() {
        return pmp.gui.AboutFrame.ABOUT_INFO;
    }

    @Override
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    createGUI();
                }
            });
        } catch (Exception ex) {
        }
    }

    private void createGUI() {
        setName("PMP Applet");
        MainPanel pmpPanel = new MainPanel(this, this);
        setJMenuBar(pmpPanel.createMenu());
        getContentPane().add(pmpPanel);
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (MENU_QUIT.equals(action)) {
        } else if (MENU_ABOUT.equals(action)) {
            AboutFrame.showAboutFrame(this);
        } else if (MENU_HELP.equals(action)) {
            AboutFrame.showHelpFrame();
        }
    }
}
