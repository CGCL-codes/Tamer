package org.newsml.toolkit.conformance;

import java.io.IOException;
import org.newsml.toolkit.BaseNode;
import org.newsml.toolkit.FormalNameNode;
import org.newsml.toolkit.NewsMLException;
import org.newsml.toolkit.TopicSet;

/**
 * FormalName-specific conformance tests.
 *
 * <p>This class tests that a FormalNameNode has a vocabulary (either
 * explicit or defaulted), and, if possible, confirms that the formal
 * name appears in that vocabulary.  It is possible to pass on option
 * to the constructor to disable the error report if a vocabulary is
 * not available (for NewsItemId and ProviderId).</p>
 *
 * @author Reuters PLC
 * @version 2.0
 * @see NewsMLTestManager
 */
public class FormalNameTest extends TestBase {

    /**
     * Default constructor.
     *
     * <p>By default, the test will report an error if a vocabulary
     * is not specified for the formal name.</p>
     */
    public FormalNameTest() {
        this(true);
    }

    /**
     * Constructor.
     *
     * @param requireVocab true if the test should report an error if
     * a vocabulary is not specified, false otherwise.
     */
    public FormalNameTest(boolean requireVocab) {
        this.requireVocab = requireVocab;
    }

    /**
     * Run tests for a FormalNameNode.
     *
     * @param node A FormalNameNode.
     * @param useExternal Not used.
     * @exception NewsMLException If the test fails.
     * @exception java.lang.ClassCastException If the node parameter is not
     *   an instance of {@link FormalNameNode}. 
     * @see TestBase#run
     */
    public void run(BaseNode node, boolean useExternal) throws NewsMLException {
        FormalNameNode formalName = (FormalNameNode) node;
        String name;
        String vocab;
        String scheme;
        try {
            name = formalName.getName().toString();
            vocab = formalName.getDefaultVocabulary(useExternal);
            scheme = formalName.getDefaultScheme(useExternal);
        } catch (IOException e) {
            throw new NewsMLException(e);
        }
        if (!requireVocab && vocab == null) return;
        if (useExternal) {
            if (vocab == null) error("No vocabulary found for formal name: " + name);
        } else {
            if (vocab == null) error("No vocabulary found for formal name: " + name);
        }
        if (Util.isDuidRef(vocab) || Util.isHttpUrl(vocab)) {
            try {
                if (formalName.findTopic(useExternal) == null) warning("Cannot retrieve topic for formal name " + name);
            } catch (IOException e) {
                throw new NewsMLException(e);
            }
        } else if (Util.isNewsMLUrn(vocab)) {
        } else {
            error("Vocabulary \"" + vocab + "\" is not a Duid ref, NewsML URN, or HTTP URL");
        }
    }

    private boolean requireVocab;
}
