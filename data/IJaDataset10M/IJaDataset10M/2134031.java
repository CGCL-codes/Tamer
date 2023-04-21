package flattree.tree;

import flattree.flat.ReadLine;
import flattree.flat.WriteLine;
import flattree.flat.read.DelimitedReadLine;
import flattree.flat.write.DelimitedWriteLine;

/**
 * A {@link Node} which writes and reads characters bounded by a delimiter.
 * 
 * @see #setDelimiter(String)
 */
public class DelimitedNode extends AbstractNode {

    /**
	 * The default delimiter (if none is configured) is set to the systems
	 * default line separator.
	 * 
	 * @see #getDelimiter()
	 */
    public static final String DEFAULT_DELIMITER = System.getProperty("line.separator");

    private String delimiter;

    /**
	 * Default constructor.
	 */
    public DelimitedNode() {
    }

    public DelimitedNode(String name) {
        this(name, DEFAULT_DELIMITER);
    }

    public DelimitedNode(String name, String delimiter) {
        super(name);
        setDelimiter(delimiter);
    }

    /**
	 * Set the delimiter.
	 * 
	 * @param delimiter
	 *            the delimiter
	 * @throws IllegalArgumentException
	 *             if delimiter is empty
	 */
    public void setDelimiter(String delimiter) {
        if (delimiter == null || delimiter.length() == 0) {
            throw new IllegalArgumentException("delimiter must not be empty");
        }
        this.delimiter = delimiter;
    }

    /**
	 * Get the delimiter.
	 * 
	 * @return delimiter
	 */
    public String getDelimiter() {
        if (delimiter == null) {
            delimiter = DEFAULT_DELIMITER;
        }
        return delimiter;
    }

    /**
	 * Limits the given line to with the configured delimiter.
	 * 
	 * @see #getDelimiter()
	 */
    @Override
    protected ReadLine beforeRead(final ReadLine line) {
        return new DelimitedReadLine(line, getDelimiter());
    }

    /**
	 * Limits the given line to with the configured delimiter.
	 * 
	 * @see #getDelimiter()
	 */
    @Override
    protected WriteLine beforeWrite(WriteLine line) {
        return new DelimitedWriteLine(line, getDelimiter());
    }
}
