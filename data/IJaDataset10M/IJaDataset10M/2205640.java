package orgx.jdom;

/**
 * An XML comment. Methods allow the user to get and set the text of the
 * comment.
 *
 * @version $Revision: 1.33 $, $Date: 2007/11/10 05:28:58 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 */
public class Comment extends Content {

    private static final String CVS_ID = "@(#) $RCSfile: Comment.java,v $ $Revision: 1.33 $ $Date: 2007/11/10 05:28:58 $ $Name: jdom_1_1 $";

    /** Text of the <code>Comment</code> */
    protected String text;

    /**
     * Default, no-args constructor for implementations to use if needed.
     */
    protected Comment() {
    }

    /**
     * This creates the comment with the supplied text.
     *
     * @param text <code>String</code> content of comment.
     */
    public Comment(String text) {
        setText(text);
    }

    /**
     * Returns the XPath 1.0 string value of this element, which is the
     * text of this comment.
     *
     * @return the text of this comment
     */
    public String getValue() {
        return text;
    }

    /**
     * This returns the textual data within the <code>Comment</code>.
     *
     * @return <code>String</code> - text of comment.
     */
    public String getText() {
        return text;
    }

    /**
     * This will set the value of the <code>Comment</code>.
     *
     * @param text <code>String</code> text for comment.
     * @return <code>Comment</code> - this Comment modified.
     * @throws IllegalDataException if the given text is illegal for a
     *         Comment.
     */
    public Comment setText(String text) {
        String reason;
        if ((reason = Verifier.checkCommentData(text)) != null) {
            throw new IllegalDataException(text, "comment", reason);
        }
        this.text = text;
        return this;
    }

    /**
     * This returns a <code>String</code> representation of the
     * <code>Comment</code>, suitable for debugging. If the XML
     * representation of the <code>Comment</code> is desired,
     * {@link orgx.jdom.output.XMLOutputter#outputString(Comment)}
     * should be used.
     *
     * @return <code>String</code> - information about the
     *         <code>Attribute</code>
     */
    public String toString() {
        return new StringBuffer().append("[Comment: ").append(new orgx.jdom.output.XMLOutputter().outputString(this)).append("]").toString();
    }
}
