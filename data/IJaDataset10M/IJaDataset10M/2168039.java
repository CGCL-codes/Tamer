package presentation;

import javax.swing.JButton;
import java.awt.Dimension;

/**
 *
 * @author Andreu Marimon
 */
public class defaultJButton extends JButton {

    public defaultJButton() {
        this.setFont(new java.awt.Font("Verdana", 1, 11));
        this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentation/imatges/button.jpg")));
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        Dimension size = new Dimension();
        size.setSize(100, 29);
        this.setMaximumSize(size);
        this.setSize(size);
        this.setMinimumSize(size);
        this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        this.setIconTextGap(0);
        this.setMargin(new java.awt.Insets(0, 0, 0, 0));
        this.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/presentation/imatges/pressedbutton.jpg")));
        this.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/presentation/imatges/hoverbutton.jpg")));
    }
}
