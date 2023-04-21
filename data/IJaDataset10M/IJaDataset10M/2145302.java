package nl.utwente.ewi.tpl.nodeselection;

import nl.utwente.ewi.tpl.ast.tree.NodeDef;
import nl.utwente.ewi.tpl.runtime.MultiplicityType;

/**
 * {@code ResultPair} stores intermediate results during the context checking of
 * a node selection. The pair consists of a {@link NodeDef} and its
 * {@linkplain MultiplicityType multiplicity}.
 *
 * @author Emond Papegaaij
 */
public class ResultPair {

    /**
	 * The type of this {@code ResultPair}. 
	 */
    private NodeDef node;

    /**
	 * The multiplicity of this {@code ResultPair}. 
	 */
    private MultiplicityType multiplicity;

    /**
	 * Creates a new {@code ResultPair}.
	 * @param node The type of the pair.
	 * @param multiplicity The multiplicity of the pair.
	 */
    public ResultPair(NodeDef node, MultiplicityType multiplicity) {
        this.node = node;
        this.multiplicity = multiplicity;
    }

    /**
	 * Creates a new {@code ResultPair}, based on a previous one. The type will be
	 * the provided {@link NodeDef} and the multiplicity that of the base,
	 * {@linkplain MultiplicityType#multiply multiplied} by the given
	 * multiplicity.
	 * @param base The {@code ResultPair} the new pair is based on.
	 * @param node The type of the new {@code ResultPair}.
	 * @param multiplicity The multiplication factor.
	 */
    public ResultPair(ResultPair base, NodeDef node, MultiplicityType multiplicity) {
        this.node = node;
        this.multiplicity = base.getMultiplicity().multiply(multiplicity);
        assert (this.multiplicity != null) : "Invalid multiplicity combination";
    }

    /**
	 * Returns the type of the {@code ResultPair}.
	 * @return The type of the {@code ResultPair}.
	 */
    public NodeDef getNode() {
        return node;
    }

    /**
	 * Returns the multiplicity of the {@code ResultPair}.
	 * @return The multiplicity of the {@code ResultPair}.
	 */
    public MultiplicityType getMultiplicity() {
        return multiplicity;
    }

    /**
	 * Generates a hash code for the {@code ResultPair}. Both the type and
	 * multiplicity are taken into account.
	 * @return A hash code for the {@code ResultPair}.
	 */
    public int hashCode() {
        return getMultiplicity().hashCode() ^ getNode().getType().getValue().hashCode();
    }

    /**
	 * Compares this {@code ResultPair} against the given object.
	 * @return True when the given object is a {@code ResultPair} with the same
	 * type and multiplicity.
	 */
    public boolean equals(Object o) {
        if (o instanceof ResultPair) {
            ResultPair other = (ResultPair) o;
            return getMultiplicity().equals(other.getMultiplicity()) && getNode().getType().getValue().equals(other.getNode().getType().getValue());
        }
        return false;
    }

    /**
	 * Returns a string representation of the {@code ResultPair}, which can be
	 * used for debugging.
	 * @return A string representation of the {@code ResultPair}.
	 */
    public String toString() {
        return node.getType().getValue() + " " + multiplicity;
    }
}
