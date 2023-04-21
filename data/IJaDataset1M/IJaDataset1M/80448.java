package edu.mit.wi.omnigene.omnidas;

/**
 * The implementating class for the <code>Feature</code> interface.
 *
 * @author David Turner
 * @version 1.0
 */
public class FeatureImpl implements Feature {

    private String id;

    private String description;

    private Type type;

    private Method method;

    private Range range;

    private String score;

    private String orientation;

    private String phase;

    private String[] note;

    private Link[] link;

    private Target[] target;

    private Group[] group;

    private Segment[] segment;

    /**
     * Constructs a new <code>Feature</code> object.
     */
    public FeatureImpl() {
        this(null, null, null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Constructs a new <code>Feature</code> object with the indicated
     * parameter values.
     *
     * @param id the feature id.
     * @param description the feature description.
     * @param type the type of this feature.
     * @param method the method the feature was found.
     * @param range the range of the feature.
     * @param score the score of the method used to find this feature
     * @param orientation the orientation of the feature relative to the 
     *                    direction of the transcription.  
     * @param phase the position of the feature relative to open reading frame.
     * @param note a human readable note
     * @param link a link to a webpage that provide more information.
     * @param target the target sequence in a sequence similarity match.
     * @param group a unique group id.
     */
    public FeatureImpl(String id, String description, Type type, Method method, Range range, String score, String orientation, String phase, String[] note, Link[] link, Target[] target, Group[] group) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.method = method;
        this.range = range;
        this.score = score;
        this.orientation = orientation;
        this.phase = phase;
        this.note = note;
        this.link = link;
        this.target = target;
        this.group = group;
    }

    /**
     * Returns the id for this <code>Feature</code>.
     *
     * @return the id value for this Feature.
     */
    public String getID() {
        return this.id;
    }

    /**
     * Sets the id for this <code>Feature</code>.
     *
     * @param id the id value for this Feature.
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Returns the description for this <code>Feature</code>.
     *
     * @return the description value for this Feature.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the Description for this <code>Feature</code>.
     *
     * @param description the description value for this Feature.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the type for this <code>Feature</code>.
     *
     * @return the type value for this Feature.
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Sets the Type for this <code>Feature</code>.
     *
     * @param type the type value for this Feature.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns the method for this <code>Feature</code>.
     *
     * @return the method value for this Feature.
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * Sets the method for this <code>Feature</code>.
     *
     * @param method the method value for this Feature.
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * Returns the range for this <code>Feature</code>.
     *
     * @return the range value for this Feature.
     */
    public Range getRange() {
        return this.range;
    }

    /**
     * Sets the range for this <code>Feature</code>.
     *
     * @param range the range value for this Feature.
     */
    public void setRange(Range range) {
        this.range = range;
    }

    /**
     * Returns the score for this <code>Feature</code>.
     *
     * @return the score value for this Feature.
     */
    public String getScore() {
        return this.score;
    }

    /**
     * Sets the score for this <code>Feature</code>.
     *
     * @param score the score value for this Feature.
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * Returns the orientation for this <code>Feature</code>.
     *
     * @return the orientation value for this Feature.
     */
    public String getOrientation() {
        return this.orientation;
    }

    /**
     * Sets the orientation for this <code>Feature</code>.
     *
     * @param orientation the orientation value for this Feature.
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * Returns the phase for this <code>Feature</code>.
     *
     * @return the phase value for this Feature.
     */
    public String getPhase() {
        return this.phase;
    }

    /**
     * Sets the phase for this <code>Feature</code>.
     *
     * @param phase the phase value for this Feature.
     */
    public void setPhase(String phase) {
        this.phase = phase;
    }

    /**
     * Returns the note for this <code>Feature</code>.
     *
     * @return the not value for this Feature.
     */
    public String[] getNote() {
        return this.note;
    }

    /**
     * Sets the note for this <code>Feature</code>.
     *
     * @param note the note value for this Feature.
     */
    public void setNote(String[] note) {
        this.note = note;
    }

    /**
     * Returns the link for this <code>Feature</code>.
     *
     * @return the link value for this Feature.
     */
    public Link[] getLink() {
        return this.link;
    }

    /**
     * Sets the link for this <code>Feature</code>.
     *
     * @param link the link value for this Feature.
     */
    public void setLink(Link[] link) {
        this.link = link;
    }

    /**
     * Returns the target for this <code>Feature</code>.
     *
     * @return the target value for this Feature.
     */
    public Target[] getTarget() {
        return this.target;
    }

    /**
     * Sets the Target for this <code>Feature</code>.
     *
     * @param target the target value for this Feature.
     */
    public void setTarget(Target[] target) {
        this.target = target;
    }

    /**
     * Returns the Group for this <code>Feature</code>.
     *
     * @return the group value for this Feature.
     */
    public Group[] getGroup() {
        return this.group;
    }

    /**
     * Sets the group for this <code>Feature</code>.
     *
     * @param group the group value for this Feature.
     */
    public void setGroup(Group[] group) {
        this.group = group;
    }

    public void setSegment(Segment[] segment) {
        this.segment = segment;
    }

    public Segment[] getSegment() {
        return segment;
    }
}
