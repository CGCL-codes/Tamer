package repast.simphony.data.logging.formula;

public class ASTCos extends FunctionNode {

    public ASTCos(int id) {
        super(id);
    }

    public ASTCos(Parser p, int id) {
        super(p, id);
    }

    /**
   * Initializes the func var to the function appropriate for
   * each subclass implementation.
   */
    protected void initFunc() {
        func = factory.createCos();
    }
}
