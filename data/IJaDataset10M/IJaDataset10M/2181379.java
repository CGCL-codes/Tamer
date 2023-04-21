package com.groovytagger.ui.frames;

import com.groovytagger.utils.LogManager;
import com.groovytagger.utils.StaticObj;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JDialog;

public class Dialog_Save extends JDialog {

    public Dialog_Save() {
        this(null, "", false);
    }

    public Dialog_Save(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            LogManager.getInstance().getLogger().error(e);
            if (StaticObj.DEBUG) e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 300));
        this.getContentPane().setLayout(null);
        this.setModal(true);
    }
}
