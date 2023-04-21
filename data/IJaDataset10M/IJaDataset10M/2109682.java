package org.vardb.graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.vardb.CVardbException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CPlatformType;

public final class CImageHelper {

    public enum ScaleType {

        SCALE_TO_FIT, SCALE_TO_FILL
    }

    ;

    public enum Format {

        jpeg, gif, png
    }

    ;

    private CImageHelper() {
    }

    public static BufferedImage getImage(String path) {
        try {
            URL url = new URL(path);
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new CVardbException(e);
        }
    }

    public static void displayImage(Image image) {
        JFrame frame = new JFrame();
        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static BufferedImage createImage(Container container, int width, int height) {
        container.setSize(new Dimension(width, height));
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        container.paint(graphics);
        graphics.dispose();
        return bufferedImage;
    }

    public static BufferedImage createImage(Canvas canvas, int width, int height) {
        System.setProperty("java.awt.headless", "true");
        canvas.setSize(new Dimension(width, height));
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        canvas.paint(graphics);
        graphics.dispose();
        return bufferedImage;
    }

    public static BufferedImage scaleImage(BufferedImage image, int max) {
        float width = (float) image.getWidth();
        float height = (float) image.getHeight();
        float scaledwidth = 0.0f;
        float scaledheight = 0.0f;
        float scale = 1.0f;
        if (width > height) scale = ((float) width) / (float) max; else scale = ((float) height) / (float) max;
        scaledwidth = width / scale;
        scaledheight = height / scale;
        if (scaledwidth == 0) scaledwidth = 1;
        if (scaledheight == 0) scaledheight = 1;
        return scaleImage(image, (int) scaledwidth, (int) scaledheight, ScaleType.SCALE_TO_FIT);
    }

    public static Dimension scaleImage(int width, int height, int max) {
        float scale = 1.0f;
        if (width > height) scale = ((float) width) / (float) max; else scale = ((float) height) / (float) max;
        int thumb_width = Math.round(width / scale);
        int thumb_height = Math.round(height / scale);
        return new Dimension(thumb_width, thumb_height);
    }

    public static BufferedImage scaleImage(BufferedImage src, int width, int height, ScaleType scaletype) {
        if (src.getWidth() == width && src.getHeight() == height) return src;
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage dest = new BufferedImage(width, height, type);
        Graphics2D g2 = dest.createGraphics();
        if (scaletype == ScaleType.SCALE_TO_FIT) {
            g2.setBackground(UIManager.getColor("Panel.background"));
            g2.clearRect(0, 0, width, height);
        }
        double scale = getScale(src, width, height, scaletype);
        double x = (width - scale * src.getWidth()) / 2;
        double y = (height - scale * src.getHeight()) / 2;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.scale(scale, scale);
        g2.drawRenderedImage(src, at);
        g2.dispose();
        return dest;
    }

    private static double getScale(BufferedImage image, int width, int height, ScaleType scaletype) {
        double xScale = (double) width / image.getWidth();
        double yScale = (double) height / image.getHeight();
        return (scaletype == ScaleType.SCALE_TO_FIT) ? Math.min(xScale, yScale) : Math.max(xScale, yScale);
    }

    public static BufferedImage scaleImage(BufferedImage src, int width, int height) {
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage dest = new BufferedImage(width, height, type);
        Graphics2D g2 = dest.createGraphics();
        double xScale = (double) width / src.getWidth();
        double yScale = (double) height / src.getHeight();
        double x = (width - xScale * src.getWidth()) / 2;
        double y = (height - yScale * src.getHeight()) / 2;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.scale(xScale, yScale);
        g2.drawRenderedImage(src, at);
        g2.dispose();
        return dest;
    }

    public static BufferedImage readImage(String filename) {
        try {
            return ImageIO.read(new File(filename));
        } catch (IOException e) {
            throw new CVardbException(e);
        }
    }

    public static void writeImage(BufferedImage image, Format format, String filename) {
        try {
            ImageIO.write(image, format.name(), new File(filename));
        } catch (IOException e) {
            throw new CVardbException(e);
        }
    }

    public static void writeImage(BufferedImage image, Format format, OutputStream stream) {
        try {
            ImageIO.write(image, format.name(), stream);
            stream.flush();
        } catch (IOException e) {
            throw new CVardbException(e);
        }
    }

    public static void writeImage(byte[] image, OutputStream stream) {
        try {
            stream.write(image);
            stream.flush();
        } catch (IOException e) {
            throw new CVardbException(e);
        }
    }

    public static void writeImage(String svg, String filename) {
        try {
            Reader reader = new StringReader(svg);
            OutputStream out = new FileOutputStream(filename);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            PNGTranscoder transcoder = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(reader);
            TranscoderOutput output = new TranscoderOutput(bout);
            transcoder.transcode(input, output);
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }

    public static byte[] renderSvg(String svg) {
        try {
            Reader reader = new StringReader(svg);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PNGTranscoder transcoder = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(reader);
            TranscoderOutput output = new TranscoderOutput(stream);
            transcoder.transcode(input, output);
            return stream.toByteArray();
        } catch (Exception e) {
            if (CPlatformType.find().isWindows()) CFileHelper.writeFile("d:/temp/error.svg", svg);
            throw new CVardbException(e);
        }
    }

    public static byte[] createArray(BufferedImage image, Format format) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, format.name(), stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new CVardbException(e);
        }
    }

    /** Tell system to use native look and feel, as in previous
	*  releases. Metal (Java) LAF is the default otherwise. */
    public static void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting native LAF: " + e);
        }
    }

    /** A simplified way to see a JPanel or other Container.
	*  Pops up a JFrame with specified Container as the content pane. */
    public static JFrame openInJFrame(Container content, int width, int height, String title, Color bgColor) {
        JFrame frame = new JFrame(title);
        frame.setBackground(bgColor);
        content.setBackground(bgColor);
        frame.setSize(width, height);
        frame.setContentPane(content);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        return frame;
    }

    /** Uses Color.white as the background color. */
    public static JFrame openInJFrame(Container content, int width, int height, String title) {
        return openInJFrame(content, width, height, title, Color.white);
    }

    /** Uses Color.white as the background color, and the
	*  name of the Container's class as the JFrame title. */
    public static JFrame openInJFrame(Container content, int width, int height) {
        return openInJFrame(content, width, height, content.getClass().getName(), Color.white);
    }

    public static String createSvg(SVGGraphics2D generator) {
        try {
            boolean useCSS = true;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            generator.stream(writer, useCSS);
            return new String(out.toByteArray());
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }
}
