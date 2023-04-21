package operators;

public class IsIn implements IOperator {

    private static IOperator instance = null;

    private IsIn() {
    }

    public static IOperator getInstance() {
        if (instance == null) instance = new IsIn();
        return instance;
    }

    public String toString() {
        return "=in";
    }
}
