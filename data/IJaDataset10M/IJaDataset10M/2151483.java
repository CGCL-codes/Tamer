package de.knowwe.core.utils;

import java.util.Comparator;
import de.knowwe.core.kdom.Type;

/**
 * Used in TypeUtils to sort Types lexicographical.
 * 
 * @author Johannes Dienst
 * 
 */
public class TypeComparator implements Comparator<Type> {

    @Override
    public int compare(Type o1, Type o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
