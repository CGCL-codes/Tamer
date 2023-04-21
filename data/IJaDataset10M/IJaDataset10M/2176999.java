package astcentric.structure.basic;

/**
 * Node collection wrapper which filters the wrapped collection during 
 * traversing.  
 *
 */
public class FilteredNodeCollection implements NodeCollection {

    private final NodeCollection _collection;

    private final NodeValidator _filter;

    private final ValidationContext _validationContext;

    /**
   * Creates an instance for the specified collection and an optional filter
   * and validation context. 
   */
    public FilteredNodeCollection(NodeCollection collection, NodeValidator filter, ValidationContext validationContext) {
        if (collection == null) {
            throw new IllegalArgumentException("Unspecified node collection.");
        }
        _collection = collection;
        _filter = filter;
        _validationContext = validationContext;
    }

    public boolean contains(Node node) {
        return _collection.contains(node) && filterPassed(node);
    }

    public void traverseNodes(final NodeHandler handler) {
        _collection.traverseNodes(new NodeHandler() {

            public boolean handle(Node node) {
                if (filterPassed(node)) {
                    return handler.handle(node);
                }
                return false;
            }
        });
    }

    private boolean filterPassed(Node node) {
        return _filter == null || _filter.validate(node, _validationContext).successful();
    }
}
