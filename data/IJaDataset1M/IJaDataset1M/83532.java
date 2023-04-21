package self.amigo.elem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import self.awt.ATextRenderInfoAdapter;
import self.awt.ITextRenderInfo;
import self.awt.PainterManager;
import self.awt.painter.TextPainter;
import self.gee.IDiagramLayer;
import self.lang.StringUtils;
import self.xml.DomUtils;

public class InputBoxView extends ARectangularHandledElement {

    /**
	 * If the internal state of this class ever changes in such a way that it can't be defaulted,
	 * then the {@link #serialVersionUID} should be incremented to ensure serialized instances cleanly fail.  
	 */
    private static final long serialVersionUID = 1;

    public static final int SINGLE_LINE_EDIT = 0;

    public static final int COMBO_BOX = 1;

    public static final int MEMO_EDIT = 2;

    public static final int NO_SCROLLBARS = 0;

    public static final int HORIZ_SCROLLBAR = 1;

    public static final int VERT_SCROLLBAR = 2;

    public static final int BOTH_SCROLLBARS = 3;

    public static final String TEXT_PROP = "text";

    public static final String TYPE_PROP = "type";

    public static final String SCROLLBAR_PROP = "scrollBars";

    public static final String AID_PROP = "aid";

    private static final Font textFont = new Font("Arial", Font.PLAIN, 12);

    private String contents = "edit text";

    private int type = SINGLE_LINE_EDIT;

    private int scroller = NO_SCROLLBARS;

    private String aidLink;

    protected transient InputBoxPaintContext inputContext = new InputBoxPaintContext();

    protected TextPainter textPainter;

    public InputBoxView() {
        fig.setBounds(10, 10, 140, 20);
        inputContext.resetContentForType();
        resetFrame();
    }

    public void setLayer(IDiagramLayer layerToDrawOn, boolean add) {
        super.setLayer(layerToDrawOn, add);
        textPainter = (TextPainter) PainterManager.getPainterForOwner(layer, TextPainter.class);
    }

    public void getProperties(Map store) {
        store.put(TEXT_PROP, contents);
        store.put(TYPE_PROP, new Integer(type));
        store.put(SCROLLBAR_PROP, new Integer(scroller));
        store.put(AID_PROP, aidLink);
    }

    public void setProperties(Map data) {
        contents = (String) data.get(TEXT_PROP);
        type = ((Integer) data.get(TYPE_PROP)).intValue();
        scroller = ((Integer) data.get(SCROLLBAR_PROP)).intValue();
        aidLink = (String) data.get(AID_PROP);
        inputContext.resetContentForType();
        resetFrame();
        layer.setDirty();
    }

    public void readFrom(Node self, HashMap idObjLookUp) throws DOMException {
        super.readFrom(self, idObjLookUp);
        contents = DomUtils.getElementText(self);
        contents = StringUtils.toEmptyIfNull(contents);
        type = Integer.parseInt(DomUtils.getElementAttribute(self, TYPE_PROP));
        scroller = Integer.parseInt(DomUtils.getElementAttribute(self, SCROLLBAR_PROP));
        aidLink = DomUtils.getElementAttribute(self, AID_PROP, null);
        inputContext.resetContentForType();
        resetFrame();
    }

    public void writeTo(Document doc, Element self, HashMap objIdLookup) throws DOMException {
        super.writeTo(doc, self, objIdLookup);
        DomUtils.setElementText(self, StringUtils.toEmptyIfNull(contents));
        DomUtils.setElementAttribute(self, TYPE_PROP, "" + type);
        DomUtils.setElementAttribute(self, SCROLLBAR_PROP, "" + scroller);
        if (!StringUtils.isNullOrEmpty(aidLink)) DomUtils.setElementAttribute(self, AID_PROP, StringUtils.toEmptyIfNull(aidLink));
    }

    public void paint(Graphics surface) {
        if (outlineOnly) {
            surface.setColor(frameColor);
            surface.drawRect(fig.x, fig.y, fig.width, fig.height);
            return;
        }
        boolean paintSymbol = aidLink != null;
        if (paintSymbol) {
            surface.setColor(Color.white);
            SymbolPaintManagerUtils.startSymbol(surface, this, aidLink);
        }
        try {
            paintTextArea(surface);
            paintDecorations(surface);
        } finally {
            if (paintSymbol) SymbolPaintManagerUtils.endSymbol(surface);
        }
    }

