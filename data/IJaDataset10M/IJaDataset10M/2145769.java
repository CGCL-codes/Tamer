package seventhsense.data;

/**
 * Class for Folders.
 * Folders can contain further folders or scenarios (or other types).
 * 
 * @author Parallan
 *
 */
public class FolderNode extends AbstractContainerNode {

    /**
	 * Default serial version for serializable
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Creates a simple folder container node
	 * 
	 * @param name name of the node
	 */
    public FolderNode(final String name) {
        super(name);
    }

    @Override
    public INode deepClone() {
        final FolderNode clone = new FolderNode(_name);
        for (INode node : _children) {
            clone.addNode(-1, node.deepClone());
        }
        return clone;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
