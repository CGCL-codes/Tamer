package com.foursoft.fourever.draw;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import com.foursoft.component.util.ImageFileDescription;
import com.foursoft.fourever.draw.impl.DrawManagerImpl;
import com.foursoft.fourever.vmodell.regular.Vorgehensbaustein;

/**
 * A diagram generator using the apache batik framework
 * for generating a png image of the vorgehensbaustein
 */
public class PNGDiagramFactory extends DiagramFactory {

    /**
   * @see DiagramGenerator::generateDiagram
   */
    @Override
    public void generateDiagram(Vorgehensbaustein v, File f, ImageFileDescription i) throws IOException {
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);
        SVGGraphics2D g = new SVGGraphics2D(document);
        DiagramDraughtsman d = new DiagramDraughtsman(v);
        int w = d.getDrawingWidth();
        int h = d.draw(g);
        g.setSVGCanvasSize(new Dimension(w, h));
        g.getRoot(document.getDocumentElement());
        PNGTranscoder t = new PNGTranscoder();
        t.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, new Float(i.getWidth()));
        TranscoderInput in = new TranscoderInput(document);
        FileOutputStream os = new FileOutputStream(f);
        TranscoderOutput out = new TranscoderOutput(os);
        try {
            t.transcode(in, out);
        } catch (TranscoderException e) {
            DrawManagerImpl.log.error("Transcoding failed!", e);
        }
        os.flush();
        os.close();
    }
}
