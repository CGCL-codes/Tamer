package dutchradioscrobbler.gui;

import dutchradioscrobbler.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.ReplicateScaleFilter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A JFrame with info about the application and writer.
 *
 * @author Niek Haarman
 * 11-feb-2010 21:43:16
 */
public class AboutFrame extends JFrame {

    /**
     * The Singleton instance for this AboutFrame.
     */
    private static AboutFrame aboutFrame = new AboutFrame();

    private Image logoBig, gplLogo;

    /**
     * Returns the only possible instance of AboutFrame.
     * To be used instead of the constructor.
     * @return the instance of AboutFrame.
     * @ensures result != null.
     */
    public static AboutFrame getInstance() {
        return aboutFrame;
    }

    /**
     * Creates a new AboutFrame.
     * This method can't be invoked from outside this class,
     * use getInstance() instead.
     */
    private AboutFrame() {
        super(Constants.APPNAME + " - About");
        setSize(630, 270);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setResizable(false);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        try {
            logoBig = ImageIO.read(new File("images/logo_rectangle.png"));
            ImageFilter replicate = new ReplicateScaleFilter(553, 230);
            ImageProducer prod = new FilteredImageSource(logoBig.getSource(), replicate);
            logoBig = createImage(prod);
            gplLogo = ImageIO.read(new File("images/gplv3.png"));
            setBackground(Color.WHITE);
            BufferedImage image = null;
            image = ImageIO.read(new File("images/logo_square.png"));
            setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Paints the images and text on the JFrame.
     */
    @Override
    public void paint(Graphics g) {
        int txt = 20;
        g.drawImage(logoBig, 25, 35, this);
        g.drawImage(gplLogo, 520, 230, this);
        g.setColor(Color.BLACK);
        g.setFont(new Font("sansserif", Font.BOLD, 16));
        g.drawString(Constants.APPNAME, txt, 65);
        g.setFont(new Font("sansserif", Font.PLAIN, 10));
        g.drawString("Build " + Constants.BUILD, txt, 78);
        g.drawString("Copyright © 2010 Niek Haarman.", txt, 89);
        g.drawString("All rights reserved.", txt, 100);
    }
}
