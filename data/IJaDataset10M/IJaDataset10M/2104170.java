package src.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import src.applications.Aplicacion;
import src.gui.windows.MainWindow;

public class ActionAplicacionSeleccionada implements ActionListener {

    private MainWindow mainWindow = null;

    public ActionAplicacionSeleccionada(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox jcb = (JComboBox) e.getSource();
        Aplicacion ap = (Aplicacion) jcb.getSelectedItem();
        this.mainWindow.setPanelAplicacion(ap.getPanel());
        this.mainWindow.setEstado(ap.getEstado());
    }
}
