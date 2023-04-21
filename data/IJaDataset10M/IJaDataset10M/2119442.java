package oqube.patchwork.gui;

import java.util.HashSet;
import java.util.Set;

/**
 * A class for storing and displaying information about a package.
 * 
 * @author nono
 * 
 */
public class PackageInfo implements CodeInfo {

    private Set<ClassInfo> classes = new HashSet<ClassInfo>();

    private String name;

    private boolean reference = true;

    public PackageInfo(String pname) {
        this.name = pname;
    }

    public Set<ClassInfo> getClasses() {
        return classes;
    }

    public void setClasses(Set<ClassInfo> classes) {
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addClasses(ClassInfo ci) {
        classes.add(ci);
        if (!ci.isReference()) this.reference = false;
    }

    @Override
    public boolean equals(Object arg0) {
        PackageInfo pi = (PackageInfo) arg0;
        if (pi == null) return false;
        return pi.name == null ? name == null : pi.name.equals(name);
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isReference() {
        return reference;
    }
}
