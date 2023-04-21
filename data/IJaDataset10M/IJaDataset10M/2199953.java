package issrg.pba.rbac.xmlpolicy.ifstatement;

/**
 * This is the class for GT node of the IF-statement. Its functionality is
 * very much determined by OperationNode, which is
 * configured to deliver the following semantics:
 *
 * <p>This operation expects two and only two Terms of any type, and 
 * returns a 
 * value of type Types.BOOLEAN_TYPE.
 * The evaluation result is "true", if and only if there is an interpreter for
 * the required set of Terms, and it evaluates that the first Term is strictly
 * greater than the second Term.
 *
 * <p>It is possible to register the interpreters for specific 
 * combinations of Terms. Use registerInterpreter method to do that. There is no 
 * default 
 * Interpreter. 
 *
 * @see #registerInterpreter
 *
 * @author A.Otenko
 */
public class GtNode extends OperationNode {

    public static final String GT_NODE = "GT";

    /**
   * Call this method to register the node with the XMLPolicyParser.
   */
    public static void register() {
        try {
            issrg.pba.rbac.xmlpolicy.XMLPolicyParser.registerXMLNode(GT_NODE, GtNode.class);
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        }
    }

    /**
   * This method maintains a register of interpreters for specific combinations
   * of Terms. If no special interpreters are registered, the evaluation error
   * occurs. The semantics of the interpreters should make sure that the first
   * Term is strictly greater than the second Term in the array passed to the
   * evaluate method of that Interpreter.
   *
   * @param i - the Interpreter instance that can evaluate inequality expression
   *   for some combination of Terms
   *
   * @see Interpreter#evaluate
   */
    public static void registerInterpreter(Interpreter i) {
        OperationNode.registerInterpreterForNode(GT_NODE, i);
    }

    protected GtNode() {
    }

    /**
   * This constructor builds a GtNode given the XMLPolicyParser and the set of
   * attributes of this XML element. It expects that there are two and only two
   * child nodes.
   *
   * @param p - the XMLPolicyParser that builds this GtNode
   * @param attr - the attributes of this XML element
   */
    public GtNode(issrg.pba.rbac.xmlpolicy.XMLPolicyParser p, org.xml.sax.Attributes attrs) {
        super(GT_NODE, attrs, 2, 2);
    }
}
