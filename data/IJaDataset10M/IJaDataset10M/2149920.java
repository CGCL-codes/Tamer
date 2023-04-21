package fr.gedeon.telnetservice.syntaxtree.operations;

import java.util.TreeMap;
import org.apache.log4j.Logger;
import fr.gedeon.telnetservice.DisplayUtilitiesVT100;
import fr.gedeon.telnetservice.SessionState;
import fr.gedeon.telnetservice.syntaxtree.SyntaxTreeWalkCursor;
import fr.gedeon.telnetservice.syntaxtree.SyntaxTreeWalkDisplayHandlerImpl;
import fr.gedeon.telnetservice.syntaxtree.TransitionToken;
import fr.gedeon.telnetservice.syntaxtree.ast.EntityNamePart;
import fr.gedeon.telnetservice.syntaxtree.ast.Node;
import fr.gedeon.telnetservice.syntaxtree.ast.Transition;
import fr.gedeon.telnetservice.syntaxtree.ast.impl.EntityNameImpl;
import fr.gedeon.telnetservice.syntaxtree.ast.impl.TransitionImpl;

public class HelpOperationTransition extends TransitionImpl implements Transition {

    private static final String QUESTION = "?";

    private static final String HELP_COMMAND = "help";

    public HelpOperationTransition() {
        super(new EntityNameImpl(EntityNameImpl.RELATIVE_NAME_BASE, HELP_COMMAND, EntityNamePart.TYPE_OPERATION));
        setDescription("Displays this help listing. Also use <filter>? to limit help " + "items to those starting with <filter>");
    }

    public TransitionToken acceptToken(TransitionToken token, SessionState sessionState) {
        String tokenStr = token.getToken().toString();
        if (tokenStr.endsWith(QUESTION) || tokenStr.equalsIgnoreCase(HELP_COMMAND)) {
            Logger.getLogger(this.getClass()).debug(token + " accepted");
            return token;
        } else {
            return null;
        }
    }

    public Node performTransitionAction(TransitionToken acceptedToken, SessionState sessionState) {
        SyntaxTreeWalkCursor cursor = SyntaxTreeWalkCursor.retrieveFromSessionState(sessionState);
        String filterBy = null;
        String tokenStr = acceptedToken.getToken().toString();
        if (tokenStr.endsWith(QUESTION)) {
            filterBy = tokenStr.substring(0, tokenStr.length() - QUESTION.length());
        } else {
            filterBy = "";
        }
        boolean foundOneTransition = false;
        TreeMap<String, Transition> localTransitions = new TreeMap<String, Transition>();
        for (Transition tr : cursor.getCurrentNode().getOutTransitions()) {
            if (tr.isEnabled(sessionState)) {
                String name = tr.getName().getLocalPart().getValue();
                if (name.startsWith(filterBy)) {
                    localTransitions.put(tr.getName().getLocalPart().getValue(), tr);
                    foundOneTransition = true;
                }
            }
        }
        TreeMap<String, Transition> globalTransitions = new TreeMap<String, Transition>();
        for (Transition tr : cursor.getSyntaxTree().getGlobalTransitions()) {
            if (tr.isEnabled(sessionState)) {
                String name = tr.getName().getLocalPart().getValue();
                if (name.startsWith(filterBy)) {
                    globalTransitions.put(tr.getName().getLocalPart().getValue(), tr);
                    foundOneTransition = true;
                }
            }
        }
        SyntaxTreeWalkDisplayHandlerImpl syntaxTreeWalkDisplayHandler = null;
        if (sessionState.getDisplayHandler() instanceof SyntaxTreeWalkDisplayHandlerImpl) {
            syntaxTreeWalkDisplayHandler = (SyntaxTreeWalkDisplayHandlerImpl) sessionState.getDisplayHandler();
        }
        StringBuffer buf = new StringBuffer();
        if (foundOneTransition) {
            for (Transition tr : localTransitions.values()) {
                buf.append("    ");
                if (syntaxTreeWalkDisplayHandler != null) {
                    syntaxTreeWalkDisplayHandler.appendColoredPart(buf, tr.getName().getLocalPart().getValue(), tr.getName().getLocalPart().getType());
                } else {
                    buf.append(tr.getName().getLocalPart().getValue());
                }
                buf.append(" : ");
                String description = tr.getDescription();
                if (description != null) {
                    buf.append(description);
                } else if (tr.getName().getLocalPart().getType().equals(EntityNamePart.TYPE_CONTEXT)) {
                    buf.append("Go to sub-context " + tr.getName().getLocalPart().getValue());
                } else if (tr.getName().getLocalPart().getType().equals(EntityNamePart.TYPE_OPERATION)) {
                    buf.append("Go to operation " + tr.getName().getLocalPart().getValue());
                }
                buf.append(sessionState.getDisplayHandler().getEOLN());
            }
            if (localTransitions.size() > 0) {
                buf.append(sessionState.getDisplayHandler().getEOLN());
            }
            for (Transition tr : globalTransitions.values()) {
                buf.append("    ");
                if (syntaxTreeWalkDisplayHandler != null) {
                    syntaxTreeWalkDisplayHandler.appendColoredPart(buf, tr.getName().getLocalPart().getValue(), tr.getName().getLocalPart().getType());
                } else {
                    buf.append(tr.getName().getLocalPart().getValue());
                }
                buf.append(" : ");
                String description = tr.getDescription();
                if (description != null) {
                    buf.append(description);
                } else if (tr.getName().getLocalPart().getType().equals(EntityNamePart.TYPE_CONTEXT)) {
                    buf.append("Go to sub-context " + tr.getName().getLocalPart().getValue());
                } else if (tr.getName().getLocalPart().getType().equals(EntityNamePart.TYPE_OPERATION)) {
                    buf.append("Go to operation " + tr.getName().getLocalPart().getValue());
                }
                buf.append(sessionState.getDisplayHandler().getEOLN());
            }
        } else {
            DisplayUtilitiesVT100.setAttrs(buf, new byte[][] { DisplayUtilitiesVT100.RESET, DisplayUtilitiesVT100.ATTRS_FORE_MAGENTA, DisplayUtilitiesVT100.DONE });
            buf.append("No operations available (if you used a filter, consider losening the prefix)");
            DisplayUtilitiesVT100.setAttrs(buf, DisplayUtilitiesVT100.ATTRS_DEFAULT);
            buf.append(sessionState.getDisplayHandler().getEOLN());
        }
        sessionState.getSessionCallback().writeln(buf.toString());
        return SyntaxTreeWalkCursor.retrieveFromSessionState(sessionState).getCurrentNode();
    }

    public Node getTargetNode() {
        return null;
    }

    public String toString() {
        return getName().toString();
    }
}
