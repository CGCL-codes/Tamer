package fullflow;

import com.sun.java.swing.plaf.nimbus.LoweredBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author blackmoon
 */
public class PanelNombre extends JPanel implements ActionListener, KeyListener {

    JLabel descripcion;

    JTextField nombreDelProyecto;

    JButton aceptar;

    JButton cancelar;

    Ventana ventana;

    public PanelNombre(Ventana ventana) {
        this.setVisible(false);
        this.setBorder(new LoweredBorder());
        this.ventana = ventana;
        descripcion = new JLabel("Nombre Del Proyecto : ");
        nombreDelProyecto = new JTextField("");
        aceptar = new JButton("Aceptar");
        cancelar = new JButton("Cancelar");
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        aceptar.addActionListener(this);
        cancelar.addActionListener(this);
        this.add(descripcion);
        this.add(nombreDelProyecto);
        this.add(Box.createHorizontalStrut(60));
        this.add(aceptar);
        this.add(cancelar);
        nombreDelProyecto.addKeyListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(aceptar)) {
            ventana.agregarPestaña(nombreDelProyecto.getText());
            this.setVisible(false);
            nombreDelProyecto.setText("");
        }
        if (e.getSource().equals(cancelar)) {
            this.setVisible(false);
        }
    }

    public JButton getAceptar() {
        return aceptar;
    }

    public void setAceptar(JButton aceptar) {
        this.aceptar = aceptar;
    }

    public JButton getCancelar() {
        return cancelar;
    }

    public void setCancelar(JButton cancelar) {
        this.cancelar = cancelar;
    }

    public JLabel getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(JLabel descripcion) {
        this.descripcion = descripcion;
    }

    public JTextField getNombreDelProyecto() {
        return nombreDelProyecto;
    }

    public void setNombreDelProyecto(JTextField nombreDelProyecto) {
        this.nombreDelProyecto = nombreDelProyecto;
    }

    public Ventana getVentana() {
        return ventana;
    }

    public void setVentana(Ventana ventana) {
        this.ventana = ventana;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            ventana.agregarPestaña(nombreDelProyecto.getText());
            this.setVisible(false);
            nombreDelProyecto.setText("");
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
