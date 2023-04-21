package be.lassi.ui.widgets;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class TestButton extends JButton implements MouseListener {

    private int size = 1;

    public TestButton(final String title) {
        super(title);
        addMouseListener(this);
        addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                System.out.println("Button action " + (size + 1) + " " + getBounds());
                if (++size > 4) {
                    size = 1;
                }
                Rectangle r = getBounds();
                setBounds(r.x, r.y, r.width * size, r.height * size);
                System.out.println("Nieuw bounds " + getBounds());
            }
        });
    }

    private void createPopup(final Point point) {
        JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem("Option 1"));
        popup.add(new JMenuItem("Option 2"));
        popup.add(new JMenuItem("Option 3"));
        popup.add(new JMenuItem("Option 4"));
        popup.add(new JMenuItem("Option 5"));
        popup.show(this, point.x, point.y);
    }

    public void mouseClicked(final MouseEvent evt) {
        System.out.println("Clicked");
        System.out.println("Clicked Popup");
        createPopup(evt.getPoint());
    }

    public void mouseEntered(final MouseEvent evt) {
    }

    public void mouseExited(final MouseEvent evt) {
    }

    public void mousePressed(final MouseEvent evt) {
    }

    public void mouseReleased(final MouseEvent evt) {
    }
}
