package hu.ihash.apps.webimagedupe.view;

import hu.ihash.hashing.util.ImageLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JComponent;

public class SimilarImage extends JComponent {

    private static final int WIDTH = 64;

    private static final int HEIGHT = 64;

    private BufferedImage image;

    private File file;

    public SimilarImage(String filename) {
        this(new File(filename), ImageLoader.loadOptimized(new File(filename), WIDTH, HEIGHT));
    }

    public SimilarImage(File file, BufferedImage image) {
        this.image = image;
        this.file = file;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        this.image = ImageLoader.loadOptimized(file, WIDTH, HEIGHT);
    }

    public BufferedImage getImage() {
        return image;
    }
}
