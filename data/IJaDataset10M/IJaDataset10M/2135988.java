package org.argouml.notation.providers.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.model.Model;
import org.argouml.notation.providers.OperationNotation;

/**
 * The notation for an operation.
 * 
 * @author michiel
 */
public class OperationNotationJava extends OperationNotation {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(OperationNotationJava.class);

    /**
     * The constructor.
     * 
     * @param operation the operation we represent
     */
    public OperationNotationJava(Object operation) {
        super(operation);
    }

    public void parse(Object modelElement, String text) {
        ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED, this, "Parsing in Java not yet supported"));
    }

    public String getParsingHelp() {
        return "Parsing in Java not yet supported";
    }

    public String toString(Object modelElement, Map args) {
        StringBuffer sb = new StringBuffer(80);
        String nameStr = null;
        boolean constructor = false;
        Iterator its = Model.getFacade().getStereotypes(modelElement).iterator();
        String name = "";
        while (its.hasNext()) {
            Object o = its.next();
            name = Model.getFacade().getName(o);
            if ("create".equals(name)) {
                break;
            }
        }
        if ("create".equals(name)) {
            nameStr = Model.getFacade().getName(Model.getFacade().getOwner(modelElement));
            constructor = true;
        } else {
            nameStr = Model.getFacade().getName(modelElement);
        }
        sb.append(generateConcurrency(modelElement));
        sb.append(generateAbstractness(modelElement));
        sb.append(NotationUtilityJava.generateChangeability(modelElement));
        sb.append(NotationUtilityJava.generateScope(modelElement));
        sb.append(NotationUtilityJava.generateVisibility(modelElement));
        Collection returnParams = Model.getCoreHelper().getReturnParameters(modelElement);
        Object rp;
        if (returnParams.size() == 0) {
            rp = null;
        } else {
            rp = returnParams.iterator().next();
        }
        if (returnParams.size() > 1) {
            LOG.warn("Java generator only handles one return parameter" + " - Found " + returnParams.size() + " for " + Model.getFacade().getName(modelElement));
        }
        if (rp != null && !constructor) {
            Object returnType = Model.getFacade().getType(rp);
            if (returnType == null) {
                sb.append("void ");
            } else {
                sb.append(NotationUtilityJava.generateClassifierRef(returnType)).append(' ');
            }
        }
        List params = new ArrayList(Model.getFacade().getParameters(modelElement));
        params.remove(rp);
        sb.append(nameStr).append('(');
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(NotationUtilityJava.generateParameter(params.get(i)));
            }
        }
        sb.append(')');
        Collection c = Model.getFacade().getRaisedSignals(modelElement);
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            boolean first = true;
            while (it.hasNext()) {
                Object signal = it.next();
                if (!Model.getFacade().isAException(signal)) {
                    continue;
                }
                if (first) {
                    sb.append(" throws ");
                } else {
                    sb.append(", ");
                }
                sb.append(Model.getFacade().getName(signal));
                first = false;
            }
        }
        return sb.toString();
    }

    /**
     * Generates "synchronized" keyword for guarded operations.
     * @param op The operation
     * @return String The synchronized keyword if the operation is guarded,
     *                else "".
     */
    private static String generateConcurrency(Object op) {
        if (Model.getFacade().getConcurrency(op) != null && Model.getConcurrencyKind().getGuarded().equals(Model.getFacade().getConcurrency(op))) {
            return "synchronized ";
        }
        return "";
    }

    /**
     * Generate "abstract" keyword for an abstract operation.
     *
     * @param op the operation
     * @return the generated string
     */
    private static String generateAbstractness(Object op) {
        if (Model.getFacade().isAbstract(op)) {
            return "abstract ";
        }
        return "";
    }
}
