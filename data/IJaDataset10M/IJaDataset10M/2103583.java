package darwin.nodeFilter;

/**
 * Accept nodes that have a constant value
 * @author Kevin Dolan
 */
public class Constant extends Filter {

    public boolean accept(Filterable node) {
        return node.isMutable();
    }
}
