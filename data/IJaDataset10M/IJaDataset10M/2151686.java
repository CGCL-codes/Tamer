package uk.ac.ebi.intact.application.editor.struts.view;

import uk.ac.ebi.intact.application.editor.business.EditorService;
import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.business.IntactHelper;
import uk.ac.ebi.intact.model.Annotation;
import uk.ac.ebi.intact.model.CvTopic;

/**
 * Bean to store data for comments (annotations).
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: CommentBean.java 4045 2005-06-20 08:50:56Z smudali $
 */
public class CommentBean extends AbstractEditKeyBean {

    /**
     * Reference to the annotation object. Transient as it can be created using
     * values in the bean.
     */
    private transient Annotation myAnnotation;

    /**
     * The CV topic.
     */
    private String myTopic;

    /**
     * The topic as as a link
     */
    private String myTopicLink;

    /**
     * The annotated text.
     */
    private String myAnnotatedText;

    /**
     * Default constructor. Used for creating a new annotation.
     *
     * @see #clear()
     */
    public CommentBean() {
    }

    /**
     * Instantiate an object of this class from an Annotation object. The key
     * is set to a default value (unique).
     * @param annotation the underlying <code>Annotation</code> object.
     */
    public CommentBean(Annotation annotation) {
        initialize(annotation);
    }

    /**
     * Instantiates with given annotation and key.
     * @param annotation the underlying <code>Annotation</code> object.
     * @param key the key to assigned to this bean.
     */
    public CommentBean(Annotation annotation, long key) {
        super(key);
        initialize(annotation);
    }

    /**
     * Override to make a clone of this object.
     *
     * @return a cloned version of the current instance.
     * @throws CloneNotSupportedException for errors in cloning.
     */
    public Object clone() throws CloneNotSupportedException {
        CommentBean copy = (CommentBean) super.clone();
        copy.myAnnotation = null;
        return copy;
    }

    /**
     * Updates the internal annotation with the new values from the form.
     * @param helper the IntactHelper to search the database
     * @return an Annotation created or updated using values in the bean.
     * @throws IntactException for errors in searching for a CvTopic.
     */
    public Annotation getAnnotation(IntactHelper helper) throws IntactException {
        CvTopic cvtopic = (CvTopic) helper.getObjectByLabel(CvTopic.class, getTopic());
        if (myAnnotation == null) {
            myAnnotation = new Annotation(getService().getOwner(), cvtopic);
        } else {
            myAnnotation.setCvTopic(cvtopic);
        }
        myAnnotation.setAnnotationText(getDescription());
        return myAnnotation;
    }

    /**
     * Returns the topic.
     * @return topic as a <code>String</code>.
     */
    public String getTopic() {
        return myTopic;
    }

    /**
     * Returns the topic with a link to show its contents in a window.
     * @return the topic as a browsable link.
     */
    public String getTopicLink() {
        if (myTopicLink == null) {
            setTopicLink();
        }
        return myTopicLink;
    }

    /**
     * Sets the topic.
     * @param topic the new topic as a <code>String</code>.
     */
    public void setTopic(String topic) {
        if (!topic.equals(myTopic)) {
            myTopic = topic;
            setTopicLink();
        }
    }

    /**
     * Returns the annotated text.
     * @return description as a <code>String</code>.
     */
    public String getDescription() {
        return myAnnotatedText;
    }

    /**
     * Sets the annotated text.
     * @param text the annotated text as a <code>String</code>.
     */
    public void setDescription(String text) {
        myAnnotatedText = text.trim();
    }

    /**
     * Resets fields to blanks, so the addAnnotation form doesn't display
     * previous values.
     */
    public void clear() {
        myTopic = null;
        myTopicLink = null;
        myAnnotatedText = null;
    }

    /**
     * Returns true if given bean is equivalent to the current bean.
     * @param cb the bean to compare.
     * @return true if topic and text are equivalent; otherwise false is returned.
     */
    public boolean isEquivalent(CommentBean cb) {
        return cb.getTopic().equals(getTopic()) && cb.getDescription().equals(getDescription());
    }

    /**
     * Intialize the member variables using the given Annotation object.
     * @param annotation <code>Annotation</code> object to populate this bean.
     */
    private void initialize(Annotation annotation) {
        myAnnotation = annotation;
        myTopic = annotation.getCvTopic().getShortLabel();
        setTopicLink();
        myAnnotatedText = annotation.getAnnotationText();
    }

    private void setTopicLink() {
        myTopicLink = getLink(EditorService.getTopic(CvTopic.class), myTopic);
    }
}
