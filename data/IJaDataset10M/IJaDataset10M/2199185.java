package javax.swing.addon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import com.listentothesong.ImagesRepository;

@SuppressWarnings("serial")
public class IconPopupChooser<T> extends JPanel {

    private static final int GAP = 3;

    private final JPopupMenu popupMenu = new JPopupMenu();

    private final JButton openButton = new JButton();

    public IconPopupChooser(ImageIcon defaultIcon) {
        ImageIcon defaultIconWithArrow = createImageWithArrow(defaultIcon);
        openButton.setIcon(defaultIconWithArrow);
        openButton.setPreferredSize(new Dimension(defaultIconWithArrow.getIconWidth() + 10, defaultIconWithArrow.getIconHeight() + 8));
        openButton.setFocusable(false);
        openButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                popupMenu.show(openButton, 0, openButton.getHeight());
            }
        });
        setLayout(new BorderLayout());
        add(openButton);
    }

    public void addItem(final IconPopupItem<T> item) {
        final ImageIcon icon = item.getImageIcon();
        final JMenuItem menuItem = new JMenuItem(icon) {

            public Dimension getPreferredSize() {
                return new Dimension(icon.getIconWidth() + 13, icon.getIconHeight() + 6);
            }
        };
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    openButton.setIcon(createImageWithArrow(item.getImageIcon()));
                    item.fireActionPerformed();
                } catch (Throwable t) {
                    ErrorDetailsDialog.open(t);
                }
            }
        });
        popupMenu.add(menuItem);
    }

    protected ImageIcon createImageWithArrow(ImageIcon imageIcon) {
        ImageIcon arrowIcon = ImagesRepository.MINI_DOWN_ARROW;
        int imageHeight = Math.max(imageIcon.getIconHeight(), arrowIcon.getIconHeight());
        BufferedImage newImage = new BufferedImage(imageIcon.getIconWidth() + GAP + arrowIcon.getIconWidth(), imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.createGraphics();
        g.drawImage(imageIcon.getImage(), 0, (imageHeight - imageIcon.getIconHeight()) / 2, this);
        g.drawImage(arrowIcon.getImage(), imageIcon.getIconWidth() + GAP, (imageHeight - arrowIcon.getIconHeight()) / 2, this);
        return new ImageIcon(newImage);
    }
}
