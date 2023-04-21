package depth.formatting.nestedblock;

import java.util.List;
import depth.formatting.Grouper;
import depth.formatting.Templates;
import depth.tree.TokenNode;

public class NestedBlockGrouper extends Grouper<NestedBlockGroup> {

    public NestedBlockGrouper(List<TokenNode> nodes, int depth) {
        super(nodes, depth);
    }

    public boolean belongsToGroup(NestedBlockGroup group, TokenNode tn) {
        NestedBlockInformation bi = (NestedBlockInformation) tn.getInformation(Templates.NESTEDBLOCK);
        if (bi == null) {
            return group.getBlockIndex() == -1;
        } else {
            return group.getBlockIndex() == bi.getBlockIndex(getDepth());
        }
    }

    public NestedBlockGroup createNewGroup(TokenNode tn) {
        NestedBlockInformation bi = (NestedBlockInformation) tn.getInformation(Templates.NESTEDBLOCK);
        return new NestedBlockGroup(bi == null ? -1 : bi.getBlockIndex(getDepth()), getDepth());
    }
}
