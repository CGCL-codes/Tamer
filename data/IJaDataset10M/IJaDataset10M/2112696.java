package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.BNFProductionPayload;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class BNFProductionTag extends GrammarTag {

    protected BNFProductionTag() {
        super(TagCategory.SINGLE_SEQ_CHILD);
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new BNFProductionPayload();
    }

    @Override
    public String getDescription() {
        return "BNFP";
    }

    private static final VisreedTag instance = new BNFProductionTag();

    public static VisreedTag getInstance() {
        return instance;
    }
}
