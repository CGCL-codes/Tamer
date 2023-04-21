package de.fu_berlin.inf.gmanda.gui.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import javax.swing.JComponent;
import javax.swing.RepaintManager;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.CachedImageHandlerBase64Encoder;
import org.apache.batik.svggen.GenericImageHandler;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.picocontainer.annotations.Inject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;

public class SVGScreenshotTaker {

    @Inject
    CommonService commonService;

    @Inject
    ScreenshotFileChooser fileChooser;

    public String getScreenshot(JComponent c) {
        Writer out = new StringWriter();
        try {
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);
            SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
            ctx.setEmbeddedFontsOn(true);
            GenericImageHandler ihandler = new CachedImageHandlerBase64Encoder();
            ctx.setGenericImageHandler(ihandler);
            SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, false);
            RepaintManager.currentManager(c).setDoubleBufferingEnabled(false);
            c.paint(svgGenerator);
            RepaintManager.currentManager(c).setDoubleBufferingEnabled(true);
            boolean useCSS = true;
            svgGenerator.stream(out, useCSS);
        } catch (SVGGraphics2DIOException e) {
            throw new ReportToUserException(e);
        }
        return out.toString();
    }

    public void screenshotToFile(final JComponent c) {
        String svg = getScreenshot(c);
        File saveFile = fileChooser.getSaveFile();
        if (saveFile != null) {
            OutputStreamWriter out;
            try {
                out = new OutputStreamWriter(new FileOutputStream(saveFile), "UTF-8");
                out.write(svg);
                out.close();
            } catch (Exception e) {
                throw new ReportToUserException(e);
            }
        }
    }
}
