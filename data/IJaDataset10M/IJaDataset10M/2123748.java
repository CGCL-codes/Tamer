package org.bitmetrics.bithull.kit;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JWindow;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.util.XMLResourceDescriptor;
import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;
import org.w3c.dom.Document;

/**
 * @author Rajesh
 *
 */
public class Kit extends JComponent {

    JWindow win;

    Document doc = null;

    BufferedImage img = null;

    int w, h;

    public Kit() {
    }

    public void init(String name, int width, int height) {
        win = new JWindow();
        win.setSize(width, height);
        win.getContentPane().add(this);
        setSize(width, height);
        w = width;
        h = height;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, w, h, null);
        }
    }

    public BufferedImage makeImage(int width, int height) {
        BufferedImageTranscoder t = new BufferedImageTranscoder();
        t.addTranscodingHint(BufferedImageTranscoder.KEY_QUALITY, new Float(1.0));
        t.addTranscodingHint(BufferedImageTranscoder.KEY_WIDTH, new Float(width));
        t.addTranscodingHint(BufferedImageTranscoder.KEY_HEIGHT, new Float(height));
        try {
            TranscoderInput input = new TranscoderInput(doc);
            t.transcode(input, null);
        } catch (TranscoderException e) {
            e.printStackTrace();
        }
        img = t.buff;
        return t.buff;
    }

    /**
     * @param fname
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Document loadSVG(String fname) throws FileNotFoundException, IOException {
        InputStream fis = new FileInputStream(fname);
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        doc = f.createDocument("file://junk.svg", fis);
        return doc;
    }

    /**
     * @param url
     */
    public static void openURLInBrowser(String url) {
        try {
            Desktop.browse(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (DesktopException e) {
            e.printStackTrace();
        }
    }
}
