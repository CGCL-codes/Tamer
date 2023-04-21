package tufts.oki.dr.fedora;

/**
    TypeIterator returns a set, one at a time.  The purpose of all Iterators is to to offer a way for SID methods to return multiple values of a common type and not use an array.  Returning an array may not be appropriate if the number of values returned is large or is fetched remotely.  Iterators do not allow access to values by index, rather you must access values in sequence. Similarly, there is no way to go backwards through the sequence unless you place the values in a data structure, such as an array, that allows for access by index. <p>Licensed under the {@link osid.SidLicense MIT O.K.I SID Definition License}.<p>@version $Revision: 1.8 $ / $Date: 2010-02-03 19:21:24 $
    */
public class TypeIterator implements osid.shared.TypeIterator {

    private java.util.Vector vector = new java.util.Vector();

    private int i = 0;

    public TypeIterator(java.util.Vector vector) throws osid.shared.SharedException {
        this.vector = vector;
    }

    /**
        Return true if there are additional  Types ; false otherwise.
        @return boolean
        @throws An exception with one of the following messages defined in osid.dr.DigitalRepositoryException may be thrown:  OPERATION_FAILED
        */
    public boolean hasNext() throws osid.shared.SharedException {
        return i < vector.size();
    }

    /**
        Return the next Type
        @return Type
        @throws An exception with one of the following messages defined in osid.dr.DigitalRepositoryException may be thrown:  OPERATION_FAILED, NO_MORE_ITERATOR_ELEMENTS
        */
    public osid.shared.Type next() throws osid.shared.SharedException {
        if (i < vector.size()) {
            return (osid.shared.Type) vector.elementAt(i++);
        } else {
            throw new osid.shared.SharedException(osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
