package visreed.extension.example.tag;

import java.util.ArrayList;
import java.util.List;
import visreed.model.VisreedNode;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public abstract class ExampleTag extends VisreedTag {

    public ExampleTag(TagCategory category) {
        super(category);
    }

    public static final VisreedTag SEQUENCE = SequenceTag.getInstance();

    public static final VisreedTag APPLE = AppleTag.getInstance();

    public static final VisreedTag STRAWBERRY = StrawberryTag.getInstance();

    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(SEQUENCE);
        return seq;
    }

    @Override
    protected VisreedNode createSEQ(VisreedWholeGraph wholeGraph) {
        return wholeGraph.makeRootNode(SEQUENCE);
    }
}
