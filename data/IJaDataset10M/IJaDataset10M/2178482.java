package org.usajusaj.jtex;

import java.util.ArrayList;
import java.util.Iterator;

public class TeXPackageCollectionImpl implements ITPackageCollection {

    private ArrayList<ITPackage> packages;

    public TeXPackageCollectionImpl(ITPackage... packages) {
        this.packages = new ArrayList<ITPackage>();
        for (ITPackage p : packages) this.packages.add(p);
    }

    @Override
    public ITPackageCollection addPackage(ITPackage p) {
        for (ITPackage pp : packages) if (pp.equals(p)) return this;
        packages.add(p);
        return this;
    }

    @Override
    public void clearPackages() {
        packages.clear();
    }

    @Override
    public ITPackage getPackage(String p) {
        for (ITPackage pp : packages) if (pp.getName().equals(p)) return pp;
        return null;
    }

    @Override
    public void removePackage(ITPackage p) {
        packages.remove(p);
    }

    @Override
    public void removePackage(String p) {
        ITPackage pp = getPackage(p);
        if (pp != null) packages.remove(pp);
    }

    @Override
    public Iterator<ITPackage> iterator() {
        return packages.iterator();
    }
}
