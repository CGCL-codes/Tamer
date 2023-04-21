package vidis.util.graphs.graph;

/**
 * A negative capacity exception.
 * 
 * @author Ralf Vandenhouten
 *
 */
public class NegativeCapacityException extends GraphException {

    public NegativeCapacityException() {
        super();
    }

    public NegativeCapacityException(String msg) {
        super(msg);
    }
}
