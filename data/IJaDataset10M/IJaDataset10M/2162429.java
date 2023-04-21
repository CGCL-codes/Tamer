package org.alicebot.server.core.processor;

import org.alicebot.server.core.PredicateMaster;
import org.alicebot.server.core.parser.AIMLParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Toolkit;

/**
 *  Handles a
 *  <code><a href="http://www.alicebot.org/TR/2001/WD-aiml/#section-get">get</a></code>
 *  element.
 *
 *  @version    4.1.3
 *  @author     Jon Baer
 *  @author     Thomas Ringate, Pedro Colla
 */
public class GetProcessor extends AIMLProcessor {

    public static final String label = "get";

    public String process(int level, String userid, XMLNode tag, AIMLParser parser) throws AIMLProcessorException {
        if (tag.XMLType == XMLNode.EMPTY) {
            String name = Toolkit.getAttributeValue(NAME, tag.XMLAttr);
            if (name.equals(EMPTY_STRING)) {
                throw new AIMLProcessorException("<get/> must have a non-empty name attribute.");
            }
            return PredicateMaster.get(name, userid);
        } else {
            throw new AIMLProcessorException("<get/> cannot have content!");
        }
    }
}
