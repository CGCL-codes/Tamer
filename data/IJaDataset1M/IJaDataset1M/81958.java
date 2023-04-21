package apjava.snowman;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.JSlider;
import javax.swing.event.MouseInputListener;

public class SnowmanCanvas extends Canvas implements MouseInputListener {

    private static final long serialVersionUID = 1L;

    private Graphics2D buffer;

    private Image offscreen;

    private Dimension dim;

    private Snowman s;

    private JSlider tempSlider;

    private PointerInfo p;

    private Point l;

    private boolean drag;

    int sx, sy;

    public SnowmanCanvas(Snowman s, JSlider l, Object p) {
        dim = ((SnowmanRunner) p).getSize();
        offscreen = ((SnowmanRunner) p).createImage(dim.width, dim.height);
        buffer = (Graphics2D) (offscreen.getGraphics());
        this.s = s;
        this.tempSlider = l;
        addMouseListener(this);
        sx = 20;
        sy = 0;
    }

    public void paint(Graphics g) {
        buffer.clearRect(0, 0, dim.width, dim.height);
        buffer.setPaint(new GradientPaint(0, 0, Color.cyan, 0, dim.height / 5 * 4, Color.PINK, true));
        buffer.fillRect(0, 0, dim.width, dim.height * 4 / 5);
        buffer.setColor(Color.DARK_GRAY);
        buffer.fillRect(0, dim.height * 4 / 5, dim.width, dim.height / 5);
        if (drag) {
            p = MouseInfo.getPointerInfo();
            l = this.getLocationOnScreen();
            s.draw(buffer, p.getLocation().x - l.x - s.snowballs.get(s.snowballs.size() - 1).getCurrWidth() / 2, p.getLocation().y - l.y);
        } else s.draw(buffer, sx, sy);
        buffer.setColor(Color.black);
        buffer.drawString("Temp: " + tempSlider.getValue(), 140, 20);
        getGraphics().drawImage(offscreen, 0, 0, this);
    }

    public void mouseClicked(MouseEvent e) {
        System.out.print(e.getX() + "," + e.getY());
        System.out.println(s.hitTest(e.getX(), e.getY(), 0, 0));
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            drag = false;
            sx = e.getX() - s.snowballs.get(s.snowballs.size() - 1).getCurrWidth() / 2;
            sy = e.getY();
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) drag = true;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        System.out.println("drag");
    }

    public void mouseMoved(MouseEvent e) {
    }
}
