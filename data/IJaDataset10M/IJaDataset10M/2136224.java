package de.itar.swing;

import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import de.itar.resources.Const;
import de.itar.resources.Constants;
import de.itar.resources.Resources;
import de.itar.exceptions.BiboExceptionHelper;
import hm.core.utils.SwingHelper;

public class HelpFrame extends JFrame {

    private static HelpFrame singleton;

    public static void showHelpFrame() {
        if (singleton == null) {
            singleton = new HelpFrame();
        }
        singleton.setVisible(true);
    }

    private HelpFrame() {
        super(Const.getInstance().getProperty(Constants.MENU_ITEM_CONTENT) + " " + Const.PROGRAMM_NAME_VERSION);
        try {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JEditorPane editorPane = new JEditorPane(Resources.HELP_URL_GREMAN);
            editorPane.setEditable(false);
            JScrollPane sp = new JScrollPane(editorPane);
            getContentPane().add(sp, BorderLayout.CENTER);
            SwingHelper.centerInScreen(this, 70, true);
            setVisible(true);
        } catch (Exception ex) {
            BiboExceptionHelper.showErrorDialog(ex);
        }
    }
}
