package v2k.parser.tree;

/**
 *
 * @author karl
 */
public class ContinuousAssign {

    public ContinuousAssign(Delay3 d3, ListOf<NetAssign> nets) {
        m_del = d3;
        m_assigns = nets;
    }

    private Delay3 m_del;

    private ListOf<NetAssign> m_assigns;
}
