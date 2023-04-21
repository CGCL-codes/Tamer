package issrg.pba.rbac.xmlpolicy.ifstatement;

/**
 * This is the class for And node of the IF-statement. Its functionality is
 * very much determined by OperationNode and AndInterpreter, which are 
 * configured to deliver the following semantics:
 *
 * <p>This operation expects one or more Terms, each returning a 
 * Types.BOOLEAN_TYPE type, and returns a value of type Types.BOOLEAN_TYPE.
 * The evaluation result is "true", if and only if all Terms of the AndNode
 * evaluate to boolean "true".
 *
 * @author A.Otenko
 */
public class AndNode extends OperationNode {

    public static final String AND_NODE = "AND";

    /**
   * Call this method to register the node with the XMLPolicyParser. This 
   * method also registers the default AndInterpreter.
   */
    public static void register() {
        try {
            issrg.pba.rbac.xmlpolicy.XMLPolicyParser.registerXMLNode(AND_NODE, AndNode.class);
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        }
        issrg.pba.rbac.xmlpolicy.ifstatement.OperationNode.registerInterpreterForNode(AND_NODE, new AndInterpreter());
    }

    protected AndNode() {
    }

    /**
   * This constructor builds an AndNode, given a XMLPolicyParser and the
   * set of attributes of this XML element.
   *
   * @param p - the XMLPolicyParser that builds this AndNode
   * @param attrs - the attributes of this XML element
   */
    public AndNode(issrg.pba.rbac.xmlpolicy.XMLPolicyParser p, org.xml.sax.Attributes attrs) {
        super(AND_NODE, attrs, -1, 1);
    }
}

/**
 * This is the default AndInterpreter that can evaluate the expression 
 * consisting of any number of Terms of type Types.BOOLEAN_TYPE. It returns
 * a boolean "true" if and only if all of the Terms evaluate
 * to boolean "true".
 */
class AndInterpreter implements Interpreter {

    public AndInterpreter() {
    }

    /**
   * This method returns Types.BOOLEAN_TYPE if the array length is non-zero, and
   * each Term is of type Types.BOOLEAN_TYPE.
   *
   * @param t - the array of Terms; must have non-zero length
   *
   * @return Types.BOOLEAN_TYPE if the array length is non-zero and all Terms
   *   in it return Types.BOOLEAN_TYPE on call to getType()
   */
    public String canEvaluate(Term[] t) {
        if (t == null || t.length == 0) {
            return null;
        }
        for (int i = 0; i < t.length; i++) {
            if (t[i].getType().intern() != Types.BOOLEAN_TYPE) {
                return null;
            }
        }
        return Types.BOOLEAN_TYPE;
    }

    /**
   * This method evaluates the given expression, so that a boolean "true" is 
   * returned if all of the Terms evaluate to boolean "true". Note
   * that not all of the Terms may be evaluated as the result (once a Term that
   * evaluates to "false" is found, evaluation of any further Terms is 
   * pointless).
   *
   * @param env - the Environment of evaluation
   * @param t - the array of Terms
   *
   * @return java.lang.Boolean object with the result of evaluation; it is
   *   set to "false" if there is at least one Term that evaluates to "false";
   *   otherwise it is set to "true"
   */
    public Object evaluate(Environment env, Term[] t) throws EvaluationException {
        if (canEvaluate(t) == null) {
            throw new EvaluationException("Cannot evaluate expression");
        }
        Object result = null;
        for (int i = 0; i < t.length; i++) {
            result = t[i].evaluate(env);
            if (!((Boolean) result).booleanValue()) {
                break;
            }
        }
        return result;
    }
}
