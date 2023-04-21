package org.alicebot.server.core.parser;

import java.util.Vector;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.processor.AIMLProcessorException;
import org.alicebot.server.core.processor.AIMLProcessorRegistry;
import org.alicebot.server.core.processor.ProcessorException;
import org.alicebot.server.core.processor.ProcessorRegistry;

/**
 *  <code>AIMLParser</code> is still a primitive class, implementing not a
 *  &quot;real&quot; XML parser, but just enough (hopefully) to get the job done.
 */
public class AIMLParser extends GenericParser {

    /** The values captured from the input by wildcards in the <code>pattern</code>. */
    private Vector inputStars = new Vector();

    /** The values captured from the input path by wildcards in the <code>that</code>. */
    private Vector thatStars = new Vector();

    /** The values captured from the input path by wildcards in the <code>topic</code>. */
    private Vector topicStars = new Vector();

    /** The input that matched the <code>pattern</code> associated with this template (helps to avoid endless loops). */
    private Vector inputs = new Vector();

    /**
     *  Initializes an <code>AIMLParser</code>.
     *  The <code>input</code> is a required parameter!
     *
     *  @param input    the input that matched the <code>pattern</code>
     *                  associated with this template (helps to avoid endless loops)
     *
     *  @throws AIMLParserException if the <code>input</code> is null
     */
    public AIMLParser(String input) throws AIMLParserException {
        if (input == null) {
            throw new AIMLParserException("No input supplied for AIMLParser!");
        }
        this.inputs.add(input);
        super.processorRegistry = Globals.getAIMLProcessorRegistry();
    }

    /**
     *  Processes the AIML within and including a given AIML element.
     *
     *  @param level    the current level in the XML trie
     *  @param userid   the user identifier
     *  @param tag      the tag being evaluated
     *
     *  @return the result of processing the tag
     *
     *  @throws AIMLProcessorException if the AIML cannot be processed
     */
    public String processTag(int level, String userid, XMLNode tag) throws ProcessorException {
        try {
            return super.processTag(level, userid, tag);
        } catch (ProcessorException e0) {
            if (Globals.supportDeprecatedTags()) {
                try {
                    return DeprecatedAIMLParser.processTag(level, userid, tag, this);
                } catch (UnknownDeprecatedAIMLException e1) {
                }
            }
            if (Globals.nonAIMLRequireNamespaceQualification()) {
                if (tag.XMLData.indexOf(COLON) == -1) {
                    throw new AIMLProcessorException("Unknown element \"" + tag.XMLData + "\"");
                }
            }
            return formatTag(level, userid, tag);
        }
    }

    /**
     *  Adds an input to the inputs vector (for avoiding infinite loops).
     *
     *  @param input    the input to add
     */
    public void addInput(String input) {
        this.inputs.add(input);
    }

    /**
     *  Returns the input that matched the <code>pattern</code> associated with this template.
     *
     *  @return the input that matched the <code>pattern</code> associated with this template
     */
    public Vector getInputs() {
        return this.inputs;
    }

    /**
     *  Returns the values captured from the input path by wildcards in the <code>pattern</code>.
     *
     *  @return the values captured from the input path by wildcards in the <code>pattern</code>
     */
    public Vector getInputStars() {
        return this.inputStars;
    }

    /**
     *  Returns the the values captured from the input path by wildcards in the <code>that</code>.
     *
     *  @return the values captured from the input path by wildcards in the <code>that</code>
     */
    public Vector getThatStars() {
        return this.thatStars;
    }

    /**
     *  Returns the values captured from the input path by wildcards in the <code>topic name</code>.
     *
     *  @return the values captured from the input path by wildcards in the <code>topic name</code>
     */
    public Vector getTopicStars() {
        return this.topicStars;
    }

    /**
     *  Sets the <code>inputStars</code> Vector.
     *
     *  @param  values captured from the input path by wildcards in the <code>pattern</code>
     */
    public void setInputStars(Vector vector) {
        this.inputStars = vector;
    }

    /**
     *  Sets the <code>thatStars</code> Vector.
     *
     *  @param  values captured from the input path by wildcards in the <code>that</code>
     */
    public void setThatStars(Vector vector) {
        this.thatStars = vector;
    }

    /**
     *  Sets the <code>topicStars</code> Vector.
     *
     *  @param  values captured from the input path by wildcards in the <code>topic name</code>
     */
    public void setTopicStars(Vector vector) {
        this.topicStars = vector;
    }
}
