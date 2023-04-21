package com.google.template.soy.soytree;

import com.google.template.soy.soytree.SoyNode.BlockNode;
import com.google.template.soy.soytree.SoyNode.StandaloneNode;

/**
 * Abstract implementation of a BlockNode and CommandNode.
 *
 * <p> Important: Do not use outside of Soy code (treat as superpackage-private).
 *
 */
public abstract class AbstractBlockCommandNode extends AbstractParentCommandNode<StandaloneNode> implements BlockNode {

    /**
   * @param id The id for this node.
   * @param commandName The name of the Soy command.
   * @param commandText The command text, or empty string if none.
   */
    public AbstractBlockCommandNode(int id, String commandName, String commandText) {
        super(id, commandName, commandText);
    }

    /**
   * Copy constructor.
   * @param orig The node to copy.
   */
    protected AbstractBlockCommandNode(AbstractBlockCommandNode orig) {
        super(orig);
    }
}
