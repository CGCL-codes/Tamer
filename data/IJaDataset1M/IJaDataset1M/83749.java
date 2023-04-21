package cc.sprite.data;

/**
 * An interface for table data providers.  Table data is a ordered
 * list of rows that are displayed in user interface elements.  
 * table data is used by WGrid, WSelect, etc...
 * 
 * @author Joe Mayer
 */
public interface WTable extends WObserver {

    /**
   * Get the number of column values in each row.
   * @return The number of column values in each row.
   */
    public int columnCount();

    /**
   * Get an iterator of the element data.
   * @return  An element data iterator.
   */
    public WIterator iterator();

    /**
   * Add another observer to the set of observers watching this 
   * element data.  Adding the same observer a second time has no
   * additional effect.
   * @param observer  The observer to add.
   */
    public void addObserver(WObserver observer);

    /**
   * Remove the specified observer from the list of observers watching
   * this element data. Has no effect if the observer isn't in the list.
   * @param observer  The observer to remove.
   */
    public void removeObserver(WObserver observer);
}
