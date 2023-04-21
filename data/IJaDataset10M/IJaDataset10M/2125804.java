package com.ouroboroswiki.core.content.svg;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMSVGElement;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.w3c.dom.svg.SVGDocument;
import com.ouroboroswiki.core.AbstractProxiedContent;
import com.ouroboroswiki.core.Content;
import com.ouroboroswiki.core.ContentException;

public class SVGToImageContentAdapter extends AbstractProxiedContent {

    private static final Logger log = Logger.getLogger(SVGToImageContentAdapter.class.getName());

    private String outputMimeType;

    private String informalImageType;

    private String requestPath;

    public SVGToImageContentAdapter(Content proxied, String outputMimeType, String informalImageType, String requestPath) {
        super(proxied);
        this.outputMimeType = outputMimeType;
        this.informalImageType = informalImageType;
        this.requestPath = requestPath;
    }

    @Override
    public String getMimeType() {
        return this.outputMimeType;
    }

    @Override
    public void write(OutputStream outs) throws IOException, ContentException {
        ByteArrayOutputStream svgOutputStream = new ByteArrayOutputStream();
        this.proxied.write(svgOutputStream);
        try {
            convertToImage(svgOutputStream.toByteArray(), outs);
        } catch (Exception ex) {
            log.log(Level.INFO, "unable to convert " + new String(svgOutputStream.toByteArray()), ex);
            throw new ContentException("unable to convert svg", ex);
        }
    }

    private final void convertToImage(byte[] svg, OutputStream outs) throws Exception {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        SVGDocument svgDocument = f.createSVGDocument(requestPath, new ByteArrayInputStream(svg));
        UserAgentAdapter userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        GVTBuilder builder = new GVTBuilder();
        BridgeContext ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);
        GraphicsNode rootGN = builder.build(ctx, svgDocument);
        rootGN.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rootGN.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        rootGN.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rootGN.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        SVGOMSVGElement element = (SVGOMSVGElement) svgDocument.getDocumentElement();
        int width = (int) Math.ceil(element.getWidth().getBaseVal().getValueInSpecifiedUnits());
        int height = (int) Math.ceil(element.getHeight().getBaseVal().getValueInSpecifiedUnits());
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = GraphicsUtil.createGraphics(image);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        rootGN.paint(g);
        g.dispose();
        ImageIO.write(image, this.informalImageType, outs);
    }
}
