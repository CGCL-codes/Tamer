package com.soundhelix.sequenceengine;

import java.util.Random;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.soundhelix.misc.Pattern;
import com.soundhelix.patternengine.PatternEngine;
import com.soundhelix.util.XMLUtils;

/**
 * Implements a sequence engine that repeats user-specified patterns in a single voice. A pattern is a string containing any number of comma-separated
 * integers, minus and plus signs. Integers play the corresponding note of the chord (0 is the base note, 1 the middle note and so on; the numbers may
 * also be negative). A minus sign is a pause. A plus sign plays a transition note between the current chord and the chord of the next non-transition
 * tone that will be played. The pitch of the transition note is based on the base notes of the two chords. This can be used for funky base lines.
 * 
 * @author Thomas Schuerger (thomas@schuerger.com)
 */
public class PatternSequenceEngine extends AbstractMultiPatternSequenceEngine {

    public PatternSequenceEngine() {
        super();
    }

    public void configure(Node node, XPath xpath) throws XPathException {
        random = new Random(randomSeed);
        NodeList nodeList = (NodeList) xpath.evaluate("patternEngine", node, XPathConstants.NODESET);
        if (nodeList.getLength() == 0) {
            throw new RuntimeException("Need at least 1 patternEngine");
        }
        try {
            setNormalizeChords(!XMLUtils.parseBoolean(random, "obeyChordSubtype", node, xpath));
            logger.warn("The tag \"obeyChordSubtype\" has been deprecated. " + "Use \"normalizeChords\" with inverted value instead.");
        } catch (Exception e) {
        }
        try {
            setNormalizeChords(XMLUtils.parseBoolean(random, "normalizeChords", node, xpath));
        } catch (Exception e) {
        }
        PatternEngine patternEngine;
        try {
            int i = random.nextInt(nodeList.getLength());
            patternEngine = XMLUtils.getInstance(PatternEngine.class, nodeList.item(i), xpath, randomSeed, i);
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating PatternEngine", e);
        }
        Pattern pattern = patternEngine.render("" + TRANSITION);
        setPatterns(new Pattern[] { pattern });
    }
}
