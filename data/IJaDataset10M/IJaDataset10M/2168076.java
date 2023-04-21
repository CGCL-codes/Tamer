package app;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
*
* @author Edisoncor
*/
public class JPanelgradient extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Gradiente gradiente;

    public JPanelgradient() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Rectangle clip = g2.getClipBounds();
        if (gradiente != null) g2.setPaint(gradiente.getGradiente());
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);
    }

    public Gradiente getGradiente() {
        return gradiente;
    }

    public void setGradiente(Gradiente gradiente) {
        gradiente.setComponente(this);
        this.gradiente = gradiente;
    }
}
