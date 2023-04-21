package awilkins.eclipse.jdt.properties;

/**
 *
 *         Style - Code Templates
 */
public class ConstructorAccessor extends AbstractMethodAccessor {

    /**
   *
   */
    public ConstructorAccessor(ConstructorPropertyNode node) {
        super(node);
    }

    /**
   * @return Assignments the propertyNode.
   */
    public ConstructorPropertyNode getConstructorNode() {
        return (ConstructorPropertyNode) getPropertyNode();
    }
}
