package visreed.extension.javaCC.model.tag;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import visreed.model.VisreedNode;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;
import visreed.view.PaintParameter;

/**
 * @author Xiaoyu Guo
 */
public abstract class LexicalTag extends JavaCCTag {

    protected static final Color BACK_COLOR = new Color(42, 221, 248);

    protected LexicalTag(TagCategory category) {
        super(category, Field.LEXICAL);
        this.paintParameter = new PaintParameter();
        this.paintParameter.setBackColor(BACK_COLOR);
    }

    @Override
    protected VisreedNode createSEQ(VisreedWholeGraph wholeGraph) {
        return wholeGraph.makeRootNode(LEXICAL_SEQUENCE);
    }

    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(LEXICAL_SEQUENCE);
        return seq;
    }

    @Override
    protected boolean contentModelHook(final List<VisreedTag> childTags) {
        boolean result = childTags.get(0).equals(LEXICAL_SEQUENCE);
        return result;
    }

    /** Common child tags for Lexical non-SEQs */
    protected static final VisreedTag[] CHILD_TAG_NON_SEQ_LEX = { LEXICAL_SEQUENCE };
}
