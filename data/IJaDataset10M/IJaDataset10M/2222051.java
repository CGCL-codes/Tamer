package com.sun.perseus.model;

import com.sun.perseus.platform.MathSupport;
import com.sun.perseus.j2d.Box;
import com.sun.perseus.j2d.PaintServer;
import com.sun.perseus.j2d.PaintTarget;
import com.sun.perseus.j2d.RenderContext;
import com.sun.perseus.j2d.RenderGraphics;
import com.sun.perseus.j2d.TextRenderingProperties;
import com.sun.perseus.j2d.TextProperties;
import com.sun.perseus.j2d.Transform;
import com.sun.perseus.util.SVGConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGRect;

/**
 * Models text content. In Perseus, text nodes are described by their content
 * (a character string), a number of font properties which guide which 
 * Font is selected to render the text and a text property (text-anchor) which 
 * defines how the text is positioned about its anchor point.
 * <br />
 * Text nodes lazily (i.e., on paint request) evaluate which font they use to
 * render their content. At that time, the characters in the Text content is 
 * matched with the glyphs that available Fonts can display. This generates a 
 * set of children <code>GlyphProxy</code> nodes which are then laid-out 
 * using the text property and the font metrics.
 *
 * @version $Id: Text.java,v 1.18 2006/06/29 10:47:35 ln156897 Exp $
 */
public class Text extends Group {

    /**
     * The text to be displayed
     */
    protected String content;

    /**
     * The origin of the text string on the x axis.
     */
    protected float[] x = { 0 };

    /**
     * The origin of the text string on the y axis.
     */
    protected float[] y = { 0 };

    /**
     * Per-glyph rotation
     */
    protected float[] rotate;

    /**
     * Points to the first text chunk.
     */
    protected GlyphLayout firstChunk;

    /**
     * Points to the last text chunk.
     */
    protected GlyphLayout lastChunk;

    /**
     * Used to scale dash array values.
     */
    protected float[] helperDashArray;

    /**
     * Constructor.
     *
     * @param ownerDocument this element's owner <code>DocumentNode</code>
     */
    public Text(final DocumentNode ownerDocument) {
        super(ownerDocument);
    }

    /**
     * @return the SVGConstants.SVG_TEXT_TAG value
     */
    public String getLocalName() {
        return SVGConstants.SVG_TEXT_TAG;
    }

    /**
     * Used by <code>DocumentNode</code> to create a new instance from
     * a prototype <code>Text</code>.
     *
     * @param doc the <code>DocumentNode</code> for which a new node is
     *        should be created.
     * @return a new <code>Text</code> for the requested document.
     */
    public ElementNode newInstance(final DocumentNode doc) {
        return new Text(doc);
    }

    /**
     * @return an adequate <code>ElementNodeProxy</code> for this node.
     */
    ElementNodeProxy buildProxy() {
        return new TextProxy(this);
    }

    /**
     * @param newX this text's new x-axis position
     */
    public void setX(final float[] newX) {
        if (equal(newX, x)) {
            return;
        }
        modifyingNode();
        if (newX == null || newX.length == 0) {
            this.x = new float[1];
            this.x[0] = 0;
        } else {
            this.x = newX;
        }
        clearLayoutsQuiet();
        modifiedNode();
    }

    /**
     * @param newY this text's new y-axis position
     */
    public void setY(final float[] newY) {
        if (equal(newY, y)) {
            return;
        }
        modifyingNode();
        if (newY == null || newY.length == 0) {
            this.y = new float[1];
            this.y[0] = 0;
        } else {
            this.y = newY;
        }
        clearLayoutsQuiet();
        modifiedNode();
    }

    /**
     * @param newRotate this text's new per-glyph rotation
     */
    public void setRotate(final float[] newRotate) {
        if (equal(newRotate, rotate)) {
            return;
        }
        modifyingNode();
        this.rotate = newRotate;
        clearLayoutsQuiet();
        modifiedNode();
    }

    /**
     * @return this text's x-axis author position
     */
    public float[] getX() {
        return x;
    }

    /**
     * @return this text's y-axis anchor position
     */
    public float[] getY() {
        return y;
    }

    /**
     * @return this text's set of rotation values for 
     *         its glyphs.
     */
    public float[] getRotate() {
        return rotate;
    }

    /**
     * @return true always, as a text node needs its textual content which
     * is not fully loaded until the element has been loaded.
     */
    public boolean getPaintNeedsLoad() {
        return true;
    }

    /**
     * To be overriddent by derived classes, such as TimedElementNode,
     * if they need to perform special operations when hooked into the 
     * document tree.
     */
    void nodeHookedInDocumentTree() {
        super.nodeHookedInDocumentTree();
    }

