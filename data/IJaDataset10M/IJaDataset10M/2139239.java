package vqwiki;

import junit.framework.TestCase;

/**
 * @author mteodori
 * @version $Revision: 948 $ $Date: $
 */
public class WikiBaseTest extends TestCase {

    /**
     * Test method for {@link vqwiki.WikiBase#readDefaultTopic(java.lang.String)}.
     * @throws Exception
     */
    public void testReadDefaultTopic() throws Exception {
        String[] topicNames = new String[] { "WikiHelp", "WikiHelp:AdvancedFormatting", "WikiHelp:BasicFormatting", "WikiHelp:MakingLinks" };
        for (int i = 0; i < topicNames.length; i++) {
            String topic = WikiBase.readDefaultTopic(topicNames[i]);
            assertNotNull(topic);
            assertTrue(topic.length() > 0);
        }
    }
}
