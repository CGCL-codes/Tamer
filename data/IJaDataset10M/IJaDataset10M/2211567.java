package praktikumid.k11.p02.a;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;

/**
 * @author Ivor
 *
 */
public class GraafikaPind extends Canvas {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public GraafikaPind() {
        super();
        addMouseListener(new HiireKuulaja());
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(100, 100, 200, 200);
    }
}
