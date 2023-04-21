package net.sf.saxon.expr.sort;

/**
 * A Sortable is an object that can be sorted using the QuickSort method.
 *
 * @author Michael H. Kay
 *
 */
public interface Sortable {

    /**
    * Compare two objects within this Sortable, identified by their position.
    * @return <0 if obj[a]<obj[b], 0 if obj[a]=obj[b], >0 if obj[a]>obj[b]
    */
    public int compare(int a, int b);

    /**
    * Swap two objects within this Sortable, identified by their position.
    */
    public void swap(int a, int b);
}
