package cunei.confusion;

import java.util.Collection;
import java.util.Map;
import cunei.collections.Lattice;
import cunei.collections.ScoredSet;

public class InputLattice extends Lattice<InputNode> {

    protected static class InputSet extends ScoredSet<InputNode> {

        private static final long serialVersionUID = 1L;

        public InputSet() {
            super();
        }
    }

    public InputLattice() {
        super(InputSet.class);
    }

    public InputLattice clone() {
        return (InputLattice) super.clone();
    }

    public ConfusionLattice makeConfusionLattice() {
        ConfusionLattice result = new ConfusionLattice();
        for (Map.Entry<Location, Collection<InputNode>> entry : entries()) {
            final Location location = entry.getKey();
            for (InputNode node : entry.getValue()) result.add(location, new ConfusionNode(node));
        }
        return result;
    }
}
