package org.dbe.kb.qee.queryanalyzer;

/**
 * <p>Title: Syntax Tree Implementation of BooleanLiteralNode.
 * <p><code>BooleanLiteralNode</code> is a <code>SyntaxTreeNode</code> that holds a
 * boolean value.
 * <p>Copyright: 2004
 * <p>Company: MUSIC/TUC
 *
 * @author George Kotopoulos
 * @version 1.0
 */
public class BooleanLiteralNode extends LiteralNode {

    /**
     * Constructs a Boolean Tree Node with <code>Boolean</code> value
     * @param value the Boolean value
     */
    public BooleanLiteralNode(Boolean value) {
        super(value);
    }

    /**
   * Constructs a Boolean Tree Node with <code>boolean</code> value
  * @param value the boolean value
  */
    public BooleanLiteralNode(boolean value) {
        super(new Boolean(value));
    }

    /**
   * Gets the value of this node.
   * @return Object of type <code>Boolean</code>
   */
    public Object getValue() {
        return userObject;
    }

    /**
   * Gets the value of this node.
   * @return the boolean value
   */
    public boolean getBooleanValue() {
        return ((Boolean) userObject).booleanValue();
    }
}
