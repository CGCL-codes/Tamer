package Cosmo.ui.menu.submenu.viewSource;

import java.awt.event.ActionEvent;
import Cosmo.ui.graphics.model;
import Cosmo.util.Constants;

/**
 *
 * @author  shridhar
 */
public class JavaSourceCodeSubMenu extends SourceCodeSubMenu {

    public JavaSourceCodeSubMenu(model m) {
        super("DEVSJava Source Code", m);
        this.m = m;
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        String fileName = System.getProperty("user.dir") + "\\MB_Models\\" + Constants.DB_NAME + "\\JavaModels\\GeneratedModelsDEVS_Suite\\" + Constants.THE_QUERY.getClassName(m.getTID() + ":" + m.getTIID() + ":" + m.getIID()) + Constants.JAVA_EXTENSION;
        requestModelForEditorFromServer(fileName);
    }
}
