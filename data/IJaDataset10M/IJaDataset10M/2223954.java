package br.org.databasetools.core.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public abstract class TAbstractAction extends AbstractAction implements IAction {

    public TAbstractAction() {
    }

    public abstract void execute();

    public void actionPerformed(ActionEvent e) {
        execute();
    }
}
