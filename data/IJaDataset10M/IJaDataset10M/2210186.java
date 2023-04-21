package edu.mit.osidimpl.repository.shared;

/**
 * RecordIterator provides access to these objects sequentially, one at a time.
 * The purpose of all Iterators is to to offer a way for OSID methods to
 * return multiple values of a common type and not use an array.  Returning an
 * array may not be appropriate if the number of values returned is large or
 * is fetched remotely.  Iterators do not allow access to values by index,
 * rather you must access values in sequence. Similarly, there is no way to go
 * backwards through the sequence unless you place the values in a data
 * structure, such as an array, that allows for access by index.
 * 
 * <p>
 * OSID Version: 2.0
 * </p>
 * 
 * <p>
 * Licensed under the {@link org.osid.SidImplementationLicenseMIT MIT
 * O.K.I&#46; OSID Definition License}.
 * </p>
 */
public class RecordIterator implements org.osid.repository.RecordIterator {

    private java.util.Iterator<org.osid.repository.Record> iterator;

    /**
     *  Constructs a new <code>RecordIterator</code>.
     */
    public RecordIterator(java.util.Collection collection) {
        this.iterator = collection.iterator();
    }

    /**
     *  Return true if there is an additional  Record ; false otherwise.
     *
     *  @return boolean
     *  @throws org.osid.repository.RepositoryException
     */
    public boolean hasNextRecord() throws org.osid.repository.RepositoryException {
        return (this.iterator.hasNext());
    }

    /**
     *  Return the next Record.
     *
     *  @return Record
     *  @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#NO_MORE_ITERATOR_ELEMENTS
     *         NO_MORE_ITERATOR_ELEMENTS}
     */
    public org.osid.repository.Record nextRecord() throws org.osid.repository.RepositoryException {
        try {
            return (this.iterator.next());
        } catch (Exception e) {
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
