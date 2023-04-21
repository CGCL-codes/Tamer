package edu.jrous.tool.graphicpaquete;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import edu.jrous.paquetes.PackageSerial;
import edu.jrous.ui.Constantes;

public class GraphicsSerial extends JComponent {

    /**
	 * <p></p>
	 */
    private static final long serialVersionUID = 5384754922627370427L;

    /**
	 * Paquete a utilizar
	 */
    transient PackageSerial ser;

    /**
	 * String de figuras
	 */
    transient ImageIcon iconame;

    /**
	 * Name
	 */
    public transient String name;

    public GraphicsSerial(PackageSerial _ser, String _iconame, String _name) {
        ser = _ser;
        iconame = new ImageIcon(getClass().getResource(_iconame));
        name = _name;
    }

    /**
	 * Para pintar
	 */
    public void paint(Graphics g) {
        g.setFont(Constantes.FONT_PACKETES);
        if (ser == null) {
            g.drawImage(iconame.getImage(), 20, 10, this);
            g.setColor(Constantes.COLOR_AZULNEGRO);
            g.drawString(name, 55, 30);
        } else {
            try {
                g.setColor(Constantes.COLOR_OSI);
                g.fillRect(40, 10, 60, 43);
                g.setColor(Constantes.COLOR_WHITE);
                g.drawString("FISICO", 54, 35);
                g.setColor(Constantes.COLOR_OSII);
                g.fillRect(102, 10, 160, 43);
                g.setColor(Constantes.COLOR_BLACK);
                g.drawString(ser.getProtocol().toUpperCase(), 168, 35);
                g.setColor(Constantes.COLOR_OSIII);
                g.fillRect(264, 10, 135, 43);
                g.setColor(Constantes.COLOR_BLACK);
                g.drawString("IP", 330, 21);
                g.drawString("IP.O. ", 280, 34);
                g.drawString("IP.D. ", 280, 48);
                g.drawString(ser.getData().getSourceIP().getIP(), 313, 34);
                g.drawString(ser.getData().getDestinyIP().getIP(), 313, 48);
                g.setColor(Constantes.COLOR_OSIV);
                g.fillRect(401, 10, 80, 43);
                g.setColor(Constantes.COLOR_BLACK);
                if (ser.getData().getTransport().equals(Constantes.ICMP)) {
                    g.drawString("ICMP", 429, 35);
                    throw new Exception();
                }
                g.drawString("TCP", 435, 21);
                g.drawString("P.O. " + ser.getData().getPort_source(), 415, 34);
                g.drawString("P.D. " + ser.getData().getPort_destiny(), 415, 48);
                g.setColor(Constantes.COLOR_OSVI);
                g.fillRect(483, 10, 80, 43);
                g.setColor(Constantes.COLOR_WHITE);
                g.drawString("TELNET", 505, 35);
            } catch (Exception e) {
            } finally {
                g.setColor(Constantes.COLOR_BLACK);
                g.drawOval(10, 0, 30, 60);
                g.setColor(Constantes.COLOR_WHITE);
                g.fillRect(20, 0, 21, 62);
                g.setColor(Constantes.COLOR_AZULNEGRO);
                g.drawLine(19, 50, 20, 60);
                g.drawLine(10, 53, 20, 60);
                g.drawImage(iconame.getImage(), 20, 65, this);
                g.drawString(name, 55, 75);
            }
        }
    }
}
