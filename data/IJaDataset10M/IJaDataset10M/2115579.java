package ciarlini.ftpsynch.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ciarlini.ftpsynch.thread.Temporizador;

public class IniciarTemporizadorAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        Temporizador.getInstance().start();
    }
}
