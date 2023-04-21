package javaaxp.swingviewer.service.impl.viewer.brushes;

import java.awt.Graphics2D;
import java.awt.PaintContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.util.Vector;
import javaaxp.core.service.IXPSAccess;
import javaaxp.core.service.IXPSIterator;
import javaaxp.core.service.XPSError;
import javaaxp.core.service.model.document.page.IVisualBrush;
import javaaxp.swingviewer.IXPSRenderingExtension;
import javaaxp.swingviewer.service.impl.rendering.AWTXPSRenderer;
import javaaxp.swingviewer.service.impl.rendering.FontLoader;
import javaaxp.swingviewer.service.impl.rendering.ImageLoader;

public class AWTXPSVisualPaintContext extends AWTXPSTilingPaintContext {

    private IVisualBrush fVisualBrush;

    private FontLoader fFontLoader;

    private ImageLoader fImageLoader;

    private IXPSAccess fXPSAccess;

    public AWTXPSVisualPaintContext(ColorModel cm, PaintContext opacityPaintContext, AffineTransform xform, IVisualBrush visualBrush, FontLoader fontLoader, ImageLoader imageLoader, Rectangle2D locationOfFirstTileToRender, AffineTransform brushTransform, Rectangle2D userBounds, IXPSAccess access) throws XPSError {
        super(cm, opacityPaintContext, xform, brushTransform, userBounds, visualBrush.getViewport(), visualBrush.getViewbox(), visualBrush.getTileMode());
        fFontLoader = fontLoader;
        fImageLoader = imageLoader;
        fVisualBrush = visualBrush;
        fXPSAccess = access;
    }

    @Override
    protected void renderSingleTile(Graphics2D singleTileGraphics) {
        try {
            AWTXPSRenderer visualBrushRenderer = new AWTXPSRenderer(fFontLoader, fImageLoader, singleTileGraphics, fXPSAccess, new Vector<IXPSRenderingExtension>());
            IXPSIterator iterator = fXPSAccess.getVisualElementIterator(fVisualBrush);
            iterator.accept(visualBrushRenderer);
        } catch (XPSError e) {
            e.printStackTrace();
        }
    }
}
