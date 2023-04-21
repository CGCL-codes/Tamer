package v2k.parser.tree;

/**
 *
 * @author karl
 */
public class MinTypMaxExpression {

    public MinTypMaxExpression(Expression mtm[]) {
        m_min = mtm[0];
        m_typ = mtm[1];
        m_max = mtm[2];
    }

    private Expression m_min, m_typ, m_max;
}
