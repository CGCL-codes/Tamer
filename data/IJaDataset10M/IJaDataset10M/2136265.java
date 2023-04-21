package com.bluemarsh.jswat.ui.nodes;

import com.bluemarsh.jswat.core.util.Names;
import com.sun.jdi.ClassLoaderReference;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.NbBundle;

/**
 * Represents a package in the classes view.
 *
 * @author  Nathan Fiedler
 */
public class PackageNode extends BaseNode {

    /** The class loader reference we represent (null = boot). */
    private ClassLoaderReference loader;

    /** The full name of the package. */
    private String longName;

    /** The short name of the package. */
    private String shortName;

    /**
     * Constructs a PackageNode to represent a package.
     *
     * @param  kids    children of this node.
     * @param  name    full name of the package.
     * @param  loader  the class loader (may be null to represent the
     *                 boot classloader).
     */
    public PackageNode(Children kids, String name, ClassLoaderReference loader) {
        super(kids);
        this.longName = name;
        this.loader = loader;
        shortName = Names.getShortClassName(name);
        setIconBaseWithExtension("com/bluemarsh/jswat/ui/resources/nodes/Folder.gif");
    }

    /**
     * Creates a node property of the given key (same as the column keys).
     *
     * @param  key   property name (same as matching column).
     * @param  value  display value.
     * @return  new property.
     */
    private Node.Property createProperty(String key, String value) {
        String desc = NbBundle.getMessage(PackageNode.class, "CTL_ClassProperty_Desc_" + key);
        String name = NbBundle.getMessage(PackageNode.class, "CTL_ClassProperty_Name_" + key);
        return new ReadOnlyProperty(key, String.class, name, desc, value);
    }

    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Set set = sheet.get(Sheet.PROPERTIES);
        set.put(createProperty(PROP_NAME, longName));
        return sheet;
    }

    public String getDisplayName() {
        return shortName;
    }

    /**
     * Gets the full name of the package.
     *
     * @return  full package name.
     */
    public String getFullName() {
        return longName;
    }

    /**
     * Returns the ClassLoaderReference this node belongs to.
     *
     * @return  class loader.
     */
    public ClassLoaderReference getLoader() {
        return loader;
    }

    public String getName() {
        return shortName;
    }
}
