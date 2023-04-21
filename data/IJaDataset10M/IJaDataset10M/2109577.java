package net.sourceforge.taggerplugin.io;

/**
 * Factory for creating Tag I/O handlers based on desired format type.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagIoFactory {

    private TagIoFactory() {
        super();
    }

    /**
	 * Creates the appropriate handler based on the format type.
	 *
	 * @param format the data format
	 * @return the handler for the data format type
	 * @throws Exception if there is a problem
	 */
    public static final ITagIo create(TagIoFormat format) throws Exception {
        if (format.equals(TagIoFormat.XML)) {
            return (new TagXmlIo());
        } else if (format.equals(TagIoFormat.CSV)) {
            return (new TagCsvIo());
        } else if (format.equals(TagIoFormat.MEMENTO)) {
            return (new TagMementoIo());
        }
        return (null);
    }
}
