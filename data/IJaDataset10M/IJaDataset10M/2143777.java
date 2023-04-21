package org.attacmadrid.sgss.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.attacmadrid.sgss.application.SGSSApplication;
import org.attacmadrid.sgss.view.CreateGrupoView;

public class CreateGrupoAction extends AbstractAction {

    public CreateGrupoAction() {
        super("Dar de alta un grupo");
    }

    public void actionPerformed(ActionEvent e) {
        SGSSApplication.getInstance().getMainFrame().setView(new CreateGrupoView());
    }
}
