package org.siberia.base.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * Implementation of a sorted list based on an ArrayList
 *  It allow to add a new Item by dichotomie
 *
 * @author alexis
 */
public class SortedList<E extends Comparable> extends ArrayList<E> {

    /** Creates a new instance of SortedList */
    public SortedList() {
    }

    public boolean add(E e) {
        boolean result = false;
        E processedItem = null;
        if (e != null) {
            processedItem = e;
            {
                if (!this.contains(processedItem)) {
                    int indexTmp = -1;
                    if (this.size() == 0) {
                        indexTmp = 0;
                    } else {
                        int size = this.size();
                        if (processedItem.compareTo(this.get(size - 1)) > 0) {
                            indexTmp = size;
                        } else if (processedItem.compareTo(this.get(0)) <= 0) {
                            indexTmp = 0;
                        } else {
                            int startIndex = 0;
                            int endIndex = size - 1;
                            while (indexTmp == -1) {
                                int dicIndex = (endIndex - startIndex) / 2 + startIndex;
                                E itemAtDicIndex = this.get(dicIndex);
                                int compareResult = processedItem.compareTo(itemAtDicIndex);
                                if (compareResult > 0) {
                                    if (dicIndex == startIndex) {
                                        startIndex = dicIndex + 1;
                                    } else {
                                        startIndex = dicIndex;
                                    }
                                } else if (compareResult == 0) {
                                    indexTmp = dicIndex;
                                } else {
                                    if (dicIndex == endIndex) {
                                        endIndex = dicIndex - 1;
                                    } else {
                                        endIndex = dicIndex;
                                    }
                                }
                                if (startIndex == endIndex) {
                                    indexTmp = startIndex;
                                }
                            }
                        }
                    }
                    final int index = indexTmp;
                    result = true;
                    this.add(index, processedItem);
                }
            }
        }
        return result;
    }

    /** method which determine if the given collection of item
     *	should be added one by one to benefit from dichotomy.
     *	if it returns false, then the element will be added and after the collection will be sorted
     *	@param c a Collection to add
     *	@return true to add element one by one by dichotomy
     */
    protected boolean useDichotomyToAdd(Collection<? extends E> c) {
        boolean result = true;
        if (c != null) {
            if (this.size() <= 5) {
                result = false;
            } else {
                if (c.size() > this.size()) {
                    result = false;
                }
            }
        }
        return result;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean result = true;
        if (this.useDichotomyToAdd(c)) {
            Collection<E> added = new ArrayList<E>(c.size());
            Iterator<? extends E> it = c.iterator();
            while (it.hasNext()) {
                E current = it.next();
                if (this.add(current)) {
                    added.add(current);
                } else {
                    result = false;
                    Iterator<? extends E> ite = added.iterator();
                    while (ite.hasNext()) {
                        E toRemove = ite.next();
                        this.remove(toRemove);
                    }
                    break;
                }
            }
        } else {
            this.addAll(c);
            Collections.sort(this);
        }
        return result;
    }
}
