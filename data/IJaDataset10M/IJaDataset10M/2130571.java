package xxl.core.io.fat;

import java.util.Iterator;
import java.util.NoSuchElementException;
import xxl.core.io.fat.util.DataManagement;
import xxl.core.io.fat.util.InorderTraverse;

/**
 * This iterator will return all entries of the whole directory structure except the free entries.
 */
public class ListEntriesIterator implements Iterator {

    /**
	 * Instance of FATDevice.
	 */
    private FATDevice device;

    /**
	 * Actual DirectoryEntryInformation object of the iteration.
	 */
    private DirectoryEntryInformation dirEntInf;

    /**
	 * Indicates if the iterator has one more element.
	 */
    private boolean hasNext = false;

    /**
	 * Iterator that is used to iterate over one cluster chain.
	 */
    private Iterator entriesIterator;

    /**
	 * Is used to manage different traversals of the directory structure.
	 * @see xxl.core.io.fat.util.InorderTraverse
	 */
    private DataManagement data = new InorderTraverse();

    /**
	 * Create an instance of this object.
	 * This iterator will return all entries of the whole directory structure except the free entries.
	 * @param device object of FATDevice. The returned objects by the next method are
	 * DirectoryEntryInformation objects, which contain all important information.
	 * @param clusterNumber the number of the cluster.
	 * @param isRoot indicates if the given clusterNumber belongs to the root directory.
	 * @see xxl.core.io.fat.DirectoryEntryInformation
	 */
    public ListEntriesIterator(FATDevice device, long clusterNumber, boolean isRoot) {
        this.device = device;
        entriesIterator = new EntriesFilterIterator(new ListClusterChainEntriesIterator(device, clusterNumber, isRoot), DIR.LIST_ALL_EXCEPT_FREE);
    }

    /**
	 * Returns true if the iteration has more elements. (In other words,
	 * returns true if next would return an element rather than throwing an exception.)
	 * @return true if the iterator has more elements.
	 */
    public boolean hasNext() {
        if (hasNext) return hasNext;
        if (entriesIterator.hasNext()) {
            dirEntInf = (DirectoryEntryInformation) entriesIterator.next();
            String name = DIR.getShortName(dirEntInf.directoryEntry);
            if (DIR.isDirectory(dirEntInf.directoryEntry) && !(name.equals(".") || name.equals(".."))) {
                data.add(new Long(DIR.getClusterNumber(dirEntInf.directoryEntry)));
            }
            hasNext = true;
            return hasNext;
        }
        while (data.hasNext()) {
            long cn = ((Long) data.next()).longValue();
            Iterator temp = new ListClusterChainEntriesIterator(device, cn, false);
            entriesIterator = new EntriesFilterIterator(temp, DIR.LIST_ALL_EXCEPT_FREE);
            if (hasNext()) {
                hasNext = true;
                return true;
            }
        }
        hasNext = false;
        return false;
    }

    /**
	 * Returns the next element in the iteration.
	 * @return the next element in the iteration.
	 * @throws NoSuchElementException iteration has no more elements.
	 */
    public Object next() throws NoSuchElementException {
        if (hasNext) {
            hasNext = false;
            return dirEntInf;
        } else {
            if (!hasNext()) throw new NoSuchElementException(); else {
                hasNext = false;
                return dirEntInf;
            }
        }
    }

    /**
	 * Not implemented yet.
	 * @throws UnsupportedOperationException
	 */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
