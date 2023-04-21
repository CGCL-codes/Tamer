package pgr.source;

import java.util.List;
import java.util.ArrayList;

/**
 * Python module object.
 *
 * Date: 2008-09-09
 * Time: 15:44:26
 *
 * @author Pawel Majewski
 */
public final class PModule {

    private String name;

    private PModule parent;

    private final List<PModule> subModules = new ArrayList<PModule>();

    private final List<PSerializable> types = new ArrayList<PSerializable>();

    /**
     * Constructor.
     * @param s module name
     */
    public PModule(final String s) {
        this.name = s;
    }

    public PModule getParent() {
        return parent;
    }

    /**
     * @param parent new parent
     */
    public void setParent(final PModule parent) {
        this.parent = parent;
        parent.addSubModule(this);
    }

    public List<PModule> getSubModules() {
        return subModules;
    }

    /**
     * @param subname sub module name
     * @return sub module object
     */
    public PModule getSubModule(final String subname) {
        for (PModule mod : subModules) {
            if (mod.getName().equals(subname)) {
                return mod;
            }
        }
        return null;
    }

    /**
     * @param subModule next sub module
     */
    public void addSubModule(final PModule subModule) {
        if (getSubModule(subModule.getName()) == null) {
            this.subModules.add(subModule);
        }
    }

    public List<PSerializable> getTypes() {
        return types;
    }

    /**
     * @param type next type in package
     */
    public void addType(final PSerializable type) {
        if (!types.contains(type)) {
            types.add(type);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return full name of package
     */
    public String getFullName() {
        if (parent != null && !parent.getName().equals("root")) {
            return parent.getFullName() + "." + getName();
        } else {
            return getName();
        }
    }
}
