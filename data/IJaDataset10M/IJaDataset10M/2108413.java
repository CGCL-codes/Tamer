package br.org.databasetools.core.view.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import br.org.databasetools.core.view.TKeyStroke;
import br.org.databasetools.core.view.window.Controller;

@SuppressWarnings("serial")
public class DeleteButton extends TButton implements ActionListener {

    private Controller controller;

    public DeleteButton(Controller controller) {
        URL imageURL = DeleteButton.class.getResource("/images/22/trash.png");
        this.setIcon(new ImageIcon(imageURL, "Deleta registro"));
        this.setToolTipText("<html>Elimina um registro " + TKeyStroke.toString(TKeyStroke.DELETE) + "</html>");
        this.addActionListener(this);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            controller.execute("delete");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
