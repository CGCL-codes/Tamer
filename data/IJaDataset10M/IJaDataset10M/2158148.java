package br.org.databasetools.core.view.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import br.org.databasetools.core.images.ImageManager;
import br.org.databasetools.core.view.TKeyStroke;
import br.org.databasetools.core.view.window.Controller;

@SuppressWarnings("serial")
public class RefreshButton extends TButton implements ActionListener {

    private Controller controller;

    public RefreshButton(Controller controller) throws Exception {
        ImageIcon image = ImageManager.getImageIcon("refresh");
        image.setDescription("Atualizar os registros da visualiza��o");
        this.setIcon(image);
        this.setToolTipText("<html>Atualizar os registros da visualiza��o " + TKeyStroke.toString(TKeyStroke.REFRESH) + "</html>");
        this.addActionListener(this);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            controller.execute("refresh");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
