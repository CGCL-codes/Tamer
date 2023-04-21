package edu.cmu.sphinx.linguist.language.grammar;

import edu.cmu.sphinx.linguist.dictionary.Dictionary;
import edu.cmu.sphinx.result.Lattice;
import edu.cmu.sphinx.result.Node;
import edu.cmu.sphinx.result.Edge;
import edu.cmu.sphinx.util.props.PropertyException;
import edu.cmu.sphinx.util.props.PropertySheet;
import java.io.IOException;
import java.util.HashMap;

/**
 * A grammar build from a lattice. Can be used for a second and subsequent
 * passes of multi-pass recognition.
 */
public class LatticeGrammar extends Grammar {

    public Lattice lattice;

    public LatticeGrammar(Lattice lattice, boolean showGrammar, boolean optimizeGrammar, boolean addSilenceWords, boolean addFillerWords, Dictionary dictionary) {
        super(showGrammar, optimizeGrammar, addSilenceWords, addFillerWords, dictionary);
        this.lattice = lattice;
    }

    public LatticeGrammar() {
    }

    @Override
    public void newProperties(PropertySheet ps) throws PropertyException {
        super.newProperties(ps);
    }

    /**
     * Creates the grammar from the language model. This Grammar contains one word per grammar node. Each word (and
     * grammar node) is connected to all other words with the given probability
     *
     * @return the initial grammar node
     */
    @Override
    protected GrammarNode createGrammar() throws IOException {
        if (lattice == null) {
            return createGrammarNode("<s>");
        }
        lattice.removeFillers();
        GrammarNode firstNode = null;
        HashMap<Node, GrammarNode> nodeMap = new HashMap<Node, GrammarNode>();
        for (Node n : lattice.getNodes()) {
            String word = n.getWord().toString();
            GrammarNode node = createGrammarNode(word);
            if (n.equals(lattice.getInitialNode())) firstNode = node;
            if (n.equals(lattice.getTerminalNode())) node.setFinalNode(true);
            nodeMap.put(n, node);
        }
        if (firstNode == null) {
            throw new Error("No lattice start found");
        }
        for (Edge e : lattice.getEdges()) {
            float logProbability = (float) e.getLMScore();
            GrammarNode prevNode = nodeMap.get(e.getFromNode());
            GrammarNode toNode = nodeMap.get(e.getToNode());
            prevNode.add(toNode, logProbability);
        }
        return firstNode;
    }

    public void setLattice(Lattice lattice) throws IOException {
        this.lattice = lattice;
        allocate();
    }
}
