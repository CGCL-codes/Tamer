package org.xhtmlrenderer.render;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import org.xhtmlrenderer.css.style.CalculatedStyle;
import org.xhtmlrenderer.css.style.CssContext;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.layout.Styleable;

/**
 * An anonymous block box as defined in the CSS spec.  This class is only used
 * when wrapping inline content in a block box in order to ensure that a block
 * box only ever contains either block or inline content.  Other anonymous block
 * boxes create a <code>BlockBox</code> directly with the anonymous property is
 * true.
 */
public class AnonymousBlockBox extends BlockBox {

    private List _openInlineBoxes;

    public AnonymousBlockBox(Element element) {
        setElement(element);
    }

    public void layout(LayoutContext c) {
        layoutInlineChildren(c, 0, calcInitialBreakAtLine(c), true);
    }

    public int getContentWidth() {
        return getContainingBlock().getContentWidth();
    }

    public Box find(CssContext cssCtx, int absX, int absY, boolean findAnonymous) {
        Box result = super.find(cssCtx, absX, absY, findAnonymous);
        if (!findAnonymous && result == this) {
            return getParent();
        } else {
            return result;
        }
    }

    public List getOpenInlineBoxes() {
        return _openInlineBoxes;
    }

    public void setOpenInlineBoxes(List openInlineBoxes) {
        _openInlineBoxes = openInlineBoxes;
    }

    public boolean isSkipWhenCollapsingMargins() {
        for (Iterator i = getInlineContent().iterator(); i.hasNext(); ) {
            Styleable styleable = (Styleable) i.next();
            CalculatedStyle style = styleable.getStyle();
            if (!(style.isFloated() || style.isAbsolute() || style.isFixed() || style.isRunning())) {
                return false;
            }
        }
        return true;
    }

    public void provideSiblingMarginToFloats(int margin) {
        for (Iterator i = getInlineContent().iterator(); i.hasNext(); ) {
            Styleable styleable = (Styleable) i.next();
            if (styleable instanceof BlockBox) {
                BlockBox b = (BlockBox) styleable;
                if (b.isFloated()) {
                    b.getFloatedBoxData().setMarginFromSibling(margin);
                }
            }
        }
    }

    public boolean isMayCollapseMarginsWithChildren() {
        return false;
    }

    public void styleText(LayoutContext c) {
        styleText(c, getParent().getStyle());
    }

    public BlockBox copyOf() {
        throw new IllegalArgumentException("cannot be copied");
    }
}
