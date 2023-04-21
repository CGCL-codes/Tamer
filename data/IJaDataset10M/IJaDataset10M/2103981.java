package org.apache.batik.gvt.flow;

import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import org.apache.batik.gvt.text.TextLayoutFactory;
import org.apache.batik.gvt.text.TextSpanLayout;

/**
 * Factory instance that returns TextSpanLayouts appropriate to
 * FlowRoot instances.
 *
 * @see org.apache.batik.gvt.text.TextSpanLayout
 * @author <a href="mailto:dewese@apache.org">Thomas DeWeese</a>
 * @version $Id: FlowTextLayoutFactory.java,v 1.1 2005/11/21 09:51:33 dev Exp $
 */
public class FlowTextLayoutFactory implements TextLayoutFactory {

    /**
     * Returns an instance of TextSpanLayout suitable for rendering the
     * AttributedCharacterIterator.
     *
     * @param aci The character iterator to be laid out
     * @param charMap Indicates how chars in aci map to original
     *                text char array.
     * @param offset The offset position for the text layout.
     * @param frc The font render context to use when creating the text layout.
     */
    public TextSpanLayout createTextLayout(AttributedCharacterIterator aci, int[] charMap, Point2D offset, FontRenderContext frc) {
        return new FlowGlyphLayout(aci, charMap, offset, frc);
    }
}
