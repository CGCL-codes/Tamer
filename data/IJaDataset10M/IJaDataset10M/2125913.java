package UniKrak.SVG;

import UniKrak.Config.Configuration;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import org.w3c.dom.svg.SVGDocument;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.*;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import UniKrak.Graph.CoordSet;

public class SVGRender {

    private static SVGRender _instance = null;

    private static Configuration config = Configuration.getInstance();

    private static SVGDocument unimaps[] = new SVGDocument[config.highestFloor - config.lowestFloor + 1];

    private static final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    private static final String parser = XMLResourceDescriptor.getXMLParserClassName();

    protected SVGRender() throws Exception {
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        for (int i = 0; i < config.MapFileNames.length; i++) {
            String URI = (new File(config.appPath + config.MapFileNames[i])).toURL().toString();
            unimaps[i] = (SVGDocument) f.createDocument(URI);
        }
    }

    public static SVGRender getInstance() throws Exception {
        if (_instance == null) {
            _instance = new SVGRender();
        }
        return _instance;
    }

    public void addCircle(SVGDocument document, int x, int y, Color color, boolean thisfloor) {
        Element svgRoot = document.getDocumentElement();
        Element userdrawn = document.getElementById("userdrawn");
        if (userdrawn == null) {
            userdrawn = document.createElementNS(svgNS, "g");
            userdrawn.setAttributeNS(null, "id", "userdrawn");
        }
        int r;
        if (thisfloor) r = 13; else r = 10;
        Element circle = document.createElementNS(svgNS, "circle");
        circle.setAttributeNS(null, "cx", Integer.toString(x));
        circle.setAttributeNS(null, "cy", Integer.toString(y));
        circle.setAttributeNS(null, "r", Integer.toString(r));
        if (thisfloor) circle.setAttributeNS(null, "style", "fill:" + convertColorToHexString(color) + ";fill-opacity:0.75;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;"); else circle.setAttributeNS(null, "style", "fill:" + convertColorToHexString(color) + ";fill-opacity:0.75;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;stroke-miterlimit:4;stroke-dasharray:4,4;stroke-dashoffset:0");
        userdrawn.appendChild(circle);
        if (userdrawn.getParentNode() == null) svgRoot.appendChild(userdrawn);
    }

    public void addRoute(SVGDocument document, CoordSet[] coords, Color pathColor, boolean thisfloor) {
        Element svgRoot = document.getDocumentElement();
        Element userdrawn = document.getElementById("userdrawn");
        if (userdrawn == null) {
            userdrawn = document.createElementNS(svgNS, "g");
            userdrawn.setAttributeNS(null, "id", "userdrawn");
        }
        Element path = document.createElementNS(svgNS, "path");
        if (thisfloor) path.setAttributeNS(null, "style", "fill:none;fill-opacity:1.0;fill-rule:evenodd;stroke:" + convertColorToHexString(pathColor) + ";stroke-width:5px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"); else path.setAttributeNS(null, "style", "fill:none;fill-opacity:0.5;fill-rule:evenodd;stroke:" + convertColorToHexString(pathColor) + ";stroke-width:5px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:0.5;display:inline;stroke-miterlimit:4;stroke-dasharray:4,4;stroke-dashoffset:0");
        String pathcoords = "M " + coords[0].x + "," + coords[0].y;
        for (int i = 1; i < coords.length; i++) {
            pathcoords += " L " + coords[i].x + "," + coords[i].y;
        }
        path.setAttributeNS(null, "d", pathcoords);
        userdrawn.appendChild(path);
        if (userdrawn.getParentNode() == null) svgRoot.appendChild(userdrawn);
    }

    public void addText(SVGDocument document, int x, int y, Color color, String text) {
        Element svgRoot = document.getDocumentElement();
        Element userdrawn = document.getElementById("userdrawn");
        if (userdrawn == null) {
            userdrawn = document.createElementNS(svgNS, "g");
            userdrawn.setAttributeNS(null, "id", "userdrawn");
        }
        Element txt = document.createElementNS(svgNS, "text");
        txt.setAttributeNS(null, "x", Integer.toString(x));
        txt.setAttributeNS(null, "y", Integer.toString(y));
        txt.setAttributeNS(null, "style", "font-size:32px;font-style:normal;font-weight:normal;fill:" + convertColorToHexString(color) + ";fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;font-family:Bitstream Vera Sans");
        txt.setTextContent(text);
        userdrawn.appendChild(txt);
        if (userdrawn.getParentNode() == null) svgRoot.appendChild(userdrawn);
    }

    public void cleanMap(SVGDocument document) {
        Element svgRoot = document.getDocumentElement();
        Element userdrawn = document.getElementById("userdrawn");
        if (userdrawn != null) {
            svgRoot.removeChild(userdrawn);
        }
    }

    public SVGDocument getMap(int floor) throws Exception {
        if (floor <= unimaps.length) {
            return (SVGDocument) DOMUtilities.deepCloneDocument(unimaps[floor], SVGDOMImplementation.getDOMImplementation());
        } else {
            throw new Exception("Ingen SVGMap for den etage.");
        }
    }

    public static synchronized void renderMap(String filename, SVGDocument document, int zoom) throws Exception {
        TranscoderInput ti = new TranscoderInput(document);
        OutputStream ostream = new FileOutputStream(filename);
        TranscoderOutput output = new TranscoderOutput(ostream);
        float b = 4.0f;
        float a = -0.405f;
        float fzoom = b * (float) Math.exp(a * (float) zoom);
        PNGTranscoder t = new PNGTranscoder();
        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(1000 * fzoom));
        t.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, config.svg_background_color);
        t.transcode(ti, output);
        ostream.flush();
        ostream.close();
    }

    public static synchronized void renderMap(String filename, SVGDocument document, int zoom, Rectangle r) throws Exception {
        if (r == null) {
            renderMap(filename, document, zoom);
            return;
        }
        TranscoderInput ti = new TranscoderInput(document);
        OutputStream ostream = new FileOutputStream(filename);
        TranscoderOutput output = new TranscoderOutput(ostream);
        float b = 4.0f;
        float a = -0.405f;
        float fzoom = b * (float) Math.exp(a * (float) zoom);
        PNGTranscoder t = new PNGTranscoder();
        float svgtopngRatio = 1000.0f * fzoom / document.getRootElement().getWidth().getBaseVal().getValue();
        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(r.width));
        t.addTranscodingHint(PNGTranscoder.KEY_AOI, new Rectangle((int) (r.x), (int) (r.y), (int) (r.width / svgtopngRatio), (int) (r.height / svgtopngRatio)));
        t.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, config.svg_background_color);
        t.transcode(ti, output);
        ostream.flush();
        ostream.close();
    }

    private String convertColorToHexString(java.awt.Color c) {
        String str = Integer.toHexString(c.getRGB() & 0xFFFFFF);
        return ("#" + "000000".substring(str.length()) + str.toUpperCase());
    }
}