    /**
     * To be overriddent by derived classes, such as TimedElementNode,
     * if they need to perform special operations when unhooked from the 
     * document tree.
     */
    void nodeUnhookedFromDocumentTree() {
        super.nodeUnhookedFromDocumentTree();
    }

    /**
     * @return the tight bounding box in current user coordinate
     * space. 
     */
    public SVGRect getBBox() {
        return addNodeBBox(null, null);
    }

    /**
     * This method is called by nodes that can be a parent of a Text node,
     * such as Group, which is why we need this method even though the main
     * work of bbox computation is done in addNodeBBox, and the API only 
     * requires getBBox. However other element's dependency on the addBBox() 
     * method requires that we provide a funtional implementation for it here.
     *
     * @return the tight bounding box in current user coordinate
     * space. 
     */
    Box addBBox(Box bbox, final Transform t) {
        return addNodeBBox(bbox, t);
    }

    /**
     * @return the tight bounding box in screen coordinate space.
     */
    public SVGRect getScreenBBox() {
        if (!inDocumentTree()) {
            return null;
        }
        return addNodeBBox(null, txf);
    }

    /**
     * @param bbox the bounding box to which this node's bounding box should be
     *        appended. That bounding box is in the target coordinate space. It 
     *        may be null, in which case this node should create a new one.
     * @param t the transform from the node coordinate system to the coordinate
     *        system into which the bounds should be computed.
     * @return the bounding box of this node, in the target coordinate space, 
     */
    Box addNodeBBox(final Box bbox, final Transform t) {
        checkLayout();
        return addNodeBBox(bbox, t, firstChunk);
    }

    /**
     * @param bbox the bounding box to which this node's bounding box should be
     *        appended. That bounding box is in the target coordinate space. It 
     *        may be null, in which case this node should create a new one.
     * @param t the transform from the node coordinate system to the coordinate
     *        system into which the bounds should be computed.
     * @param fc the <code>GlyphLayout</code> first chunk, so that the method
     *        can be used by <code>TextProxy</code>
     * @return the bounding box of this node, in the target coordinate space, 
     */
    Box addNodeBBox(Box bbox, final Transform t, final GlyphLayout fc) {
        GlyphLayout c = fc;
        while (c != null) {
            ownerDocument.bboxChunkTxf.setTransform(t);
            c.applyTransform(this, ownerDocument.bboxChunkTxf);
            bbox = c.addBBox(bbox, ownerDocument.bboxChunkTxf);
            c = c.nextSibling;
        }
        return bbox;
    }

    /**
     * An <code>Text</code> has something to render 
     *
     * @return true
     */
    public boolean hasNodeRendering() {
        return true;
    }

    /**
     * @param text the text to append to this node's content.
     *        If text is null or empty, this does nothing.
     */
    public void appendTextChild(final String text) {
        if (text == null || text.length() == 0) {
            return;
        }
        if (content == null) {
            setContent(text);
        } else {
            setContent(content + text);
        }
    }

    /**
     * @param newContent this node's new content string
     */
    public void setContent(final String newContent) {
        if (equal(newContent, content)) {
            return;
        }
        modifyingNode();
        this.content = newContent;
        clearLayouts(false);
        modifiedNode();
    }

    /**
     * @return this node's text content as a string
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the <code>ModelNode</code>, if any, hit by the
     * point at coordinate x/y. 
     * 
     * @param pt the x/y coordinate. Should never be null and be
     *        of size two. If not, the behavior is unspecified.
     *        The coordinates are in viewport space.
     * @return the <tt>ModelNode</tt> hit at the given point or null
     *         if none was hit.
     */
    public ModelNode nodeHitAt(final float[] pt) {
        if (canRenderState != 0) {
            return null;
        }
        checkLayout();
        if (isHitVP(pt, this, getInverseTransformState(), lastChunk)) {
            return this;
        }
        return null;
    }

    /**
     * Returns the <code>ModelNode</code>, if any, hit by the
     * point at coordinate x/y in the proxy tree starting at 
     * proxy.
     * 
     * @param pt the x/y coordinate. Should never be null and be
     *        of size two. If not, the behavior is unspecified.
     *        The coordinates are in viewport space.
     * @param proxy the root of the proxy tree to test.
     * @return the <tt>ModelNode</tt> hit at the given point or null
     *         if none was hit.
     */
    ModelNode proxyNodeHitAt(final float[] pt, final ElementNodeProxy proxy) {
        if (canRenderState != 0) {
            return null;
        }
        TextProxy tp = (TextProxy) proxy;
        tp.checkLayout();
        if (isHitVP(pt, tp, tp.inverseTxf, tp.lastChunk)) {
            return proxy;
        }
        return null;
    }

