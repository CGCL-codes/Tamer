package takatuka.optimizer.deadCodeRemoval.dataObj.xml;

/**
 * 
 * @author aslam
 */
public class PackageXML {

    private String name = null;

    private boolean includeSubPackages = false;

    public PackageXML(String name, boolean includeSubPackages) {
        this.name = name;
        this.includeSubPackages = includeSubPackages;
    }

    public String getName() {
        return name;
    }

    public boolean isIncludeSubPackages() {
        return includeSubPackages;
    }

    @Override
    public String toString() {
        return name + ", " + includeSubPackages;
    }
}
