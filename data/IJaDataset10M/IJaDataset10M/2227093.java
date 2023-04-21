package org.enclojure.debug.watchesfiltering;

import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.api.debugger.jpda.InvalidExpressionException;

/**
 * Node model for JSP EL watches.
 *
 * @author Maros Sandor
 */
public class CljWatchesNodeModel implements NodeModel {

    private static final String ICON_BASE = "org/netbeans/modules/debugger/resources/watchesView/Watch";

    public String getDisplayName(Object node) throws UnknownTypeException {
        if (!(node instanceof CljElWatch)) throw new UnknownTypeException(node);
        CljElWatch watch = (CljElWatch) node;
        return watch.getExpression();
    }

    public String getIconBase(Object node) throws UnknownTypeException {
        if (!(node instanceof CljElWatch)) throw new UnknownTypeException(node);
        return ICON_BASE;
    }

    public String getShortDescription(Object node) throws UnknownTypeException {
        if (!(node instanceof CljElWatch)) throw new UnknownTypeException(node);
        CljElWatch watch = (CljElWatch) node;
        String t = watch.getType();
        String e = watch.getExceptionDescription();
        if (e != null) {
            return watch.getExpression() + " = >" + e + "<";
        }
        if (t == null) {
            return watch.getExpression() + " = " + watch.getValue();
        } else {
            try {
                return watch.getExpression() + " = (" + watch.getType() + ") " + watch.getToStringValue();
            } catch (InvalidExpressionException ex) {
                return ex.getLocalizedMessage();
            }
        }
    }

    public void addModelListener(ModelListener l) {
    }

    public void removeModelListener(ModelListener l) {
    }
}
