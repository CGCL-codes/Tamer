package br.org.databasetools.core.view.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import br.org.databasetools.core.view.TKeyStroke;
import br.org.databasetools.core.view.window.Controller;

@SuppressWarnings("serial")
public class CloseButton extends TButton implements ActionListener {

    private Controller controller;

    public CloseButton(Controller controller) {
        URL imageURL = DeleteButton.class.getResource("/images/22/exit.png");
        this.setIcon(new ImageIcon(imageURL, "Fecha Janela"));
        this.setToolTipText("<html>Fechar a janela " + TKeyStroke.toString(TKeyStroke.CANCELANDEXIT) + "<html>");
        this.addActionListener(this);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            controller.execute("close");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
