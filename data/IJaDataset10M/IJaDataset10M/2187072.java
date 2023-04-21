package visreed.extension.example.tag;

import java.util.ArrayList;
import java.util.List;
import visreed.extension.example.model.ExamplePayload;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class AppleTag extends ExampleTag {

    /**
	 * @param category
	 */
    private AppleTag() {
        super(TagCategory.TERMINAL);
    }

    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(0);
        return seq;
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new ExamplePayload(this);
    }

    @Override
    public String getDescription() {
        return "Apple";
    }

    private static final AppleTag instance = new AppleTag();

    protected static AppleTag getInstance() {
        return instance;
    }

    ;
}
