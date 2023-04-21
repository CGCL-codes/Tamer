package cunei.confusion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import cunei.collections.Lattice.Location;
import cunei.document.Phrase;
import cunei.type.SequenceType;
import cunei.type.TypeSequence;
import cunei.type.TypesOfTypes;

public class NullAnnotator implements InputAnnotator {

    private static Phrase nullPhrase;

    static {
        nullPhrase = new Phrase(0);
        final TypeSequence nullSequence = new TypeSequence(0);
        for (SequenceType type : TypesOfTypes.SEQUENCE.values()) nullPhrase.set(type, nullSequence);
    }

    private String name;

    public NullAnnotator(String configPath, String name) {
        this.name = name;
    }

    public void annotate(InputLattice lattice) {
        List<InputNode> annotations = new ArrayList<InputNode>();
        for (Map.Entry<Location, Collection<InputNode>> entry : lattice.entries()) {
            final Collection<InputNode> nodes = entry.getValue();
            for (InputNode node : nodes) {
                if (node.hasEmptySource() || !node.hasEmptyTarget() || !node.hasCompleteSource()) continue;
                annotations.add(new InputNode(node.getModel(), node.getSourcePhrase(), nullPhrase));
            }
            nodes.addAll(annotations);
            annotations.clear();
        }
    }

    public String toString() {
        return name;
    }
}
