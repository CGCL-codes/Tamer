package se.sics.cooja.mspmote.interfaces;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.jdom.Element;
import se.sics.cooja.ClassDescription;
import se.sics.cooja.Mote;
import se.sics.cooja.interfaces.LED;
import se.sics.cooja.mspmote.ESBMote;
import se.sics.mspsim.core.*;
import se.sics.mspsim.platform.esb.ESBNode;

/**
 * @author Fredrik Osterlind
 */
@ClassDescription("ESB LED")
public class ESBLED extends LED implements PortListener {

    private static Logger logger = Logger.getLogger(ESBLED.class);

    private ESBMote mote;

    private boolean redOn = false;

    private boolean greenOn = false;

    private boolean yellowOn = false;

    private static final Color DARK_GREEN = new Color(0, 100, 0);

    private static final Color DARK_YELLOW = new Color(100, 100, 0);

    private static final Color DARK_RED = new Color(100, 0, 0);

    private static final Color GREEN = new Color(0, 255, 0);

    private static final Color YELLOW = new Color(255, 255, 0);

    private static final Color RED = new Color(255, 0, 0);

    public ESBLED(Mote mote) {
        this.mote = (ESBMote) mote;
        IOUnit unit = this.mote.getCPU().getIOUnit("Port 2");
        if (unit instanceof IOPort) {
            ((IOPort) unit).setPortListener(this);
        }
    }

    public boolean isAnyOn() {
        return redOn || greenOn || yellowOn;
    }

    public boolean isGreenOn() {
        return greenOn;
    }

    public boolean isYellowOn() {
        return yellowOn;
    }

    public boolean isRedOn() {
        return redOn;
    }

    public JPanel getInterfaceVisualizer() {
        final JPanel panel = new JPanel() {

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = 20;
                int y = 25;
                int d = 25;
                if (isGreenOn()) {
                    g.setColor(GREEN);
                    g.fillOval(x, y, d, d);
                    g.setColor(Color.BLACK);
                    g.drawOval(x, y, d, d);
                } else {
                    g.setColor(DARK_GREEN);
                    g.fillOval(x + 5, y + 5, d - 10, d - 10);
                }
                x += 40;
                if (isRedOn()) {
                    g.setColor(RED);
                    g.fillOval(x, y, d, d);
                    g.setColor(Color.BLACK);
                    g.drawOval(x, y, d, d);
                } else {
                    g.setColor(DARK_RED);
                    g.fillOval(x + 5, y + 5, d - 10, d - 10);
                }
                x += 40;
                if (isYellowOn()) {
                    g.setColor(YELLOW);
                    g.fillOval(x, y, d, d);
                    g.setColor(Color.BLACK);
                    g.drawOval(x, y, d, d);
                } else {
                    g.setColor(DARK_YELLOW);
                    g.fillOval(x + 5, y + 5, d - 10, d - 10);
                }
            }
        };
        Observer observer;
        this.addObserver(observer = new Observer() {

            public void update(Observable obs, Object obj) {
                panel.repaint();
            }
        });
        panel.putClientProperty("intf_obs", observer);
        panel.setMinimumSize(new Dimension(140, 60));
        panel.setPreferredSize(new Dimension(140, 60));
        return panel;
    }

    public void releaseInterfaceVisualizer(JPanel panel) {
        Observer observer = (Observer) panel.getClientProperty("intf_obs");
        if (observer == null) {
            logger.fatal("Error when releasing panel, observer is null");
            return;
        }
        this.deleteObserver(observer);
    }

    public Collection<Element> getConfigXML() {
        return null;
    }

    public void setConfigXML(Collection<Element> configXML, boolean visAvailable) {
    }

    public void portWrite(IOPort source, int data) {
        redOn = (data & ESBNode.RED_LED) == 0;
        greenOn = (data & ESBNode.GREEN_LED) == 0;
        yellowOn = (data & ESBNode.YELLOW_LED) == 0;
        setChanged();
        notifyObservers();
    }
}
