package sjp.hg;

/**
 * This class provides a default implementation of the visitor, in which
 * the method perform no action.  Just override the methods you wish to
 * implement.
 * @author stacy
 * @version $Revision: 1.1.1.1 $
 * @see sjp.hg.HTML#accept(Visitor)
 */
public class VisitorAdapter implements Visitor {

    /**
     * @see sjp.hg.Visitor#visit(sjp.hg.Document)
     */
    public void visit(Document doc) {
    }

    /**
     * @see sjp.hg.Visitor#visit(sjp.hg.Element)
     */
    public void visit(Element elt) {
    }

    /**
     * @see sjp.hg.Visitor#visit(sjp.hg.Comment)
     */
    public void visit(Comment comment) {
    }

    /**
     * @see sjp.hg.Visitor#visit(sjp.hg.Text)
     */
    public void visit(Text text) {
    }

    /**
     * @see sjp.hg.Visitor#visit(sjp.hg.Attribute)
     */
    public void visit(Attribute attribute) {
    }

    /**
     * @see sjp.hg.Visitor#visit(sjp.hg.PI)
     */
    public void visit(PI pi) {
    }

    /**
     * @see sjp.hg.Visitor#visit(java.lang.String)
     */
    public void visit(String cdata) {
    }
}