    /**
     * Returns true if this node is hit by the input point. The input point
     * is in viewport space. By default, a node is not hit, not
     * matter what the input coordinate is.
     *  
     * @param pt the x/y coordinate. Should never be null and be
     *        of size two. If not, the behavior is unspecified.
     *        The x/y coordinate is in the node's user space.
     * @param trp the text rendering properties that apply to the layout 
     *        rendering.
     * @param itx the transform from viewport space to the text's user space.
     * @param lc the <tt>GlyphLayout</tt> 'lastChunk' so that the method
     *        can be used by <tt>Text</tt> and <tt>TextProxy</tt>.
     * @return true if the node is hit by the input point. 
     * @see #nodeHitAt
     */
    protected boolean isHitVP(final float[] pt, final TextRenderingProperties trp, final Transform itx, final GlyphLayout lc) {
        if (!trp.getVisibility()) {
            return false;
        }
        GlyphLayout c = lc;
        while (c != null) {
            ownerDocument.hitChunkTxf.setTransform(1, 0, 0, 1, 0, 0);
            c.applyInverseTransform(trp, ownerDocument.hitChunkTxf);
            ownerDocument.hitChunkTxf.mMultiply(itx);
            if (c.isHitVP(pt, this, ownerDocument.hitChunkTxf)) {
                return true;
            }
            c = c.prevSibling;
        }
        return false;
    }

    /**
     * Paints this node into the input <code>RenderGraphics</code>. A
     * <code>Text</code> node renders its associated <code>GlyphLayout</code>
     * children.
     *
     * @param rg the <tt>RenderGraphics</tt> where the node should paint itself
     */
    public void paint(final RenderGraphics rg) {
        if (canRenderState != 0) {
            return;
        }
        checkLayout();
        paintRendered(rg, this, txf, firstChunk);
    }

    /**
     * Paints this node into the input RenderGraphics, assuming the node
     * is rendered.
     *
     * @param rg the <code>RenderGraphics</code> where the node should paint
     * itself.
     * @param tc the <code>TextRenderingProperties</code> holding the properties
     * tha should be used for rendering into the input
     * <code>RenderGraphics</code>
     * @param tx the transform to use when rendering this node
     * @param fc the first chunk to render.
     * @see ElementNode#paint
     * @see ElementNode#paintRendered
     * @see ModelNode#canRender
     */
    public void paintRendered(final RenderGraphics rg, final TextRenderingProperties trc, final Transform tx, final GlyphLayout fc) {
        if (!trc.getVisibility()) {
            return;
        }
        rg.setPaintTarget(this);
        rg.setPaintTransform(tx);
        rg.setFontSize(trc.getFontSize());
        rg.setTextAnchor(trc.getTextAnchor());
        if (trc.getFill() != null) {
            rg.setFillRule(trc.getFillRule());
            rg.setFill(trc.getFill());
            rg.setFillOpacity(trc.getFillOpacity());
            fillText(rg, tx, fc);
        }
        if (trc.getStroke() != null) {
            rg.setStrokeWidth(trc.getStrokeWidth() / trc.getFontSize());
            float[] dashArray = trc.getStrokeDashArray();
            float[] trDashArray = null;
            float trDashOffset = 0;
            if (dashArray != null) {
                float fontSize = trc.getFontSize();
                if ((helperDashArray == null) || (helperDashArray.length != dashArray.length)) {
                    helperDashArray = new float[dashArray.length];
                }
                trDashArray = helperDashArray;
                for (int i = 0; i < dashArray.length; ++i) {
                    trDashArray[i] = dashArray[i] / fontSize;
                }
                trDashOffset = trc.getStrokeDashOffset() / fontSize;
            }
            rg.setStrokeDashArray(trDashArray);
            rg.setStrokeDashOffset(trDashOffset);
            rg.setStroke(trc.getStroke());
            rg.setStrokeOpacity(trc.getStrokeOpacity());
            rg.setStrokeLineCap(trc.getStrokeLineCap());
            rg.setStrokeLineJoin(trc.getStrokeLineJoin());
            rg.setStrokeMiterLimit(trc.getStrokeMiterLimit());
            drawText(rg, tx, fc);
        }
    }

    /**
     * Fills text.
     *
     * @param rg the <code>RenderGraphics</code> where the node should paint
     *        itself.
     * @param tx the rendering transform.
     * @param fc the first chunk to render.
     */
    void fillText(final RenderGraphics rg, final Transform tx, final GlyphLayout fc) {
        GlyphLayout c = fc;
        while (c != null) {
            ownerDocument.paintChunkTxf.setTransform(tx);
            c.applyTransform(rg, ownerDocument.paintChunkTxf);
            c.fillText(rg, ownerDocument.paintChunkTxf);
            c = c.nextSibling;
        }
    }

