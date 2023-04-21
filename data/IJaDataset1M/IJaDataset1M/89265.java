package org.jpox.samples.abstractclasses.self;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ComplexAssembly extends Assembly {

    private Set subAssemblies = new HashSet();

    public ComplexAssembly() {
    }

    public ComplexAssembly(int levelNo) {
        for (int i = 0; i < 3; i++) {
            if (levelNo < 4) {
                subAssemblies.add(new ComplexAssembly(levelNo + 1));
            }
        }
    }

    public void clearSubAssemblies() {
        for (Iterator i = subAssemblies.iterator(); i.hasNext(); ) {
            ComplexAssembly ap = (ComplexAssembly) i.next();
            ap.clearSubAssemblies();
        }
        subAssemblies.clear();
    }

    public int traverse(int op) {
        int count = 0;
        for (Iterator i = subAssemblies.iterator(); i.hasNext(); ) {
            Assembly ap = (Assembly) i.next();
            count += ap.traverse(op);
        }
        return count;
    }
}
