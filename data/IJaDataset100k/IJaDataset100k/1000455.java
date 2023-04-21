package net.sourceforge.toscanaj.canvas.imagewriter;

import net.sourceforge.toscanaj.canvas.DrawingCanvas;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Saves a DrawingCanvas as SVG graphic.
 */
public class BatikImageWriter implements ImageWriter {

    /**
     * A format representing SVG.
     */
    protected static class GraphicFormatSVG extends GraphicFormat {

        /**
         * Implements GraphicFormat.getName().
         */
        public String getName() {
            return "Scalable Vector Graphics";
        }

        /**
         * Implements GraphicFormat.getExtensions().
         */
        public String[] getExtensions() {
            String[] retVal = new String[1];
            retVal[0] = "svg";
            return retVal;
        }

        /**
         * Implements GraphicFormat.getWriter().
         */
        public ImageWriter getWriter() {
            return singleton;
        }
    }

    /**
     * The only instance of this class.
     */
    private static BatikImageWriter singleton;

    /**
     * We use a singleton approach, no public constructor.
     */
    private BatikImageWriter() {
    }

    /**
     * Registers our graphic format and sets up the instance.
     */
    public static void initialize() {
        singleton = new BatikImageWriter();
        GraphicFormatRegistry.registerType(new GraphicFormatSVG());
    }

    /**
     * Saves the canvas using the settings to the file.
     */
    public void exportGraphic(DrawingCanvas canvas, DiagramExportSettings settings, File outputFile) throws ImageGenerationException {
        if (settings.usesAutoMode()) {
            settings.setImageSize(canvas.getWidth(), canvas.getHeight());
        }
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(null, "svg", null);
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        svgGenerator.setSVGCanvasSize(new Dimension(settings.getImageWidth(), settings.getImageHeight()));
        Rectangle2D bounds = new Rectangle2D.Double(0, 0, settings.getImageWidth(), settings.getImageHeight());
        svgGenerator.setPaint(canvas.getBackground());
        svgGenerator.fill(bounds);
        AffineTransform transform = canvas.scaleToFit(svgGenerator, bounds);
        svgGenerator.transform(transform);
        canvas.paintCanvas(svgGenerator);
        boolean useCSS = true;
        try {
            FileOutputStream outStream = new FileOutputStream(outputFile);
            Writer out = new OutputStreamWriter(outStream, "UTF-8");
            svgGenerator.stream(out, useCSS);
            outStream.close();
        } catch (Exception e) {
            throw new ImageGenerationException("Error while generating '" + outputFile.getPath() + "' - writing SVG error: " + e.getMessage(), e);
        }
    }
}
