package org.apache.batik.gvt.flow;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.batik.gvt.renderer.StrokingTextPainter;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @version $Id: FlowTextPainter.java,v 1.1 2005/11/21 09:51:33 dev Exp $
 */
public class FlowTextPainter extends StrokingTextPainter {

    /**
     * A unique instance of this class.
     */
    protected static TextPainter singleton = new FlowTextPainter();

    /**
     * Returns a unique instance of this class.
     */
    public static TextPainter getInstance() {
        return singleton;
    }

    public List getTextRuns(TextNode node, AttributedCharacterIterator aci) {
        List textRuns = node.getTextRuns();
        if (textRuns != null) {
            return textRuns;
        }
        AttributedCharacterIterator[] chunkACIs = getTextChunkACIs(aci);
        textRuns = computeTextRuns(node, aci, chunkACIs);
        aci.first();
        List rgns = (List) aci.getAttribute(FLOW_REGIONS);
        if (rgns != null) {
            Iterator i = textRuns.iterator();
            List chunkLayouts = new ArrayList();
            TextRun tr = (TextRun) i.next();
            List layouts = new ArrayList();
            chunkLayouts.add(layouts);
            layouts.add(tr.getLayout());
            while (i.hasNext()) {
                tr = (TextRun) i.next();
                if (tr.isFirstRunInChunk()) {
                    layouts = new ArrayList();
                    chunkLayouts.add(layouts);
                }
                layouts.add(tr.getLayout());
            }
            FlowGlyphLayout.textWrapTextChunk(chunkACIs, chunkLayouts, rgns, fontRenderContext);
        }
        node.setTextRuns(textRuns);
        return textRuns;
    }
}

;
