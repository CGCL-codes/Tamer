package collab.fm.server.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import collab.fm.server.bean.protocol.Request;
import collab.fm.server.bean.protocol.ResponseGroup;
import collab.fm.server.util.exception.EntityPersistenceException;
import collab.fm.server.util.exception.InvalidOperationException;

public class FilterChain {

    private List<Filter> chain;

    private Iterator<Filter> itr;

    public FilterChain() {
        chain = new ArrayList<Filter>();
        itr = null;
    }

    public void addFilter(Filter f) {
        chain.add(f);
    }

    public void doNextFilter(Request req, ResponseGroup rg) throws EntityPersistenceException, InvalidOperationException {
        if (itr == null) {
            itr = chain.iterator();
        }
        if (itr.hasNext()) {
            itr.next().doFilter(req, rg, this);
        }
    }

    public void doDisconnectUser(String addr, ResponseGroup rg) {
        if (itr == null) {
            itr = chain.iterator();
        }
        if (itr.hasNext()) {
            itr.next().doDisconnectUser(addr, rg, this);
        }
    }
}
