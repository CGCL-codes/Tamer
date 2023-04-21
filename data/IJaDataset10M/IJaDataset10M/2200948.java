package org.goodoldai.jeff.explanation.builder;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;

/**
 * A concrete builder for creating text explanation chunks. Unlike the 
 * TextExplanationChunkBuilder, this builder does not perform 
 * internationaization.
 *
 * @author Bojan Tomic
 */
public class SimpleTextExplanationChunkBuilder implements ExplanationChunkBuilder {

    /**
     * Initializes the builder
     */
    public SimpleTextExplanationChunkBuilder() {
    }

    /**
     * This method creates a TextExplanationChunk instance based on the 
     * arguments provided and returns it as a return value. In this case, the 
     * chunk content is text (String instance).
     *
     * @param context chunk context
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content - in this case a String containing
     * the explanation
     *
     * @return created TextExplanationChunk instance
     */
    public ExplanationChunk buildChunk(int context, String group, String rule, String[] tags, Object content) {
        return new TextExplanationChunk(context, group, rule, tags, content);
    }
}