    private void paintTextArea(Graphics g) {
        int width = fig.width;
        int height = fig.height;
        if (type != SINGLE_LINE_EDIT) {
            if (type == COMBO_BOX) width -= ScrollbarView.BUTTON_THICKNESS; else {
                if (scroller > NO_SCROLLBARS) {
                    if ((scroller == HORIZ_SCROLLBAR) || (scroller == BOTH_SCROLLBARS)) height -= ScrollbarView.BUTTON_THICKNESS;
                    if ((scroller == VERT_SCROLLBAR) || (scroller == BOTH_SCROLLBARS)) width -= ScrollbarView.BUTTON_THICKNESS;
                }
            }
        }
        g.setColor(Color.white);
        g.fillRect(fig.x, fig.y, width, height);
        g.setColor(Color.black);
        g.drawRect(fig.x, fig.y, width, height);
        textPainter.paint(g, inputContext, false);
    }

    private void paintDecorations(Graphics g) {
        if (type != SINGLE_LINE_EDIT) {
            if (type == COMBO_BOX) paintComboBox(g); else if (scroller > NO_SCROLLBARS) paintMemoScrollbars(g);
        }
    }

    private void paintComboBox(Graphics g) {
        int x1, y1, x2, y2;
        x2 = fig.x + fig.width;
        x1 = x2 - ScrollbarView.BUTTON_THICKNESS;
        y1 = fig.y;
        y2 = y1 + fig.height;
        ScrollbarView.paintScrollerButton(g, x1, y1, x2, y2, 2);
        g.drawRect(x1, y1, x2 - x1, y2 - y1);
    }

    private void paintMemoScrollbars(Graphics g) {
        int b1x1, b1y1, b1x2, b1y2, b2x1, b2y1, b2x2, b2y2;
        if (scroller == BOTH_SCROLLBARS) {
            b1y2 = fig.y + fig.height;
            b1x1 = fig.x;
            b1y1 = b1y2 - ScrollbarView.BUTTON_THICKNESS;
            b1x2 = fig.x + fig.width - ScrollbarView.BUTTON_THICKNESS;
            ScrollbarView.paintScroller(g, b1x1, b1y1, b1x2, b1y2, true);
            b2x2 = fig.x + fig.width;
            b2x1 = b2x2 - ScrollbarView.BUTTON_THICKNESS;
            b2y1 = fig.y;
            b2y2 = fig.y + fig.height - ScrollbarView.BUTTON_THICKNESS;
            ScrollbarView.paintScroller(g, b2x1, b2y1, b2x2, b2y2, false);
            g.setColor(Color.black);
            g.drawRect(fig.x, fig.y, fig.width, fig.height);
        } else if (scroller == HORIZ_SCROLLBAR) {
            b1y2 = fig.y + fig.height;
            b1x1 = fig.x;
            b1y1 = b1y2 - ScrollbarView.BUTTON_THICKNESS;
            b1x2 = fig.x + fig.width;
            ScrollbarView.paintScroller(g, b1x1, b1y1, b1x2, b1y2, true);
        } else {
            b2x2 = fig.x + fig.width;
            b2x1 = b2x2 - ScrollbarView.BUTTON_THICKNESS;
            b2y1 = fig.y;
            b2y2 = fig.y + fig.height;
            ScrollbarView.paintScroller(g, b2x1, b2y1, b2x2, b2y2, false);
        }
    }

    protected void resetFrame() {
        inputContext.resetPaintContext();
    }

    protected class InputBoxPaintContext extends ATextRenderInfoAdapter {

        private Rectangle bounds = new Rectangle();

        private String contentForType;

        public void resetContentForType() {
            if (type != MEMO_EDIT) {
                int pos = contents.indexOf("\n");
                if (pos > -1) contentForType = contents.substring(0, pos); else contentForType = contents;
                format = ITextRenderInfo.TRIM_FORMAT;
            } else {
                contentForType = contents;
                format = ITextRenderInfo.WRAPPED_FORMAT;
            }
        }

        public void resetPaintContext() {
            bounds.setBounds(fig);
            if (type != MEMO_EDIT) {
                if (type == COMBO_BOX) bounds.width -= ScrollbarView.BUTTON_THICKNESS;
            } else {
                if ((scroller == HORIZ_SCROLLBAR) || (scroller == BOTH_SCROLLBARS)) bounds.height -= ScrollbarView.BUTTON_THICKNESS;
                if ((scroller == VERT_SCROLLBAR) || (scroller == BOTH_SCROLLBARS)) bounds.width -= ScrollbarView.BUTTON_THICKNESS;
            }
            bounds.grow(-2, (type == COMBO_BOX ? -1 : -2));
        }

        public String getText() {
            return contentForType;
        }

        public Rectangle getBounds() {
            return bounds;
        }

        public Font getFont() {
            return textFont;
        }

        public Color getColor() {
            return frameColor;
        }
    }
}
