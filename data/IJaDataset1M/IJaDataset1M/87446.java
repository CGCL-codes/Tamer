package listener;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * zust�ndig f�r das Mouseover Event
 * @author tim
 *
 */
public class ButtonMouseListener implements MouseListener {

    private String path;

    private JButton button;

    private Icon old_icon;

    private boolean staticSize = false;

    public ButtonMouseListener(String path, JButton button) {
        this.path = path;
        this.button = button;
        old_icon = button.getIcon();
    }

    /**
	 * @param staticSize gibt an ob der button bei mouseover vergroessert werden soll
	 */
    public ButtonMouseListener(String path, JButton button, boolean staticSize) {
        this(path, button);
        this.staticSize = staticSize;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (staticSize) {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(path));
            button.setIcon(icon);
            return;
        }
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(path));
        button.setSize(button.getSize().width + 10, button.getSize().height + 10);
        button.setLocation(button.getLocation().x, button.getLocation().y - 10);
        Image scaled1 = icon.getImage().getScaledInstance(button.getSize().width, button.getSize().height, Image.SCALE_SMOOTH);
        ImageIcon scaled_icon = new ImageIcon(scaled1);
        button.setIcon(scaled_icon);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (staticSize) {
            button.setIcon(old_icon);
            return;
        }
        button.setIcon(old_icon);
        button.setSize(button.getSize().width - 10, button.getSize().height - 10);
        button.setLocation(button.getLocation().x, button.getLocation().y + 10);
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
}
