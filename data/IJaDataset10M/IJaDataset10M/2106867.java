package com.moldflow.batik.transcoder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.awt.Dimension;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGeneratorContext;

public class SVGRenderingTranscoder extends SVGAbstractTranscoder {

    public SVGRenderingTranscoder() {
        hints.put(KEY_TEXT_AS_SHAPES, Boolean.FALSE);
    }

    public static final TranscodingHints.Key KEY_TEXT_AS_SHAPES = new BooleanKey();

    public void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
        super.transcode(document, uri, output);
        Document doc;
        if (output.getDocument() == null) {
            DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
            doc = domImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SVG_TAG, null);
        } else doc = output.getDocument();
        SVGGraphics2D svgGenerator = new SVGGraphics2D(SVGGeneratorContext.createDefault(doc), ((Boolean) hints.get(KEY_TEXT_AS_SHAPES)).booleanValue());
        Dimension d = new Dimension();
        d.setSize(width, height);
        svgGenerator.setSVGCanvasSize(d);
        this.root.paint(svgGenerator);
        try {
            OutputStream os = output.getOutputStream();
            if (os != null) {
                svgGenerator.stream(svgGenerator.getRoot(), new OutputStreamWriter(os), false, false);
                return;
            }
            Writer wr = output.getWriter();
            if (wr != null) {
                svgGenerator.stream(svgGenerator.getRoot(), wr, false, false);
                return;
            }
            String outputuri = output.getURI();
            if (outputuri != null) {
                try {
                    URL url = new URL(outputuri);
                    URLConnection urlCnx = url.openConnection();
                    os = urlCnx.getOutputStream();
                    svgGenerator.stream(svgGenerator.getRoot(), new OutputStreamWriter(os), false, false);
                    return;
                } catch (MalformedURLException e) {
                    handler.fatalError(new TranscoderException(e));
                } catch (IOException e) {
                    handler.fatalError(new TranscoderException(e));
                }
            }
        } catch (Exception ex) {
            throw new TranscoderException(ex);
        }
    }
}
