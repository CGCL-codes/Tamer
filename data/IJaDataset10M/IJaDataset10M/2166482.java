package de.ifgi.simcat2.reasoner.rendering;

import java.util.Set;

public class ProxyHandlerException extends Exception {

    private static final long serialVersionUID = -3659994269318559008L;

    private Set<ProxyState> states;

    private ProxyAction action;

    public ProxyHandlerException(Set<ProxyState> currentStates, ProxyAction action) {
        this.states = currentStates;
        this.action = action;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Action " + action.toString() + " doesn't yield valid successors of possible current states: ");
        boolean first = true;
        for (ProxyState s : states) {
            if (first) {
                sb.append("[");
                first = false;
            } else {
                sb.append("], [");
            }
            sb.append(s.getDescription());
        }
        sb.append("]");
        return sb.toString();
    }
}
