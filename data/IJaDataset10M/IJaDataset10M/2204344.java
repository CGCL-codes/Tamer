package com.neptuny.xgrapher.cli.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.neptuny.xgrapher.cli.controller.Application;

/**
 * This action is responsible for the reset of the graph.
 *
 * @author Luigi Bianchi [luigi_bianchi@katamail.com]
 * @since 9/10/07
 *
 */
@SuppressWarnings("serial")
public class ResetGraphAction extends AbstractAction {

    private Application app;

    public ResetGraphAction(Application app) {
        super("Reset Graph");
        setEnabled(true);
        this.app = app;
    }

    public void actionPerformed(ActionEvent e) {
        app.getVv().restart();
    }
}
