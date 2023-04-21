package edu.udo.scaffoldhunter.view.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import edu.udo.scaffoldhunter.util.FileType;

/**
 * Generates a SVGDocument by converting everything painted to the Graphics
 * context. An instance of this class can only be used once.
 * <p>
 * The SVGDocument can then be transcoded into several Formats
 * 
 * @author Henning Garus
 */
public class SVGGenerator {

    private final SVGGraphics2D svgGenerator;

    private final SVGDocument document;

    /**
     * Standard Constructor
     */
    public SVGGenerator() {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        document = (SVGDocument) impl.createDocument(svgNS, "svg", null);
        svgGenerator = new SVGGraphics2D(document);
    }

    /**
     * Everything that is painted to this graphics context will directly affect
     * the SVGDocument.
     * 
     * @return a Graphics object used to paint "on" the <code>SVGDocument</code>
     * 
     * @see #getSVGDocument
     */
    public Graphics2D getGraphics() {
        return svgGenerator;
    }

    /**
     * Returns an SVGDocument that contains all the content painted to the
     * graphics context in svg format.
     * 
     * @return the SVGDocument
     */
    public SVGDocument getSVGDocument() {
        Element root = document.getDocumentElement();
        svgGenerator.getRoot(root);
        return document;
    }

    /**
     * Sets the size of the root svg element.
     * 
     * @param width
     * @param height
     */
    public void setSVGCanvasSize(int width, int height) {
        svgGenerator.setSVGCanvasSize(new Dimension(width, height));
    }

    private void transcode(OutputStream ostream, FileType type) throws TranscoderException {
        Transcoder t;
        TranscoderOutput output;
        switch(type) {
            case PNG:
                t = new PNGTranscoder();
                output = new TranscoderOutput(ostream);
                break;
            case SVG:
                t = new SVGTranscoder();
                output = new TranscoderOutput(new OutputStreamWriter(ostream));
                break;
            default:
                throw new IllegalArgumentException("unsupported file type");
        }
        t.transcode(new TranscoderInput(getSVGDocument()), output);
    }

    /**
     * Creates a new Image file showing the content of the graphics context.
     * 
     * @param file
     *            the file
     * @param type
     *            The file type. If transcoding to this file type is not
     *            supported an <code>IllegalArgumentException</code> will be
     *            thrown. Supported File types are <code>SVG</code>, 
     *            <code>TIFF</code> and <code>PNG</code>.
     * @throws TranscoderException
     *            If an error occurs during transcoding or writing.
     */
    public void transcode(File file, FileType type) throws TranscoderException {
        OutputStream ostream;
        try {
            ostream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new TranscoderException(e);
        }
        transcode(ostream, type);
        try {
            ostream.close();
        } catch (IOException e) {
            throw new TranscoderException(e);
        }
    }

    /**
     * Writes the document in a xml file (used for testing only).
     */
    @Override
    public String toString() {
        boolean useCSS = true;
        Writer out = new StringWriter();
        try {
            svgGenerator.stream(out, useCSS);
        } catch (SVGGraphics2DIOException e) {
            e.printStackTrace();
            return "";
        }
        return out.toString();
    }
}