    /**
     * Draws text.
     *
     * @param rg the <code>RenderGraphics</code> where the node should paint
     *        itself.
     * @param tx the rendering transform.
     * @param fc the first chunk to render.
     */
    void drawText(final RenderGraphics rg, final Transform tx, final GlyphLayout fc) {
        GlyphLayout c = fc;
        while (c != null) {
            ownerDocument.paintChunkTxf.setTransform(tx);
            c.applyTransform(rg, ownerDocument.paintChunkTxf);
            c.drawText(rg, ownerDocument.paintChunkTxf);
            c = c.nextSibling;
        }
    }

    protected void nodeRendered() {
    }

    /**
     * Clears all cached layout information. Because the computed transform
     * property on text depends on the layout, clearLayouts on <code>Text</code>
     * also clears the transform and property cache.
     */
    public void clearLayouts() {
        clearLayouts(true);
    }

    /**
     * Implementations. Clears layouts but does not generate a 
     * modification notification.
     *
     * @param notif if true, then notifications are issued
     *        (i.e., calls to <code>modifyingNode</code> and
     *        <code>modifiedNode</code>).
     */
    protected void clearLayouts(final boolean notif) {
        if (notif) {
            modifyingNode();
        }
        clearLayoutsQuiet();
        if (notif) {
            modifiedNode();
        }
    }

    /**
     * Clears all cached layout information
     * but does not generate modification
     * events for this node.
     */
    protected void clearLayoutsQuiet() {
        firstChunk = null;
        lastChunk = null;
        ElementNodeProxy p = firstProxy;
        while (p != null) {
            ((TextProxy) p).clearLayoutsQuiet();
            p = p.nextProxy;
        }
    }

    /**
     * Checks that the text's layout has been computed and computes it in 
     * case it was not.    
     */
    void checkLayout() {
        if (firstChunk == null) {
            firstChunk = layoutText(this);
            GlyphLayout cur = firstChunk;
            while (cur.nextSibling != null) {
                cur = cur.nextSibling;
            }
            lastChunk = (GlyphLayout) cur;
        }
    }

    /**
     * @return true if the content string is not null and not empty or
     *         if this node has children. false otherwise.
     */
    public boolean hasDescendants() {
        return super.hasDescendants() || content != null && !("".equals(content));
    }

    /**
     * Applies the xml:space policy
     *
     * @param s the array of characters which should be processed
     * @return the index of the last (inclusive) relevant character
     *         after processing.
     */
    protected int applyXMLSpace(final char[] s) {
        switch(getXMLSpace()) {
            case XML_SPACE_DEFAULT:
            case XML_SPACE_INHERIT:
                return applyXMLSpaceDefault(s);
            default:
                return applyXMLSpacePreserve(s);
        }
    }

    /**
     * Applies the xml:space="default" policy
     *
     * ** SPECIFICATION TEXT, SECTION 10.15 **
     *
     * When xml:space="default", the SVG user agent will do
     * the following using a copy of the original character 
     * data content. First, it will remove all newline characters.
     * Then it will convert all tab characters into space characters. 
     * Then, it will strip off all leading and trailing space
     * characters. Then, all contiguous space characters will be 
     * consolidated
     *
     * @param s the array of characters which should be processed
     * @return the index of the last (exclusive) relevant character
     *         after processing.
     */
    protected int applyXMLSpaceDefault(final char[] s) {
        int j = 0;
        int i = 0;
        for (; i < s.length; i++) {
            if (s[i] != '\n') {
                if (s[i] == '\t') {
                    s[i] = ' ';
                }
                s[j++] = s[i];
            }
        }
        int length = j;
        for (i = 0; i < length; i++) {
            if (s[i] != ' ') {
                break;
            }
        }
        j = 0;
        s[j++] = s[i++];
        for (; i < length; i++) {
            if (s[i] != ' ') {
                s[j++] = s[i];
            } else {
                if (s[j - 1] != ' ') {
                    s[j++] = ' ';
                }
            }
        }
        length = j;
        if (s[j - 1] == ' ') {
            length = j - 1;
        }
        return length;
    }

