package moduledefault.classify.c45.rafael.jadti;

/**
 * A test node implementing the {@link LearningNode LearningNode}
 * interface.
 **/
public class LearningTestNode extends ScoreTestNode implements LearningNode {

    private final ItemSet learningSet;

    /**
     * Creates a new learning open node.
     *
     * @param weight The weight of this node.
     * @param test The test associated to the node.
     * @param score The score associated to the test.
     **/
    public LearningTestNode(double weight, Test test, double score, ItemSet learningSet) {
        super(weight, test, score);
        for (int i = 0; i < nbSons(); i++) son(i).replace(new LearningOpenNode(son(i).weight, null));
        this.learningSet = learningSet;
    }

    public ItemSet learningSet() {
        return learningSet;
    }

    public void replace(Node node) {
        if (!(node instanceof LearningNode)) throw new IllegalArgumentException("A learning node can only " + "be replaced by another " + "learning node");
        super.replace(node);
    }
}
