package unbbayes.prs.hybridbn.resources;

import java.util.ListResourceBundle;

/**
 * <p>Title: UnBBayes</p>
 * <p>Description: Resources file for unbbayes.prs.hybridbn package. Localization = english.</p>
 * <p>Company: UnB</p>
 * @author Rommel Novaes Carvalho (rommel.carvalho@gmail.com)
 * @deprecated Continuous node is no longer supported in UnBBayes core. It has 
 * now been replaced by the CPS plugin available at http://sourceforge.net/projects/prognos/.
 */
public class HybridBnResources extends ListResourceBundle {

    /**
	 *  Override getContents and provide an array, where each item in the array is a pair
	 *	of objects. The first element of each pair is a String key,
	 *	and the second is the value associated with that key.
	 *
	 * @return The resources' contents
	 */
    public Object[][] getContents() {
        return contents;
    }

    /**
	 * The resources
	 */
    static final Object[][] contents = { { "continuousNodeInvalidParentException", "A continuous node only allows:\nParent:\n-Continuous node;\n-Discrete probabilistic node.\nChild:\n-Continuous node." }, { "meanName", "Mean" }, { "varianceName", "Variance" } };
}