    /**
     * Applies the xml:space="preserve" policy
     *
     * ** SPECIFICATION TEXT, SECTION 10.15 **
     *
     * When xml:space="preserve", the SVG user agent will do the 
     * following using a copy of the original character data content. 
     * It will convert all newline and tab characters into space 
     * characters. Then, it will draw all space characters, including 
     * leading, trailing and multiple contiguous space characters. 
     * Thus, when drawn with xml:space="preserve", the string "a b" 
     * (three spaces between "a" and "b") will produce a
     * larger separation between "a" and "b" than "a b" (one space 
     * between "a" and "b").
     *
     * @param s the array of characters which should be processed
     * @return the index of the last (exclusive) relevant character
     *         after processing.
     */
    protected int applyXMLSpacePreserve(final char[] s) {
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '\n' || s[i] == '\t') {
                s[i] = ' ';
            }
        }
        return s.length;
    }

    /**
     * Invoked when text layout should be performed
     * or checked.
     *
     * @param tp text is laid out for the input <tt>TextProperties</tt>
     * @return an <code>GlyphLayout</code> instance containing the 
     *         laid out text.
     */
    public GlyphLayout layoutText(final TextProperties tp) {
        GlyphLayout startChunk = new GlyphLayout(ownerDocument);
        GlyphLayout chunk = startChunk;
        chunk.x = x[0];
        chunk.y = y[0];
        if (content == null || "".equals(content)) {
            return startChunk;
        }
        char[] s = content.toCharArray();
        int length = applyXMLSpace(s);
        FontFace.Match defaultMatch = ownerDocument.resolveFontFaces(tp);
        FontFace.Match firstMatch = defaultMatch.next;
        FontFace.Match curMatch = null;
        Glyph missingGlyph = defaultMatch.fontFace.getMissingGlyph();
        int cur = 0;
        Glyph glyph = null;
        GlyphProxy proxy = null, prevProxy = null;
        float curAdv = 0;
        float fontSize = tp.getFontSize();
        while (cur < length) {
            if (cur > 0 && (cur < x.length || cur < y.length)) {
                GlyphLayout prevChunk = chunk;
                chunk = new GlyphLayout(ownerDocument);
                prevProxy = null;
                prevChunk.advance = curAdv;
                curAdv = 0;
                if (cur < x.length) {
                    chunk.x = x[cur];
                } else {
                    chunk.x = prevChunk.x + fontSize * prevChunk.advance;
                }
                if (cur < y.length) {
                    chunk.y = y[cur];
                } else {
                    chunk.y = prevChunk.y;
                }
                prevChunk.nextSibling = chunk;
                chunk.prevSibling = prevChunk;
            }
            glyph = null;
            curMatch = firstMatch;
            while (curMatch != null) {
                if ((glyph = curMatch.fontFace.canDisplay(s, cur)) != null) {
                    break;
                }
                curMatch = curMatch.next;
            }
            if (glyph == null) {
                if ((glyph = defaultMatch.fontFace.canDisplay(s, cur)) == null) {
                    glyph = missingGlyph;
                }
            }
            proxy = new GlyphProxy(glyph);
            chunk.add(proxy);
            if (prevProxy != null) {
                float adjust = ((Font) proxy.proxied.parent).getHKern(prevProxy.proxied, proxy.proxied);
                curAdv -= adjust;
            }
            proxy.setX(curAdv);
            if (rotate != null && cur < rotate.length) {
                proxy.setRotate(rotate[cur]);
            }
            cur += glyph.getLength();
            curAdv += glyph.getTextHorizontalAdvanceX();
            prevProxy = proxy;
        }
        chunk.advance = curAdv;
        return startChunk;
    }

    /**
     * Recomputes the transform cache, if one exists. 
     * @param parentTransform the Transform applied to this node's parent.
     */
    protected void recomputeTransformState(final Transform parentTransform) {
        txf = appendTransform(parentTransform, txf);
        computeCanRenderTransformBit(txf);
        inverseTxf = null;
    }

    /**
     * Called when the computed value of the given property has changed.
     * As we do not render any text children, this does not propage changes any
     * further.
     *
     * @param propertyIndex index for the property whose value has changed.
     * @param parentPropertyValue the value that children of this node should
     *        now inherit.
     */
    protected void propagatePropertyState(final int propertyIndex, final Object parentPropertyValue) {
        if (firstProxy != null) {
            ElementNodeProxy proxy = firstProxy;
            while (proxy != null) {
                ((CompositeGraphicsNodeProxy) proxy).proxiedPropertyStateChange(propertyIndex, parentPropertyValue);
                proxy = proxy.nextProxy;
            }
        }
    }

    /**
     * Called when the computed value of the given float property has changed.
     * As we do not render any text children, this does not propage changes any
     * further.
     *
     * @param propertyIndex index for the property whose value has changed.
     * @param parentPropertyValue the value that children of this node should 
     *        now inherit.
     */
    protected void propagateFloatPropertyState(final int propertyIndex, final float parentPropertyValue) {
        if (firstProxy != null) {
            ElementNodeProxy proxy = firstProxy;
            while (proxy != null) {
                ((CompositeGraphicsNodeProxy) proxy).proxiedFloatPropertyStateChange(propertyIndex, parentPropertyValue);
                proxy = proxy.nextProxy;
            }
        }
    }

    /**
     * Called when the computed value of the given packed property has changed.
     * As we do not render any text children, this does not propage changes any
     * further.
     *
     * @param propertyIndex index for the property whose value has changed.
     * @param parentPropertyValue the value that children of this node should 
     *        now inherit.
     */
    protected void propagatePackedPropertyState(final int propertyIndex, final int parentPropertyValue) {
        if (firstProxy != null) {
            ElementNodeProxy proxy = firstProxy;
            while (proxy != null) {
                ((CompositeGraphicsNodeProxy) proxy).proxiedPackedPropertyStateChange(propertyIndex, parentPropertyValue);
                proxy = proxy.nextProxy;
            }
        }
    }

    /**
     * Text handles x, y, and #text traits.
     *
     * @param traitName the name of the trait which the element may support.
     * @return true if this element supports the given trait in one of the
     *         trait accessor methods.
     */
    boolean supportsTrait(final String traitName) {
        if (SVGConstants.SVG_X_ATTRIBUTE == traitName || SVGConstants.SVG_Y_ATTRIBUTE == traitName || SVGConstants.SVG_ROTATE_ATTRIBUTE == traitName || SVGConstants.SVG_TEXT_PSEUDO_ATTRIBUTE == traitName) {
            return true;
        } else {
            return super.supportsTrait(traitName);
        }
    }

    /**
     * Text handles x, y, and #text traits.
     *
     * @param name the requested trait's name (e.g., "#text")
     * @return the requested trait's string value (e.g., "Hello SVG Text")
     *
     * @throws DOMException with error code NOT_SUPPORTED_ERROR if the requested
     * trait is not supported on this element or null.
     * @throws DOMException with error code TYPE_MISMATCH_ERR if requested
     * trait's computed value cannot be converted to a String (SVG Tiny only).
     */
    public String getTraitImpl(final String name) throws DOMException {
        if (SVGConstants.SVG_X_ATTRIBUTE == name) {
            return toStringTrait(getX());
        } else if (SVGConstants.SVG_Y_ATTRIBUTE == name) {
            return toStringTrait(getY());
        } else if (SVGConstants.SVG_ROTATE_ATTRIBUTE == name) {
            if (rotate == null) {
                return "";
            }
            System.err.println(">>>>>>>>>>>>>>>>>> getTraitImpl(" + name + ") : '" + toStringTrait(rotate) + "'");
            return toStringTrait(rotate);
        } else if (SVGConstants.SVG_TEXT_PSEUDO_ATTRIBUTE == name) {
            if (content == null) {
                return "";
            }
            return getContent();
        } else {
            return super.getTraitImpl(name);
        }
    }

    /**
     * Text handles x, y float traits.
     *
     * @param name the trait name (e.g., "y")
     * @return the trait's float value (e.g., 10f)
     *
     * @throws DOMException with error code NOT_SUPPORTED_ERROR if the requested
     * trait is not supported on this element or null.
     * @throws DOMException with error code TYPE_MISMATCH_ERR if requested
     * trait's computed value cannot be converted to a float
     * @throws SecurityException if the application does not have the necessary
     * privilege rights to access this (SVG) content.
     */
    float getFloatTraitImpl(final String name) throws DOMException {
        if (SVGConstants.SVG_X_ATTRIBUTE == name) {
            return getX()[0];
        } else if (SVGConstants.SVG_Y_ATTRIBUTE == name) {
            return getY()[0];
        } else {
            return super.getFloatTraitImpl(name);
        }
    }

    /**
     * @param traitName the trait name.
     */
    TraitAnim createTraitAnimImpl(final String traitName) {
        if (SVGConstants.SVG_X_ATTRIBUTE == traitName || SVGConstants.SVG_Y_ATTRIBUTE == traitName || SVGConstants.SVG_ROTATE_ATTRIBUTE == traitName) {
            return new FloatTraitAnim(this, traitName, TRAIT_TYPE_FLOAT);
        } else if (SVGConstants.SVG_TEXT_PSEUDO_ATTRIBUTE == traitName) {
            return new StringTraitAnim(this, NULL_NS, traitName);
        } else {
            return super.createTraitAnimImpl(traitName);
        }
    }

    /**
     * Set the trait value as float.
     *
     * @param name the trait's name.
     * @param value the trait's value.
     *
     * @throws DOMException with error code NOT_SUPPORTED_ERROR if the requested
     * trait is not supported on this element.
     * @throws DOMException with error code TYPE_MISMATCH_ERR if the requested
     * trait's value cannot be specified as a float
     * @throws DOMException with error code INVALID_ACCESS_ERR if the input
     * value is an invalid value for the given trait.
     */
    void setFloatArrayTrait(final String name, final float[][] value) throws DOMException {
        if (SVGConstants.SVG_X_ATTRIBUTE == name) {
            setX(toTraitFloatArray(value));
        } else if (SVGConstants.SVG_Y_ATTRIBUTE == name) {
            setY(toTraitFloatArray(value));
        } else if (SVGConstants.SVG_ROTATE_ATTRIBUTE == name) {
            setRotate(toTraitFloatArray(value));
        } else {
            super.setFloatArrayTrait(name, value);
        }
    }

    /**
     * Validates the input trait value.
     *
     * @param traitName the name of the trait to be validated.
     * @param value the value to be validated
     * @param reqNamespaceURI the namespace of the element requesting 
     *        validation.
     * @param reqLocalName the local name of the element requesting validation.
     * @param reqTraitNamespace the namespace of the trait which has the values
     *        value on the requesting element.
     * @param reqTraitName the name of the trait which has the values value on 
     *        the requesting element.
     * @throws DOMException with error code INVALID_ACCESS_ERR if the input
     *         value is incompatible with the given trait.
     */
    public float[][] validateFloatArrayTrait(final String traitName, final String value, final String reqNamespaceURI, final String reqLocalName, final String reqTraitNamespace, final String reqTraitName) throws DOMException {
        if (SVGConstants.SVG_X_ATTRIBUTE == traitName || SVGConstants.SVG_Y_ATTRIBUTE == traitName) {
            return toAnimatedFloatArray(parseFloatArrayTrait(traitName, value));
        } else if (SVGConstants.SVG_ROTATE_ATTRIBUTE == traitName) {
            float[][] v = toAnimatedFloatArray(parseFloatArrayTrait(traitName, value));
            return v;
        } else {
            return super.validateFloatArrayTrait(traitName, value, reqNamespaceURI, reqLocalName, reqTraitNamespace, reqTraitName);
        }
    }

    /**
     * Text handles x, y and #text traits.
     *
     * @param name the trait's name (e.g., "#text")
     * @param value the trait's value (e.g, "Hello SVG Text")
     *
     * @throws DOMException with error code NOT_SUPPORTED_ERROR if the requested
     * trait is not supported on this element or null.
     * @throws DOMException with error code TYPE_MISMATCH_ERR if the requested
     * trait's value cannot be specified as a String
     * @throws DOMException with error code INVALID_ACCESS_ERR if the input
     * value is an invalid value for the given trait or null.
     * @throws DOMException with error code NO_MODIFICATION_ALLOWED_ERR: if
     * attempt is made to change readonly trait.
     */
    public void setTraitImpl(final String name, final String value) throws DOMException {
        if (SVGConstants.SVG_X_ATTRIBUTE == name) {
            setX(parseFloatArrayTrait(name, value));
        } else if (SVGConstants.SVG_Y_ATTRIBUTE == name) {
            setY(parseFloatArrayTrait(name, value));
        } else if (SVGConstants.SVG_ROTATE_ATTRIBUTE == name) {
            float[] rt = parseFloatArrayTrait(name, value);
            setRotate(rt);
        } else if (SVGConstants.SVG_TEXT_PSEUDO_ATTRIBUTE == name) {
            if (value == null) {
                throw illegalTraitValue(name, value);
            }
            setContent(value);
        } else {
            super.setTraitImpl(name, value);
        }
    }

    /**
     * Text handles x, y float traits.
     *
     * @param name the trait's name (e.g., "x")
     * @param value the trait's value (e.g, 10f)
     *
     * @throws DOMException with error code NOT_SUPPORTED_ERROR if the requested
     * trait is not supported on this element.
     * @throws DOMException with error code TYPE_MISMATCH_ERR if the requested
     * trait's value cannot be specified as a float
     * @throws DOMException with error code INVALID_ACCESS_ERR if the input
     * value is an invalid value for the given trait.
     * @throws SecurityException if the application does not have the necessary
     * privilege rights to access this (SVG) content.
     */
    public void setFloatTraitImpl(final String name, final float value) throws DOMException {
        if (SVGConstants.SVG_X_ATTRIBUTE == name) {
            setX(new float[] { value });
        } else if (SVGConstants.SVG_Y_ATTRIBUTE == name) {
            setY(new float[] { value });
        } else {
            super.setFloatTraitImpl(name, value);
        }
    }

    /**
     * @param name the name of the trait to convert.
     * @param value the float trait value to convert.
     */
    String toStringTrait(final String name, final float[][] value) {
        if (SVGConstants.SVG_X_ATTRIBUTE == name || SVGConstants.SVG_Y_ATTRIBUTE == name) {
            float[] v = new float[value.length];
            for (int i = 0; i < value.length; i++) {
                v[i] = value[i][0];
            }
            return toStringTrait(v);
        } else if (SVGConstants.SVG_ROTATE_ATTRIBUTE == name) {
            float[] v = new float[value.length];
            for (int i = 0; i < value.length; i++) {
                v[i] = value[i][0];
            }
            return toStringTrait(v);
        } else {
            return super.toStringTrait(name, value);
        }
    }

    /**
     * Debug helper.
     * @return a <code>String</code> containing the text content
     */
    public String toString() {
        String xStr = "x[";
        for (int i = 0; i < x.length; i++) {
            xStr += x[i] + ", ";
        }
        xStr = xStr.substring(0, xStr.length() - 2);
        xStr += "]";
        String yStr = "y[";
        for (int i = 0; i < y.length; i++) {
            yStr += y[i] + ", ";
        }
        yStr = yStr.substring(0, yStr.length() - 2);
        yStr += "]";
        return super.toString() + "[\"" + content + "\"] " + xStr + " " + yStr;
    }

    /**
     * @param newDisplay the new computed display value
     */
    void setComputedDisplay(final boolean newDisplay) {
        super.setComputedDisplay(newDisplay);
    }

    /**
     * @param newVisibility the new computed visibility property.
     */
    void setComputedVisibility(final boolean newVisibility) {
        super.setComputedVisibility(newVisibility);
    }

    /**
     * @param newFill the new computed fill property.
     */
    void setComputedFill(final PaintServer newFill) {
        this.fill = newFill;
    }

    /**
     * @param newStroke the new computed stroke property.
     */
    void setComputedStroke(final PaintServer newStroke) {
        this.stroke = newStroke;
    }

    /**
     * @param newStrokeWidth the new computed stroke-width property value.
     */
    void setComputedStrokeWidth(final float newStrokeWidth) {
        strokeWidth = newStrokeWidth;
    }

    /**
     * @param newStrokeLineJoin the new computed value for stroke-line-join
     */
    void setComputedStrokeLineJoin(final int newStrokeLineJoin) {
        super.setComputedStrokeLineJoin(newStrokeLineJoin);
    }

    /**
     * @param newStrokeLineCap the new value for the stroke-linecap property.
     */
    void setComputedStrokeLineCap(final int newStrokeLineCap) {
        super.setComputedStrokeLineCap(newStrokeLineCap);
    }

    /**
     * @param newStrokeMiterLimit the new computed stroke-miterlimit property.
     */
    void setComputedStrokeMiterLimit(final float newStrokeMiterLimit) {
        strokeMiterLimit = newStrokeMiterLimit;
    }

    /**
     * @param newStrokeDashArray the new computed stroke-dasharray property 
     *        value.
     */
    void setComputedStrokeDashArray(final float[] newStrokeDashArray) {
        strokeDashArray = newStrokeDashArray;
    }

    /**
     * @param newStrokeDashOffset the new stroke-dashoffset computed property 
     *        value.
     */
    void setComputedStrokeDashOffset(final float newStrokeDashOffset) {
        strokeDashOffset = newStrokeDashOffset;
    }

    /**
     * @param newFillOpacity the new computed value for the fill opacity 
     *        property.
     */
    void setComputedFillOpacity(final float newFillOpacity) {
        super.setComputedFillOpacity(newFillOpacity);
    }

    /**
     * @param newStrokeOpacity the new computed stroke-opacity property.
     */
    void setComputedStrokeOpacity(final float newStrokeOpacity) {
        super.setComputedStrokeOpacity(newStrokeOpacity);
    }

    /**
     * @param newFontSize the new computed font-size property value.
     */
    void setComputedFontSize(final float newFontSize) {
        this.fontSize = newFontSize;
        computeCanRenderFontSizeBit(newFontSize);
    }

    /**
     * @param newFontFamily the new computed font-family property value.
     */
    void setComputedFontFamily(final String[] newFontFamily) {
        this.fontFamily = newFontFamily;
        clearLayoutsQuiet();
    }

    /**
     * Sets the value of the computed text anchor property.
     *
     * @param newTextAnchor the new value for the computed text anchor property.
     */
    void setComputedTextAnchor(final int newTextAnchor) {
        super.setComputedTextAnchor(newTextAnchor);
    }

    /**
     * @param newFontWeight new computed value for the font-weight property.
     */
    void setComputedFontWeight(final int newFontWeight) {
        super.setComputedFontWeight(newFontWeight);
        clearLayoutsQuiet();
    }

    /**
     * @param newFontStyle the new computed font-style property.
     */
    void setComputedFontStyle(final int newFontStyle) {
        super.setComputedFontStyle(newFontStyle);
        clearLayoutsQuiet();
    }
}
