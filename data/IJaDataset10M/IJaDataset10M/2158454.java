package unbbayes.prs.bn;

import java.util.ArrayList;
import java.util.List;
import unbbayes.prs.Graph;
import unbbayes.prs.INode;
import unbbayes.util.Debug;

/**
 * This is the default implementation of {@link ILikelihoodExtractor}.
 * Use
 * @author Shou Matsumoto
 *
 */
public class LikelihoodExtractor implements ILikelihoodExtractor {

    /**
	 * default constructor is protected to allow inheritance.
	 * @deprecated use {@link #newInstance()} instead.
	 */
    protected LikelihoodExtractor() {
    }

    /**
	 * Default constructor method.
	 * @return
	 */
    public static ILikelihoodExtractor newInstance() {
        return new LikelihoodExtractor();
    }

    /**
	 * Just extracts the likelihood of the first node, assuming that the node
	 * is a {@link TreeVariable}.
	 * @see unbbayes.prs.bn.ILikelihoodExtractor#extractLikelihoodRatio(java.util.List)
	 */
    public float[] extractLikelihoodRatio(Graph graph, INode node) {
        try {
            return ((TreeVariable) node).getLikelihood();
        } catch (Exception e) {
            Debug.println(getClass(), e.getMessage(), e);
        }
        return null;
    }

    public List<INode> extractLikelihoodParents(Graph graph, INode node) {
        List<INode> ret = null;
        try {
            ret = ((TreeVariable) node).getLikelihoodParents();
        } catch (ClassCastException e) {
            Debug.println(getClass(), e.getMessage(), e);
        }
        if (ret == null) {
            ret = new ArrayList<INode>();
        }
        return ret;
    }
}
