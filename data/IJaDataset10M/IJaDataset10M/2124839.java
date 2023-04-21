package edu.uah.lazarillo.comm.out.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JApplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.uah.lazarillo.comm.out.EventListener;
import edu.uah.lazarillo.core.model.Area;
import edu.uah.lazarillo.core.model.Movement;
import edu.uah.lazarillo.core.model.Pacient;

public class Applet extends JApplet implements EventListener {

    private static int size = 100;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    Map<Pacient, Area> pacients = new HashMap<Pacient, Area>();

    public Applet() throws HeadlessException {
        super();
        this.resize(size * 5, size * 5);
    }

    AreaGui[] areas = { new AreaGui(0, 0, size, size, "cinco", 5), new AreaGui(size * 2, 0, size, size, "ocho", 8), new AreaGui(0, size, (size * 3 / 2), size, "seis", 6), new AreaGui(size * 3 / 2, size, (size * 3 / 2), size, "siete", 7), new AreaGui(size * 3, size, (size * 3 / 2), size, "nueve", 9), new AreaGui(0, size * 2, (size * 3 / 2), size, "cuatro", 4), new AreaGui(size * 3 / 2, size * 2, (size * 3 / 2), size, "dos", 2), new AreaGui(size * 3, size * 2, (size * 3 / 2), size, "diez", 10), new AreaGui(0, size * 3, size, size, "3", 3), new AreaGui(size * 2, size * 3, size, size, "1", 1) };

    @Override
    public void init() {
        super.init();
        logger.debug("INIT");
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        for (AreaGui a : areas) {
            g.drawRect(a.getX(), a.getY(), a.getWidth(), a.getHeight());
            g.drawString(a.getName(), a.getX() + 20, a.getY() + 20);
        }
        Set<Pacient> keys = pacients.keySet();
        for (Pacient pacient : keys) {
            Area area = pacients.get(pacient);
            for (AreaGui areaGui : areas) {
                if (areaGui.getId() == area.getId()) {
                    g.setColor(Color.RED);
                    g.drawOval(areaGui.getX() + (size / 2), areaGui.getY() + (size / 2), 10, 10);
                }
            }
        }
    }

    public void send(Movement movement) throws IOException {
        pacients.put(movement.getPacient(), movement.getTo());
        this.repaint();
    }

    public static void main(String args[]) {
        Frame appletFrame = new Frame("Some applet");
        appletFrame.resize(500, 400);
        appletFrame.show();
        java.applet.Applet myApplet = new Applet();
        appletFrame.add(myApplet);
        myApplet.init();
        myApplet.start();
    }
}
