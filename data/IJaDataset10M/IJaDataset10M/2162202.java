package org.mitre.midiki.impl.mitre;

import org.mitre.midiki.logic.Bindings;
import org.mitre.midiki.state.*;
import java.util.*;

/**
 * Provides the executable code for a <code>Method</code>.
 *
 * @author <a href="mailto:cburke@mitre.org">Carl Burke</a>
 * @version 1.0
 * @since 1.0
 */
public class MethodHandlerProxy implements MethodHandler {

    protected Contract.Method method;

    protected ImmutableCellImpl cell;

    protected CellClient client;

    public MethodHandlerProxy(String name, ImmutableCellImpl ic, CellClient cl) {
        Iterator mit = ic.getContract().methods();
        while (mit.hasNext()) {
            Contract.Method m = (Contract.Method) mit.next();
            if (name.equals(m.name())) {
                method = m;
                break;
            }
        }
        cell = ic;
        client = cl;
    }

    /**
     * Executes the query method specified by this handler.
     * The specified arguments are passed to the method, and may be modified
     * by variable assignments within the current set of bindings. The
     * method may bind additional variables.
     *
     * @param arguments a <code>Collection</code> value
     * @param bindings a <code>Bindings</code> value
     * @return <code>true</code> if the method succeeded
     */
    public boolean invoke(Collection arguments, Bindings bindings) {
        String tag = "method$" + cell.getContract().name() + "$" + method.name();
        Collection results = new LinkedList();
        results.addAll(bindings.marshalAll());
        LinkedList parameters = new LinkedList(arguments);
        if (client.mediatedBy.usesMidikiProtocol()) {
            if ((cell == null) || (cell.rootInstance == null)) {
                parameters.addFirst(null);
            } else {
                parameters.addFirst(cell.rootInstance.instanceId);
            }
        }
        boolean success = client.mediatedBy.useService(tag, parameters, results);
        bindings.unmarshalLatest(results);
        return success;
    }
}
