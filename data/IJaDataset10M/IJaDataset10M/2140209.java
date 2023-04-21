package org.goodoldai.jeff.explanation;

/**
 * This abstract class represents one piece of explanation - the
 * explanation chunk.
 *
 * The idea is that when an expert system rule gets executed (fired), zero,
 * one or more explanation chunks get created and inserted into the 
 * explanation. The "rule" and "group" attributes represent the identifier
 * and the rule group of the executed rule.
 *
 * Each explanation chunk is supposed to hold a certain type of content:
 * text, data or image (additional types can be added by subclassing). 
 * Concrete explanation chunk types are implemented as subclasses of this 
 * abstract class.
 *
 * The information contained in the explanation can be a warning, error,
 * alert or it can be just informational. This is referred to as context. 
 * This class has static constants representing some predefined context 
 * values (e.g. "INFORMATIONAL", "WARNING"). Additionally,
 * chunks can be tagged with one or more tags in order to better explain 
 * their contents and what they refer to.
 *
 * @author Bojan Tomic
 */
public abstract class ExplanationChunk implements Cloneable {

    /**
     * Static constant representing informational context.
     *
     * This is the default
     * value for context.When the "context" attribute is set to this
     * value, it means that the chunk holds some informational content.
     */
    public static final int INFORMATIONAL = 0;

    /**
     * Static constant representing a warning context.
     *
     * When the
     * "context" attribute is set to this value, it means that the chunk
     * contains a warning.
     */
    public static final int WARNING = -5;

    /**
     * Static constant representing an error context.
     * 
     * When the "context"
     * attribute is set to this value, it means that the chunks content refers 
     * to an error.
     */
    public static final int ERROR = -10;

    /**
     * Static constant representing a "positive" context.
     * 
     * When the "context" attribute is set to this value, it means that the chunks
     * content refers to some positive conclusion or event (e.g. "The profit is
     * on the rise this year").
     * 
     * The "POSITIVE", "VERY_POSITIVE", "NEGATIVE" and "VERY_NEGATIVE"
     * context values were created in order to mark "positive"
     * and "negative" contexts.
     */
    public static final int POSITIVE = 1;

    /**
     * Static constant representing a "very positive" context.
     * 
     * When the "context" attribute is set to this value, it means that the chunks
     * content refers to some very positive conclusion or event (e.g. "The
     * profit has doubled since last year").
     * 
     * The difference between "positive" and "very positive" context is
     * subjective so it is left to the developer to decide which one to use.
     *
     * The "POSITIVE", "VERY_POSITIVE", "NEGATIVE" and "VERY_NEGATIVE"
     * context values were created in order to mark "positive"
     * and "negative" contexts.
     */
    public static final int VERY_POSITIVE = 2;

    /**
     * Static constant representing a "negative" context.
     * 
     * When the "context" attribute is set to this value, it means that the chunks
     * content refers to some negative conclusion or event (e.g. "The profit
     * has declined since last year").
     *
     * The "POSITIVE", "VERY_POSITIVE", "NEGATIVE" and "VERY_NEGATIVE"
     * context values were created in order to mark "positive"
     * and "negative" contexts.
     */
    public static final int NEGATIVE = -1;

    /**
     * Static constant representing a "very negative" context.
     * 
     * When the "context" attribute is set to this value, it means that the chunks
     * content refers to some very negative conclusion or event (e.g. "The
     * profit is negative - money is being lost").
     * 
     * The difference between "negative" and "very negative" context is
     * subjective so it is left to the developer to decide which one to use.
     *
     * The "POSITIVE", "VERY_POSITIVE", "NEGATIVE" and "VERY_NEGATIVE"
     * context values were created in order to mark "positive"
     * and "negative" contexts.
     */
    public static final int VERY_NEGATIVE = -2;

    /**
     * Static constant representing a "strategy" context.
     *
     * When the "context" attribute is set to this value, it means that the chunks
     * content refers to some strategic conclusion. This conclusion refers to the
     * "strategy" type of expert system explanation as it effects future
     * inferences on a strategic level (e.g. "It can be determined that the car
     * malfunction is in the electrical system, so future inference is focused on
     * it").
     */
    public static final int STRATEGIC = 10;

    /**
     * The information contained in the explanation can be a warning, error, 
     * alert or just informational. This is referred to as context.
     *
     * This class has static constants representing some predefined context
     * values (e.g. "INFORMATIONAL", "WARNING").
     */
    private int context;

