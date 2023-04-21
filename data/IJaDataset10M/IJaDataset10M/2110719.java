package html;

/**
 *
 * @author jhierrot
 */
public class Span extends Container {

    /** Creates a new instance of Span */
    public Span() {
    }

    @Override
    protected String StartCont() {
        return ("<span " + CalcStyle() + ">");
    }

    @Override
    protected String EndCont() {
        return ("</span>");
    }
}
