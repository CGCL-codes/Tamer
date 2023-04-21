package visreed.extension.javaCC.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.KleeneStarPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarKleeneStarTag extends GrammarTag {

    protected GrammarKleeneStarTag() {
        super(TagCategory.SINGLE_SEQ_CHILD);
    }

    private static final GrammarKleeneStarTag instance = new GrammarKleeneStarTag();

    protected static GrammarKleeneStarTag getInstance() {
        return instance;
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new KleeneStarPayload(this);
    }

    @Override
    public String getDescription() {
        return "GKLN*";
    }
}
