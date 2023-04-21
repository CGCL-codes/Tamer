package uk.org.ogsadai.tuple.sort;

import java.util.concurrent.Callable;

/**
 * A callable that serialises a tuple list and writes it to file.
 *
 * @author The OGSA-DAI Project Team.
 */
public class TupleListFileWriter implements Callable {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Tuple list sorter. */
    private final TupleListSort mSort;

    /** Sorted tuple list. */
    private SortedList<ComparableTuple> mList;

    /**
     * Constructs a new file writer.
     * 
     * @param sort
     *            the object that actually writes the file
     * @param list
     *            a sorted list of tuples
     */
    public TupleListFileWriter(TupleListSort sort, SortedList<ComparableTuple> list) {
        mSort = sort;
        mList = list;
    }

    /**
     * {@inheritDoc}
     */
    public Object call() throws Exception {
        mSort.writeList(mList);
        mList = null;
        System.gc();
        return null;
    }
}
