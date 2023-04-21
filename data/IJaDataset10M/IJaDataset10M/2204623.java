package com.ibm.wala.refactoring.buckets;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DependenceMap extends HashMap<Integer, HashSet<Integer>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public boolean dependenceAdd(Integer key, Collection<Integer> values) {
        if (values == null) return false;
        HashSet<Integer> c = this.get(key);
        if (c == null) {
            HashSet<Integer> hs = new HashSet<Integer>(values);
            this.put(key, hs);
            return true;
        } else {
            boolean hasChanges = !c.containsAll(values);
            if (hasChanges) c.addAll(values);
            return hasChanges;
        }
    }

    public boolean dependenceAdd(Integer key, Integer value) {
        HashSet<Integer> values = this.get(key);
        if (values == null) {
            values = new HashSet<Integer>();
            values.add(value);
            this.put(key, values);
            return true;
        }
        if (!values.contains(value)) {
            values.add(value);
            return true;
        }
        return false;
    }

    public void printDependences() {
        for (Integer line : this.keySet()) {
            System.out.println("line number " + (line) + " :");
            if (this.get(line) != null) {
                for (int i : this.get(line)) {
                    System.out.print(i + ", ");
                }
                System.out.println();
            }
        }
    }

    public DependenceMap invert() {
        DependenceMap dependencesMap = new DependenceMap();
        for (Integer from : this.keySet()) {
            for (Integer to : this.get(from)) {
                dependencesMap.dependenceAdd(to, from);
            }
        }
        return dependencesMap;
    }

    public int getNumOfDependences() {
        int result = 0;
        for (Integer key : this.keySet()) {
            result += this.get(key).size();
        }
        return result;
    }

    public DependenceMap deepClone() {
        DependenceMap map = new DependenceMap();
        for (Integer key : keySet()) {
            HashSet<Integer> vals = this.get(key);
            map.put(key, (HashSet<Integer>) vals.clone());
        }
        return map;
    }
}
