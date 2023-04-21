package repast.simphony.data.logging.formula;

public class ASTSum extends FunctionNode {

    public ASTSum(int id) {
        super(id);
    }

    public ASTSum(Parser p, int id) {
        super(p, id);
    }

    /**
   * Initializes the func var to the function appropriate for
   * each subclass implementation.
   */
    protected void initFunc() {
        func = factory.createSum();
    }
}
