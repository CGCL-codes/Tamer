package v2k.parser.tree;

/**
 *
 * @author karl
 */
public class NonBlockingAssign {

    public NonBlockingAssign(Lvalue lv, DelayOrEventControl dec, Expression exp) {
        m_lval = lv;
        m_dec = dec;
        m_expr = exp;
        BlockingAssign.validate(m_lval, false);
    }

    private Lvalue m_lval;

    private DelayOrEventControl m_dec;

    private Expression m_expr;
}
