package net.sf.javavp8decoder.tools;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.sf.javavp8decoder.imageio.WebPImageReaderSpi;

public class plugintest extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        IIORegistry r = javax.imageio.spi.IIORegistry.getDefaultInstance();
        WebPImageReaderSpi s = new WebPImageReaderSpi();
        r.registerServiceProvider(s);
        for (String n : ImageIO.getReaderFileSuffixes()) {
            System.out.println(n);
        }
        plugintest app;
        app = new plugintest();
        app.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private BufferedImage bi;

    private JPanel jp;

    private JScrollPane sp;

    plugintest() {
        File f = new File("testdata/test.webp");
        try {
            bi = ImageIO.read(f);
            if (bi == null) {
                System.out.println("null");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        class ImagePanel extends JPanel {

            /**
			 * 
			 */
            private static final long serialVersionUID = 1L;

            private BufferedImage bi;

            public ImagePanel(BufferedImage bi) {
                this.bi = bi;
            }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bi, 0, 0, null);
            }
        }
        ;
        jp = new ImagePanel(bi);
        jp.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
        sp = new JScrollPane();
        sp.add(jp);
        sp.setViewportView(jp);
        this.add(sp);
        this.setVisible(true);
        this.setSize(1000, 1000);
    }
}
