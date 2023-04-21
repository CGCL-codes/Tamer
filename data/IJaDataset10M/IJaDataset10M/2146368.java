package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import modelo.Liga;
import vista.vFechadePartidos;

public class cFechadePartidos implements ActionListener {

    private vFechadePartidos fechaPartidos;

    public cFechadePartidos(Liga liga) {
        super();
        this.fechaPartidos = new vFechadePartidos();
        this.fechaPartidos.setLocation(480, 220);
        this.fechaPartidos.setVisible(true);
        this.fechaPartidos.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("Salir")) {
            this.fechaPartidos.setVisible(false);
        }
    }
}
