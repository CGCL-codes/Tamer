package myComponents.myGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JDialog;
import tools.Skin;

public class MyDraggable extends JDialog {

    private static final long serialVersionUID = 45364567452375637L;

    static Point origin = new Point();

    /**
   * Creates a draggable panel
   */
    public MyDraggable() {
        this.setUndecorated(true);
        this.setModal(true);
        getContentPane().setBackground(Skin.getOuterColor());
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                origin.x = e.getX();
                origin.y = e.getY();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
            }
        });
    }
}
