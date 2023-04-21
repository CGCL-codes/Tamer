package org.attacmadrid.sgss.application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author ivan
 */
public class SGSSApplication {

    private static final SGSSApplication instance = new SGSSApplication();

    private static final String[] EXTENSIONS = { ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".JPG", ".JPEG", ".PNG", ".GIF", ".BMP", "" };

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        SGSSApplication.getInstance().launch();
    }

    public static SGSSApplication getInstance() {
        return instance;
    }

    private Image unloadedImage;

    private SGSSMainFrame mainFrame;

    public SGSSApplication() {
        BufferedImage bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5));
        g.drawLine(0, 0, 64, 64);
        g.drawLine(64, 0, 0, 64);
        unloadedImage = bi;
    }

    public void launch() {
        System.setProperty("swing.aatext", "true");
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.attac_sgss";
        System.setProperty("derby.system.home", systemDir);
        mainFrame = new SGSSMainFrame();
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                mainFrame.setVisible(true);
            }
        });
    }

    public SGSSMainFrame getMainFrame() {
        return mainFrame;
    }

    public Icon loadIcon(String name) {
        return new ImageIcon(loadImage(name));
    }

    public Image loadImage(String name) {
        if (name != null) {
            for (String ext : EXTENSIONS) {
                try {
                    URL url = getClass().getClassLoader().getResource("images/" + name + ext);
                    if (url == null) {
                        continue;
                    }
                    return ImageIO.read(url);
                } catch (IOException e) {
                }
            }
        }
        return unloadedImage;
    }

    public List<String> loadList(String name) {
        List<String> ret = new ArrayList<String>();
        try {
            URL url = getClass().getClassLoader().getResource("lists/" + name + ".utf-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                ret.add(line);
            }
            reader.close();
        } catch (IOException e) {
            showError("No se puede cargar la lista de valores: " + name, e);
        }
        return ret;
    }

    public void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(mainFrame, message);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public void exit(int sc) {
        System.exit(sc);
    }
}
