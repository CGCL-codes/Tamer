package net.sf.jtables.table;

import java.io.IOException;
import net.sf.jtables.table.impl.IntegerTable;
import net.sf.jtables.table.impl.StringTable;
import net.sf.kerner.utils.io.buffered.IOIterable;
import net.sf.kerner.utils.io.buffered.IOIterator;

/**
 * 
 * A {@code TableReader} reads an {@link AnnotatedTable} from an input source.
 * </p> It does so by extending {@link IOIterable} in oder to provide
 * possibility to iterate over a table's rows. </p> Via
 * {@link TableReader#readAll()} it is also possible to read in a whole table at
 * once.
 * 
 * @see IOIterable
 * @see AnnotatedTable
 * @see StringTable
 * @see IntegerTable
 * 
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2012-01-25
 * 
 * @param <T>
 *            type of elements in {@code Table}
 */
public interface TableReader<T> extends IOIterable<Row<T>> {

    /**
	 * Close this reader.
	 * 
	 * @see java.io.Reader#close()
	 * 
	 */
    void close();

    /**
	 * 
	 * Read a {@link Table} at once.
	 * 
	 * @return new instance of {@code AnnotatedTable} that was read
	 * @throws IOException
	 *             if reading failed
	 */
    <A extends AnnotatedTable<T>> A readAll() throws IOException;

    /**
	 * 
	 */
    IOIterator<Row<T>> getIterator() throws IOException;
}