    /**
     * The group to which the executed rule belongs (see attribute "rule").
     */
    private String group;

    /**
     * When a rule gets executed, zero one or more explanation chunks get 
     * inserted into the explanation. In some cases (e.g. testing) it is 
     * important to keep track of the rule that initiated the creation of each 
     * chunk.
     *
     * This attribute holds the name (identifier) of the rule who's execution 
     * initiated the creation of this explanation chunk.
     *
     * Rules can be divided into groups for easier maintenance, so an
     * additional (optional) rule identifier is the name of the rule group (see 
     * attribute "group").
     */
    private String rule;

    /**
     * The main purpose of explanation chunks is to hold some piece of 
     * information (text, data, images or something else). This is referred to 
     * as content and it is mandatory.
     *
     * Since this is an abstract class representing any type of explanation 
     * chunk, the "content" attribute type is Object. This means that the
     * content can be anything. Concrete classes that inherit this abstract 
     * class must implement the "setContent" method to ensure that only
     * appropriate conent can be inserted.
     */
    protected Object content;

    /**
     * Chunks can be (optionally) tagged with one or more tags in order to 
     * better annotate their contents and what they refer to. Tags can be 
     * keywords, or some other metadata.
     */
    private String[] tags;

    /**
     * This constructor sets the "context" attribute to "INFORMATIONAL", and
     * the "content" attribute to the argument value (by calling the abstract
     * "setContent" method). All other attributes are set to null.
     * 
     * @param content chunk content
     */
    public ExplanationChunk(Object content) {
        context = INFORMATIONAL;
        group = null;
        rule = null;
        tags = null;
        setContent(content);
    }

    /**
     * This constructor sets all attribute values to argument values by
     * calling the appropriate "set" methods.
     *
     * @param context chunk context
     * @param group rule group
     * @param rule rule
     * @param content chunk content
     * @param tags tags related to the chunk
     */
    public ExplanationChunk(int context, String group, String rule, String[] tags, Object content) {
        setContext(context);
        setGroup(group);
        setRule(rule);
        setTags(tags);
        setContent(content);
    }

    /**
     * Returns the chunk content.
     *
     * @return Chunk content as Object. Since ExplanationChunk is an abstract
     * class it is left for concrete subclasses to define what content type 
     * should actually be.
     */
    public Object getContent() {
        return content;
    }

    /**
     * Sets the chunk content, and performs validation beforehand.
     *
     * Since this is an abstract method it is left for concrete subclasses to
     * redefine this method and define what content type should actually be, 
     * and how it should be validated.
     *
     * @param val chunk content
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the entered content is null
     * or in some way inadequate.
     */
    public abstract void setContent(Object val);

    /**
     * Returns the context value.
     *
     * @return integer value representing context
     */
    public int getContext() {
        return context;
    }

    /**
     * Sets the context value. It can be any integer value, not just the 
     * predefined ones.
     *
     * @param val integer representing context value
     */
    public void setContext(int val) {
        this.context = val;
    }

    /**
     * Returns the rule group identifier.
     *
     * @return string representing the rule group identifier or null if the group
     * is not specified.
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the rule group identifier.
     *
     * @param val String representing the rule group identifier. Since this value
     * is optional, null and empty strings are allowed.
     */
    public void setGroup(String val) {
        this.group = val;
    }

    /**
     * Returns the rule name (identifier).
     *
     * @return string representing the rule name or null if the rule is not
     * specified.
     */
    public String getRule() {
        return rule;
    }

    /**
     * Sets the rule name (identifier).
     *
     * @param val String representing the rule name. Since this value is
     * optional, null and empty strings are allowed.
     */
    public void setRule(String val) {
        this.rule = val;
    }

    /**
     * Returns all tags for this chunk.
     *
     * @return a string array
     * containing all tags for this chunk or null if no tags are specified.
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * Sets the tags for this chunk.
     *
     * @param val Array of string values representing tags. Since tags are
     * optional, null and empty arrays are allowed.
     */
    public void setTags(String[] val) {
        this.tags = val;
    }

    /**
     * Performs a "deep" cloning operation for this class.
     *
     * Sincee the class is abstract, so is this method. Subclasses
     * are expected to implement this method.
     *
     * @return object clone
     */
    @Override
    public abstract ExplanationChunk clone();
}
