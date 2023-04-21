package koala.dynamicjava.tree;

import koala.dynamicjava.tree.visitor.Visitor;

/**
 * This class represents the long type nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/04/24
 */
public class LongTypeName extends PrimitiveTypeName {

    /**
     * Initializes the type
     */
    public LongTypeName() {
        this(SourceInfo.NONE);
    }

    /**
     * Initializes the type
     */
    public LongTypeName(SourceInfo si) {
        super(long.class, si);
    }

    /**
   * Allows a visitor to traverse the tree
   * @param visitor the visitor to accept
   */
    public <T> T acceptVisitor(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
