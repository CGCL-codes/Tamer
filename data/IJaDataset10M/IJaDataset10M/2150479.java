package xutils.imageview;

import xmage.raster.Image;
import xmage.raster.awt.ImagePanel;
import xmage.raster.codec.PNMInputCodec;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Utility for viewing PNM (PBM, PGM and PPM) images.
 */
public class PNMView {

    public PNMView(String filename) {
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
        PNMInputCodec codec = new PNMInputCodec();
        Image image = null;
        try {
            if (!codec.canRead(in)) {
                JOptionPane.showMessageDialog(null, "File " + filename + " is not a PNM file!", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            image = codec.read(in);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while reading file  " + filename + ":\n" + ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        final JFrame frame = new JFrame("PNM: " + filename);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ImagePanel(image));
        frame.pack();
        frame.setVisible(true);
    }

    private static void usage() {
        System.out.println("\n" + "Usage: java xutils.imageview.PNMView <filename>\n" + "\n");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        }
        new PNMView(args[0]);
    }
}
