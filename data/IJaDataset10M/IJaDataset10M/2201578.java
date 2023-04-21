package fr.gedeon.telnetservice.syntaxtree.ast.impl.bop;

import org.apache.log4j.Logger;
import fr.gedeon.telnetservice.SessionState;
import fr.gedeon.telnetservice.syntaxtree.SyntaxTreeWalkCursor;
import fr.gedeon.telnetservice.syntaxtree.TransitionToken;
import fr.gedeon.telnetservice.syntaxtree.ast.EntityName;
import fr.gedeon.telnetservice.syntaxtree.ast.EntityNamePartType;
import fr.gedeon.telnetservice.syntaxtree.ast.Node;
import fr.gedeon.telnetservice.syntaxtree.ast.NodeNotFoundException;
import fr.gedeon.telnetservice.syntaxtree.ast.TransitionFailedException;
import fr.gedeon.telnetservice.syntaxtree.ast.impl.TransitionImpl;

public class BeanOperationTransitionImpl extends TransitionImpl {

    boolean isCursorLayerPushing;

    public BeanOperationTransitionImpl(EntityName targetNodeName) {
        this(targetNodeName, false);
    }

    public BeanOperationTransitionImpl(EntityName targetNodeName, boolean isCursorLayerPushing) {
        super(targetNodeName.getLocalPart().getValue(), EntityNamePartType.OPERATION, targetNodeName);
        this.isCursorLayerPushing = isCursorLayerPushing;
    }

    public EntityName getTargetNodeName() {
        return this.targetNodeName;
    }

    public boolean pushTargetAsLayer() {
        return this.isCursorLayerPushing;
    }

    public void performTransitionAction(TransitionToken acceptedToken, SessionState sessionState) throws TransitionFailedException {
        Logger logger = Logger.getLogger(this.getClass());
        logger.debug("Setting up session state for this call");
        SyntaxTreeWalkCursor cursor = SyntaxTreeWalkCursor.retrieveFromSessionState(sessionState);
        Node targetNode;
        try {
            targetNode = cursor.getSyntaxTree().lookupNode(cursor.getCurrentNode().getName(), getTargetNodeName());
        } catch (NodeNotFoundException e) {
            throw new RuntimeException("Could not find target node '" + getTargetNodeName() + "' from '" + cursor.getCurrentNode().getName() + "' in syntax tree '" + cursor.getSyntaxTree() + "'", e);
        }
        if (!(targetNode instanceof BeanOperationNode)) {
            throw new RuntimeException("Expecting target node '" + targetNode.getName() + "' to be a BeanOperationNode, got " + targetNode.getClass().getName());
        }
        logger.debug("Creating bean operation");
        BeanOperationInstance boi = BeanOperationInstance.retrieveFromCursor(cursor, true);
        boi.setBeanOperationNode((BeanOperationNode) targetNode);
        logger.debug("Initializing hidden parameters");
        boi.initializeHiddenParameters(sessionState);
    }
}
