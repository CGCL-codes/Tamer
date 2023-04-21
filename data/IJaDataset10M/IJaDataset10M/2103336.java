package org.xmlcml.cml.subset;

import java.util.Collection;
import java.util.Iterator;

/** A convenience class to allow boolean operations between AtomSets or BondSets.
It has to provide the functionality of HashSet */
public interface ToolHashSet extends Cloneable {

    public String getId();

    public void setId(String id);

    public void debug();

    /** ToolHashSet containing elements common to this and h 
	@return ToolHashSet union of this/h
	*/
    public ToolHashSet and(ToolHashSet h);

    /** ToolHashSet containing elements in either this and h 
	@return ToolHashSet 
	*/
    public ToolHashSet or(ToolHashSet h);

    /** ToolHashSet containing elements in this but not h 
	@return ToolHashSet 
	*/
    public ToolHashSet not(ToolHashSet h);

    /** ToolHashSet containing elements in this or h but not both 
	@return ToolHashSet 
	*/
    public ToolHashSet xor(ToolHashSet h);

    /** does this contain an element?
	@param Object o is this in Set?
	@return boolean true if contains Object
	*/
    public boolean contains(Object o);

    /** add element (cf HashSet)
	@param Object o to add
	*/
    public boolean add(Object o);

    /** remove element (cf HashSet)
	@param Object o to remove
	*/
    public boolean remove(Object o);

    /** add Collection (cf HashSet)
	@param Collection c to add
	*/
    public boolean addAll(Collection c);

    /** retain just those elements in Collection (cf HashSet)
	@param Collection c to retain
	*/
    public boolean retainAll(Collection c);

    /** remove just those elements in Collection (cf HashSet)
	@param Collection c to remove
	*/
    public boolean removeAll(Collection c);

    /** shallow copy */
    public Object clone();

    /** create an Iterator
	@return Iterator
	*/
    public Iterator iterator();

    /** size of Set */
    public int size();
}
