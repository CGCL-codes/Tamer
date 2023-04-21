package repast.simphony.data.logging.formula;

public class ASTCosd extends FunctionNode {

    public ASTCosd(int id) {
        super(id);
    }

    public ASTCosd(Parser p, int id) {
        super(p, id);
    }

    /**
   * Initializes the func var to the function appropriate for
   * each subclass implementation.
   */
    protected void initFunc() {
        func = factory.createCosD();
    }
}
