package test;

import ch.randelshofer.quaqua.osx.OSXAquaPainter;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import static java.lang.Math.*;

/**
 * OSXPainterTest.
 *
 * @author Werner Randelshofer
 * @version 1.0 2011-07-26 Created.
 */
public class OSXPainterTest extends javax.swing.JPanel {

    private static class Canvas extends JPanel {

        private BufferedImage image;

        private OSXAquaPainter painter;

        public Canvas() {
            System.out.println("OSXPainterTest nativeCodeAvailable=" + OSXAquaPainter.isNativeCodeAvailable());
            painter = new OSXAquaPainter();
            painter.setWidget(OSXAquaPainter.Widget.buttonCheckBox);
            painter.setValueByKey(OSXAquaPainter.Key.value, 1.0);
            painter.setValueByKey(OSXAquaPainter.Key.focused, 1.0);
        }

        static float[] gaussian(float radius, float s, float sum) {
            int r = (int) Math.ceil(radius);
            float[] gaussian = new float[r * 2 + 1];
            float h = 1f;
            float c = r;
            float invs2sq = 1f / (2f * s * s);
            for (int i = 0; i < gaussian.length; i++) {
                float x = i;
                gaussian[i] = (float) (h * exp(-pow(x - c, 2) * invs2sq));
            }
            normalizeKernel(gaussian, sum);
            System.out.println("g=" + Arrays.toString(gaussian));
            return gaussian;
        }

        static float[] pyramid(float radius, float sum) {
            int r = (int) Math.ceil(radius);
            float[] gaussian = new float[r * 2 + 1];
            float c = r;
            for (int i = 0; i < gaussian.length; i++) {
                float x = i;
                gaussian[i] = (float) c - abs(x - c);
            }
            normalizeKernel(gaussian, sum);
            System.out.println("p=" + Arrays.toString(gaussian));
            return gaussian;
        }

        /** Normalizes the kernel so that all its elements add up to the given
         * sum. 
         * 
         * @param kernel
         * @param sum 
         */
        static void normalizeKernel(float[] kernel, float sum) {
            float total = 0;
            for (int i = 0; i < kernel.length; i++) {
                total += kernel[i];
            }
            if (abs(total) > 1e-20) {
                total = sum / total;
                for (int i = 0; i < kernel.length; i++) {
                    kernel[i] *= total;
                }
            }
        }

        @Override
        public void paintComponent(Graphics grr) {
            Graphics2D gr = (Graphics2D) grr;
            int w = getWidth(), h = getHeight();
            if (image == null || image.getWidth() != w || image.getHeight() != h) {
                image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
            }
            int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            painter.paint(image, 6, 6, 32, 20);
            gr.drawImage(image, 0, 0, this);
            gr.drawImage(image, 40, 0, this);
            gr.drawImage(image, 80, 0, this);
            gr.drawImage(image, 120, 0, this);
            float intensity = 1.8f;
            int blur = 7;
            final float blurry = intensity / (blur * blur);
            final float[] blurKernel = new float[blur * blur];
            for (int i = 0; i < blurKernel.length; i++) {
                blurKernel[i] = blurry;
            }
            ConvolveOp blurOp = new ConvolveOp(new Kernel(blur, blur, blurKernel));
            ConvolveOp sobelTL = new ConvolveOp(new Kernel(3, 3, (new float[] { 2, 1, 0, 1, 0, -1, 0, -1, -2 })));
            ConvolveOp sobelBR = new ConvolveOp(new Kernel(3, 3, (new float[] { -2, -1, 0, -1, 0, 1, 0, 1, 2 })));
            ConvolveOp edgeLeftOp = new ConvolveOp(new Kernel(3, 1, new float[] { 1, 0, -1 }));
            ConvolveOp edgeRightOp = new ConvolveOp(new Kernel(3, 1, new float[] { -1, 0, 1 }));
            ConvolveOp edgeTopOp = new ConvolveOp(new Kernel(1, 3, new float[] { 1, 0, -1 }));
            ConvolveOp edgeBottomOp = new ConvolveOp(new Kernel(1, 3, new float[] { -1, 0, 1 }));
            float[] gaussian = gaussian(2.0f, 2.5f / 2.25f, 0.9f);
            ConvolveOp gaussianOpV = new ConvolveOp(new Kernel(1, gaussian.length, gaussian));
            ConvolveOp gaussianOpH = new ConvolveOp(new Kernel(gaussian.length, 1, gaussian));
            float[] pyramid = pyramid(2.5f, 0.8f);
            ConvolveOp pyramidOpV = new ConvolveOp(new Kernel(1, pyramid.length, pyramid));
            ConvolveOp pyramidOpH = new ConvolveOp(new Kernel(pyramid.length, 1, pyramid));
            Graphics2D g;
            BufferedImage focusImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
            g = focusImg.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setColor(new Color(0, true));
            g.fillRect(0, 0, w, h);
            g.setComposite(AlphaComposite.SrcOver);
            g.drawImage(image, edgeLeftOp, 0, 0);
            g.drawImage(image, edgeRightOp, 0, 0);
            g.drawImage(image, edgeTopOp, 0, 0);
            g.drawImage(image, edgeBottomOp, 0, 0);
            g.setComposite(AlphaComposite.SrcIn);
            g.setColor(UIManager.getColor("Focus.color"));
            g.fillRect(0, 0, w, h);
            g.dispose();
            gr.drawImage(focusImg, 40, 0, null);
            gr.drawImage(focusImg, gaussianOpH, 80, 0);
            gr.drawImage(focusImg, gaussianOpV, 80, 0);
            gr.drawImage(focusImg, pyramidOpH, 120, 0);
            gr.drawImage(focusImg, pyramidOpV, 120, 0);
        }
    }

    private Canvas canvas;

    /** Creates new form OSXPainterTest */
    public OSXPainterTest() {
        initComponents();
        add(canvas = new Canvas());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame f = new JFrame("OSXPainterTest");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.add(new OSXPainterTest());
                f.setSize(400, 400);
                f.setVisible(true);
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }
}
