package v2k.parser.tree;

import java.util.*;

/**
 *
 * @author karl
 */
public class NetIdentifiers implements TreeSymbol.Id {

    public NetIdentifiers(NetIdent id) {
        m_id = id;
    }

    void add(Dimension dim) {
        if (null == m_dims) {
            m_dims = new LinkedList<Dimension>();
        }
        m_dims.add(dim);
    }

    public Ident getId() {
        return m_id;
    }

    private NetIdent m_id;

    private List<Dimension> m_dims;
}
