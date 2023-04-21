package vista.herramientas.dibujo.lineas;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import javax.accessibility.AccessibleIcon;
import javax.swing.Icon;

/**
 * Implementa un icono que representa una linea punteada.
 */
public class IconoLineaPunteada implements Icon, AccessibleIcon, Serializable {

    private static final long serialVersionUID = 1L;

    private String descripcion;

    private static final int ancho = 150;

    private static final int alto = 20;

    private static final int desplazamientoX = 0;

    private static final int desplazamientoY = alto / 2;

    public IconoLineaPunteada() {
        super();
        this.descripcion = "Una línea de puntos";
    }

    public int getIconHeight() {
        return alto;
    }

    public int getIconWidth() {
        return ancho;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c.getForeground());
        g2d.setBackground(c.getBackground());
        float[] patron = { 1.0f, 1.0f };
        BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, patron, 0);
        g2d.setStroke(bs);
        g2d.drawLine(desplazamientoX + x, desplazamientoY + y, ancho + x, desplazamientoY + y);
        g2d.dispose();
    }

    public String getAccessibleIconDescription() {
        return this.descripcion;
    }

    public int getAccessibleIconHeight() {
        return IconoLineaPunteada.alto;
    }

    public int getAccessibleIconWidth() {
        return IconoLineaPunteada.ancho;
    }

    public void setAccessibleIconDescription(String description) {
        this.descripcion = description;
    }
}
