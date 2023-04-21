package vista.ventanas;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Dimension;
import vista.imagenTramo.Imagen;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import vista.imagenTramo.Posicion;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * @author Usuario
 *
 */
public class VentanaSplash extends JWindow {

    JProgressBar progress = null;

    private Imagen imagen = null;

    /**
	 * 
	 */
    public VentanaSplash() {
        this.setLayout(null);
        progress = new JProgressBar();
        this.add(progress);
        this.setSize(400, 260);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        progress.setValue(0);
        progress.setStringPainted(true);
        progress.setBounds(0, 240, 400, 20);
        imagen = new Imagen("src//vista//ventanas//imagenSplash.png", new Dimension(400, 240), new Posicion());
        JPanel panel = new JPanel() {

            @Override
            public void paint(Graphics g) {
                Graphics2D grafico = (Graphics2D) g;
                grafico.drawImage(imagen.getImage(), imagen.getPosicion().getX(), imagen.getPosicion().getY(), imagen.getDimension().width, imagen.getDimension().height, null);
            }
        };
        panel.setBounds(0, 0, 400, 240);
        panel.setVisible(true);
        this.add("North", panel);
        this.repaint();
    }

    public void setProgresoProgressBar(int progreso) {
        progress.setValue(progreso);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
