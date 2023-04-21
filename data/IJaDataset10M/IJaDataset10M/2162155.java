package repast.simphony.data.logging.formula;

/**
 * Natural log.
 */
public class ASTLn extends FunctionNode {

    public ASTLn(int id) {
        super(id);
    }

    public ASTLn(Parser p, int id) {
        super(p, id);
    }

    /**
   * Initializes the func var to the function appropriate for
   * each subclass implementation.
   */
    protected void initFunc() {
        func = factory.createLog();
    }
}
