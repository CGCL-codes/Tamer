package components.resourceList;

import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingConstants;
import components.JGradientButton;
import data.Resource;

/**
 * Describes an entry in the resource list.
 */
public class ResourceListEntry extends JGradientButton {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private List<ResourceListEntry> children;

    private int levels;

    private Resource resource;

    /**
	 * @param resource the {@link Resource} to represent.
	 * @param levels the number of levels above this. 0 indicates a cell type.
	 */
    public ResourceListEntry(Resource resource, int levels) {
        super(resource.getName(), null, true);
        setHorizontalAlignment(SwingConstants.LEFT);
        this.resource = resource;
        this.levels = levels;
        this.children = new LinkedList<ResourceListEntry>();
    }

    /**
	 * Adds a child to this entry.
	 * 
	 * @param child the child to add.
	 */
    public void addChild(ResourceListEntry child) {
        children.add(child);
    }

    /**
	 * @return the children of this entry.
	 */
    public List<ResourceListEntry> getChildren() {
        return children;
    }

    /**
	 * @return the levels above this entry.
	 */
    public int getLevels() {
        return levels;
    }

    /**
	 * @return the Resource represented by this list entry.
	 */
    public Resource getResource() {
        return resource;
    }
}
