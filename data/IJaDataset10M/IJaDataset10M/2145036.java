package shapetool;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is a demo class, created to show some features of the shape toolkit.
 * @author AntonioSRGomes
 * @version $Id: GUI.java,v 1.1 2003/10/22 03:06:43 asrgomes Exp $
 */
public class GUI extends JFrame {

    private DrawArea drawArea;

    private JScrollPane drawAreaSP;

    private JPanel buttonPanel;

    private JButton exitBtn;

    private JButton helpBtn;

    /**
     * Creates the GUI window.
     * @param name window name
     * @param dim dimensions
 */
    public GUI(String name, Dimension dim) {
        super(name);
        getContentPane().setLayout(new BorderLayout());
        drawArea = new DrawArea("Main");
        drawArea.setPreferredSize(dim);
        drawAreaSP = new JScrollPane(drawArea);
        drawAreaSP.setPreferredSize(new Dimension(640, 480));
        getContentPane().add(drawAreaSP, "Center");
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        getContentPane().add(buttonPanel, "North");
        exitBtn = new JButton("Exit");
        exitBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(exitBtn);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void fitDrawArea() {
        Rectangle r = drawArea.getFullBounds();
        System.err.println(r);
        drawArea.setPreferredSize(new Dimension(r.width + r.x + 20, r.height + r.y + 20));
        drawArea.revalidate();
    }

    public void createEntities() {
        setLocation(100, 100);
        drawArea.setSnapToGrid(true);
        drawArea.setAntialiasing(true);
        final DrawArea _da = drawArea;
        final JPopupMenu menu = new JPopupMenu("DrawArea PopupMenu");
        final JCheckBoxMenuItem mi2 = new JCheckBoxMenuItem("Antialiasing", _da.getAntialiasing());
        mi2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _da.setAntialiasing(mi2.getState());
            }
        });
        menu.add(mi2);
        final JCheckBoxMenuItem mi3 = new JCheckBoxMenuItem("Show grid lines", _da.getShowGrid());
        mi3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _da.setShowGrid(mi3.getState());
            }
        });
        menu.add(mi3);
        final JCheckBoxMenuItem mi4 = new JCheckBoxMenuItem("Snap to grid", _da.getSnapToGrid());
        mi4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _da.setSnapToGrid(mi4.getState());
            }
        });
        menu.add(mi4);
        final JMenuItem mi5 = new JMenuItem("Set scale to 100%");
        mi5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _da.setScale(1);
                _da.repaint();
            }
        });
        menu.add(mi5);
        final JMenuItem mi8 = new JMenuItem("Set scale to 66%");
        mi8.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _da.setScale(.66);
                _da.repaint();
            }
        });
        menu.add(mi8);
        final JMenuItem mi6 = new JMenuItem("Set scale to 50%");
        mi6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _da.setScale(.5);
                _da.repaint();
            }
        });
        menu.add(mi6);
        final JMenuItem mi7 = new JMenuItem("Set scale to 25%");
        mi7.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _da.setScale(.33);
                _da.repaint();
            }
        });
        menu.add(mi7);
        drawArea.setPopupMenu(menu);
    }

    public static void main(String[] args) {
        GUI frame = new GUI("Shape Toolkit Demo", new Dimension(1700, 1400));
        frame.createEntities();
        frame.pack();
        frame.setVisible(true);
    }
}

class ExampleObserver extends EnterExitObserver {

    private Entity _e = null;

    public void mouseEntered(Entity e) {
        _e = e;
        e.getParent().repaint();
    }

    public void mouseExited(Entity e) {
        _e.getParent().repaint();
        _e = null;
    }

    public boolean paintLast(DrawArea src, Graphics2D g) {
        if (_e != null) {
            g.drawString(_e.getName(), _e.getX(), _e.getY());
        }
        return false;
    }
}
