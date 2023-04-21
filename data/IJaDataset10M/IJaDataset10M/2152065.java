package com.solido.objectkitchen.data;

/**
 * The TableIterator is used to iterate through the data contained in a table.
 * The primary difference between the TableIterator and a normal iterator, is the close method.
 * This method must be called once no further elements are needed, inorder to release all internal resources.
 */
public interface TableIterator {

    /**
     * Closes the iterator and releases any resources associated with it.
     */
    public void close();

    /**
     * Evaluates wether or not this iterator contains any more rows.
     *
     * @return True if there are more rows, false if not.
     */
    public boolean hasNext();

    /**
     * Returns the next row in the Iterator.
     * If no further rows are available, null will be returned.
     *
     * @return The next row in the dataset or null if no more rows are available.
     */
    public DataRow next();
}
