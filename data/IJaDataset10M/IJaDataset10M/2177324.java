package org.dishevelled.piccolo.venn.examples;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.piccolo2d.PCanvas;
import org.piccolo2d.util.PPaintContext;
import org.dishevelled.piccolo.venn.BinaryVennNode;

/**
 * Binary venn node example.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class BinaryVennExample extends JPanel implements Runnable {

    /**
     * Create a new binary venn example.
     */
    public BinaryVennExample() {
        super();
        PCanvas canvas = new PCanvas();
        canvas.setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        canvas.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        canvas.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        Set<String> set0 = read("the_pioneers.txt");
        Set<String> set1 = read("the_deerslayer.txt");
        BinaryVennNode<String> node = new BinaryVennNode<String>("The Pioneers", set0, "The Deerslayer", set1);
        node.offset(150.0d, 150.0d);
        canvas.getLayer().addChild(node);
        setLayout(new BorderLayout());
        add("Center", canvas);
    }

    /** {@inheritDoc} */
    public void run() {
        JFrame f = new JFrame("Binary Venn Example");
        f.setContentPane(this);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(100, 100, 400, 400);
        f.setVisible(true);
    }

    private static Set<String> read(final String name) {
        BufferedReader reader = null;
        Set<String> result = new HashSet<String>(12000);
        try {
            reader = new BufferedReader(new InputStreamReader(BinaryVennExample.class.getResourceAsStream(name)));
            while (reader.ready()) {
                result.add(reader.readLine().trim());
            }
        } catch (IOException e) {
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * Main.
     *
     * @param args command line arguments, ignored
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new BinaryVennExample());
    }
}
