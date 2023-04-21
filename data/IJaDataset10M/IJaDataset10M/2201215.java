package net.fortytwo.ripple.util;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.flow.Buffer;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import java.util.HashSet;
import java.util.Set;

public class BNodeClosureFilter implements Sink<Resource, RippleException> {

    private final Set<Resource> visited;

    private final Sink<Statement, RippleException> sink;

    private final SailConnection sailConnection;

    private final Buffer<Resource, RippleException> buffer;

    public BNodeClosureFilter(final Sink<Statement, RippleException> sink, final SailConnection sc) {
        this.sink = sink;
        sailConnection = sc;
        buffer = new Buffer<Resource, RippleException>(this);
        visited = new HashSet<Resource>();
    }

    public void put(final Resource r) throws RippleException {
        if (visited.contains(r)) {
            return;
        } else {
            visited.add(r);
        }
        try {
            CloseableIteration<? extends Statement, SailException> stmtIter = sailConnection.getStatements(r, null, null, false);
            while (stmtIter.hasNext()) {
                Statement st = stmtIter.next();
                sink.put(st);
                Value obj = st.getObject();
                if (isBlankNode(obj)) {
                    buffer.put((Resource) obj);
                }
            }
            stmtIter.close();
        } catch (RippleException e) {
            throw e;
        } catch (Throwable t) {
            throw new RippleException(t);
        }
        buffer.flush();
    }

    private static boolean isBlankNode(final Value v) {
        return v instanceof Resource && (!(v instanceof URI) || ((URI) v).getNamespace().startsWith(Ripple.RANDOM_URN_PREFIX));
    }
}
