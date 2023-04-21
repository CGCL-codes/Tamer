package ihmManager;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import main.ILanguage;

/**
 * 
 * @author Imad BOU-SAID
 * @since 31/01/2010
 * @lastUpdate 14/02/2010
 *
 * the pop up logo
 */
public class PopUpLogo extends JFrame implements ILanguage {

    private static final long serialVersionUID = 7421654926340573775L;

    public PopUpLogo() {
        super(TITLE);
        this.setSize(700, 200);
        this.setLocation(200, 200);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container c = getContentPane();
        JPanel imagePanel = new JPanel() {

            private static final long serialVersionUID = -999420458759218851L;

            public void paint(Graphics g) {
                try {
                    Image image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource("/ressources/img/"), "bde_connect.jpg"));
                    g.drawImage(image, 0, 0, null);
                } catch (IOException e) {
                    System.out.println("N'OUBLIEZ PAS DE CHANGER LE PATH DANS LA CLASSE FirstWindow.MERCI.");
                }
            }
        };
        imagePanel.setPreferredSize(new Dimension(700, 200));
        c.add(imagePanel);
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
    }
}
