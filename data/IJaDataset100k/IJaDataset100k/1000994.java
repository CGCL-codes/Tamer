package org.apache.batik.bridge;

import java.awt.Cursor;
import org.apache.batik.dom.events.AbstractEvent;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.svg.SVGAElement;

/**
 * Bridge class for the &lt;a> element.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @version $Id: SVGAElementBridge.java,v 1.1 2005/11/21 09:51:18 dev Exp $
 */
public class SVGAElementBridge extends SVGGElementBridge {

    /**
     * Constructs a new bridge for the &lt;a> element.
     */
    public SVGAElementBridge() {
    }

    /**
     * Returns 'a'.
     */
    public String getLocalName() {
        return SVG_A_TAG;
    }

    /**
     * Returns a new instance of this bridge.
     */
    public Bridge getInstance() {
        return new SVGAElementBridge();
    }

    /**
     * Builds using the specified BridgeContext and element, the
     * specified graphics node.
     *
     * @param ctx the bridge context to use
     * @param e the element that describes the graphics node to build
     * @param node the graphics node to build
     */
    public void buildGraphicsNode(BridgeContext ctx, Element e, GraphicsNode node) {
        super.buildGraphicsNode(ctx, e, node);
        if (ctx.isInteractive()) {
            EventTarget target = (EventTarget) e;
            EventListener l = new AnchorListener(ctx.getUserAgent());
            target.addEventListener(SVG_EVENT_CLICK, l, false);
            ctx.storeEventListener(target, SVG_EVENT_CLICK, l, false);
            l = new CursorMouseOverListener(ctx.getUserAgent());
            target.addEventListener(SVG_EVENT_MOUSEOVER, l, false);
            ctx.storeEventListener(target, SVG_EVENT_MOUSEOVER, l, false);
            l = new CursorMouseOutListener(ctx.getUserAgent());
            target.addEventListener(SVG_EVENT_MOUSEOUT, l, false);
            ctx.storeEventListener(target, SVG_EVENT_MOUSEOUT, l, false);
        }
    }

    /**
     * Returns true as the &lt;a> element is a container.
     */
    public boolean isComposite() {
        return true;
    }

    /**
     * To handle a click on an anchor.
     */
    public static class AnchorListener implements EventListener {

        protected UserAgent userAgent;

        public AnchorListener(UserAgent ua) {
            userAgent = ua;
        }

        public void handleEvent(Event evt) {
            if (AbstractEvent.getEventPreventDefault(evt)) return;
            SVGAElement elt = (SVGAElement) evt.getCurrentTarget();
            Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            userAgent.setSVGCursor(cursor);
            userAgent.openLink(elt);
            evt.stopPropagation();
        }
    }

    /**
     * To handle a mouseover on an anchor and set the cursor.
     */
    public static class CursorMouseOverListener implements EventListener {

        protected UserAgent userAgent;

        public CursorMouseOverListener(UserAgent ua) {
            userAgent = ua;
        }

        public void handleEvent(Event evt) {
            if (AbstractEvent.getEventPreventDefault(evt)) return;
            Element target = (Element) evt.getTarget();
            if (CSSUtilities.isAutoCursor(target)) {
                userAgent.setSVGCursor(CursorManager.ANCHOR_CURSOR);
            }
            SVGAElement elt = (SVGAElement) evt.getCurrentTarget();
            if (elt != null) {
                String href = XLinkSupport.getXLinkHref(elt);
                userAgent.displayMessage(href);
            }
        }
    }

    /**
     * To handle a mouseout on an anchor and set the cursor.
     */
    public static class CursorMouseOutListener implements EventListener {

        protected UserAgent userAgent;

        public CursorMouseOutListener(UserAgent ua) {
            userAgent = ua;
        }

        public void handleEvent(Event evt) {
            if (AbstractEvent.getEventPreventDefault(evt)) return;
            SVGAElement elt = (SVGAElement) evt.getCurrentTarget();
            if (elt != null) {
                userAgent.displayMessage("");
            }
        }
    }
}
