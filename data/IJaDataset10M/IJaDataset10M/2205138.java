package org.specrunner.util.comparer.impl;

import org.specrunner.util.comparer.IComparator;

/**
 * Useful comparator to ignore a given cell or row.
 * 
 * @author Thiago Santos
 * 
 */
public class ComparatorTrue implements IComparator {

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean equals(Object expected, Object received) {
        return true;
    }
}
