package xutils.imageview;

import xmage.io.FormatNotSupportedException;
import xmage.raster.Image;
import xmage.raster.awt.ImagePanel;
import xmage.raster.codec.BMPInputCodec;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Utility for viewing BMP images.
 */
public class BMPView {

    public BMPView(String filename) {
        File in = new File(filename);
        if (!in.exists()) {
            JOptionPane.showMessageDialog(null, "File " + filename + " does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        if (in.isDirectory()) {
            JOptionPane.showMessageDialog(null, "File " + filename + " is a directory!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        if (!in.canRead()) {
            JOptionPane.showMessageDialog(null, "File " + filename + " cannot be read!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        BMPInputCodec codec = new BMPInputCodec();
        Image image = null;
        try {
            image = codec.read(in);
        } catch (FormatNotSupportedException fnse) {
            JOptionPane.showMessageDialog(null, "File " + filename + " is not a BMP file!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error while reading file  " + filename + ":\n" + ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        final JFrame frame = new JFrame("BMP: " + filename);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ImagePanel(image));
        frame.pack();
        frame.setVisible(true);
    }

    private static void usage() {
        System.out.println("\n" + "Usage: java xutils.imageview.BMPView <filename>\n" + "\n");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        }
        new BMPView(args[0]);
    }
}
