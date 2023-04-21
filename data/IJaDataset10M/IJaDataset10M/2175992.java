package net.wotonomy.control;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.wotonomy.foundation.NSMutableArray;
import net.wotonomy.foundation.NSRange;

/**
* A package class that extends NSMutableArray but makes use
* of the fact that wotonomy's implementation extends ArrayList
* to intercept insertions and deletion and register and 
* unregister objects for change notifications as appropriate.
* Since we can't be sure of ArrayList's implementation, we're
* forced to override each and every add and remove method,
* some of which probably call each other.  However, 
* EOObserverCenter will only register us once per object.
*/
class ObservableArray extends NSMutableArray {

    EOObserving observer;

    ObservableArray(EOObserving anObserver) {
        observer = anObserver;
    }

    /**
	* Removes the last object from the array.
	*/
    public void removeLastObject() {
        remove(count() - 1);
    }

    /**
	* Removes the object at the specified index.
	*/
    public void removeObjectAtIndex(int index) {
        remove(index);
    }

    /**
	* Adds all objects in the specified collection.
	*/
    public void addObjectsFromArray(Collection aCollection) {
        addAll(aCollection);
    }

    /**
	* Removes all objects from the array.
	*/
    public void removeAllObjects() {
        clear();
    }

    /**
	* Removes all objects equivalent to the specified object
	* within the range of specified indices.
	*/
    public void removeObject(Object anObject, NSRange aRange) {
        if ((anObject == null) || (aRange == null)) return;
        int loc = aRange.location();
        int max = aRange.maxRange();
        for (int i = loc; i < max; i++) {
            if (anObject.equals(get(i))) {
                remove(i);
                i = i - 1;
                max = max - 1;
            }
        }
    }

    /**
	* Removes all instances of the specified object within the 
	* range of specified indices, comparing by reference.
	*/
    public void removeIdenticalObject(Object anObject, NSRange aRange) {
        if ((anObject == null) || (aRange == null)) return;
        int loc = aRange.location();
        int max = aRange.maxRange();
        for (int i = loc; i < max; i++) {
            if (anObject == get(i)) {
                remove(i);
                i = i - 1;
                max = max - 1;
            }
        }
    }

    /**
	* Removes all objects in the specified collection from the array.
	*/
    public void removeObjectsInArray(Collection aCollection) {
        removeAll(aCollection);
    }

    /**
	* Removes all objects in the indices within the specified range 
	* from the array.
	*/
    public void removeObjectsInRange(NSRange aRange) {
        if (aRange == null) return;
        for (int i = 0; i < aRange.length(); i++) {
            remove(aRange.location());
        }
    }

    /**
	* Replaces objects in the current range with objects from
	* the specified range of the specified array.  If currentRange
	* is larger than otherRange, the extra objects are removed.
	* If otherRange is larger than currentRange, the extra objects
	* are added.
	*/
    public void replaceObjectsInRange(NSRange currentRange, List otherArray, NSRange otherRange) {
        if ((currentRange == null) || (otherArray == null) || (otherRange == null)) return;
        if (otherRange.maxRange() > otherArray.size()) {
            int loc = Math.min(otherRange.location(), otherArray.size() - 1);
            otherRange = new NSRange(loc, otherArray.size() - loc);
        }
        Object o;
        List subList = subList(currentRange.location(), currentRange.maxRange());
        int otherIndex = otherRange.location();
        for (int i = 0; i < subList.size(); i++) {
            if (otherIndex < otherRange.maxRange()) {
                subList.set(i, otherArray.get(otherIndex));
            } else {
                subList.remove(i);
                i--;
            }
            otherIndex++;
        }
        for (int i = otherIndex; i < otherRange.maxRange(); i++) {
            add(otherArray.get(i));
        }
    }

    /**
	* Clears the current array and then populates it with the
	* contents of the specified collection.
	*/
    public void setArray(Collection aCollection) {
        clear();
        addAll(aCollection);
    }

    /**
	* Removes all objects equivalent to the specified object. 
	*/
    public void removeObject(Object anObject) {
        remove(anObject);
    }

    /**
	* Removes all occurences of the specified object,
	* comparing by reference.
	*/
    public void removeIdenticalObject(Object anObject) {
        EOObserverCenter.removeObserver(observer, anObject);
        super.removeIdenticalObject(anObject);
    }

    /**
	* Inserts the specified object into this array at the
	* specified index.
	*/
    public void insertObjectAtIndex(Object anObject, int anIndex) {
        add(anIndex, anObject);
    }

    /**
	* Replaces the object at the specified index with the 
	* specified object.
	*/
    public void replaceObjectAtIndex(int anIndex, Object anObject) {
        set(anIndex, anObject);
    }

    /**
	* Adds the specified object to the end of this array.
	*/
    public void addObject(Object anObject) {
        add(anObject);
    }

    public void add(int index, Object element) {
        EOObserverCenter.addObserver(observer, element);
        super.add(index, element);
    }

    public boolean add(Object o) {
        EOObserverCenter.addObserver(observer, o);
        return super.add(o);
    }

    public boolean addAll(Collection coll) {
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            EOObserverCenter.addObserver(observer, it.next());
        }
        return super.addAll(coll);
    }

    public boolean addAll(int index, Collection c) {
        Iterator it = c.iterator();
        while (it.hasNext()) {
            EOObserverCenter.addObserver(observer, it.next());
        }
        return super.addAll(index, c);
    }

    public void clear() {
        Iterator it = iterator();
        while (it.hasNext()) {
            EOObserverCenter.removeObserver(observer, it.next());
        }
        super.clear();
    }

    public Object remove(int index) {
        EOObserverCenter.removeObserver(observer, get(index));
        return super.remove(index);
    }

    public boolean remove(Object o) {
        EOObserverCenter.removeObserver(observer, o);
        return super.remove(o);
    }

    public boolean removeAll(Collection coll) {
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            EOObserverCenter.removeObserver(observer, it.next());
        }
        return super.removeAll(coll);
    }

    public boolean retainAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    public Object set(int index, Object element) {
        EOObserverCenter.removeObserver(observer, get(index));
        EOObserverCenter.addObserver(observer, element);
        return super.set(index, element);
    }
}
