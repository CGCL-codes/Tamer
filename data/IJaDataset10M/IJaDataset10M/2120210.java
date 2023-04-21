package org.openmeetings.batik;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import junit.framework.TestCase;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.openmeetings.servlet.outputhandler.ExportToImage;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestSVGExporter extends TestCase {

    private static final Logger log = Logger.getLogger(TestSVGExporter.class);

    public void testGetDiagramList() {
        try {
            ExportToImage exportToImageTest = new ExportToImage();
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
            Document document = domImpl.createDocument(svgNS, "svg", null);
            Element svgRoot = document.getDocumentElement();
            svgRoot.setAttributeNS(null, "width", "2400");
            svgRoot.setAttributeNS(null, "height", "1600");
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            SVGGraphics2D svgGenerator8 = new SVGGraphics2D(svgGenerator);
            boolean useCSS = true;
            String requestedFile = "diagram_xyz_" + new Date().getTime() + ".svg";
            Writer out = new OutputStreamWriter(System.out, "UTF-8");
            svgGenerator.stream(out, useCSS);
        } catch (Exception er) {
            log.error("ERROR ", er);
            System.out.println("Error exporting: " + er);
            er.printStackTrace();
        }
    }
}
