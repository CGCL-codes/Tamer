package net.sourceforge.taggerplugin.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import net.sourceforge.taggerplugin.model.Tag;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Interface for tag I/O handlers.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public interface ITagIo {

    /**
	 * Used to read the tag information from the Reader and create the tag objects.
	 *
	 * @param reader the reader
	 * @param monitor the progress monitor
	 * @return the tags contained in the reader
	 * @throws IOException if there is a problem reading the tags
	 */
    public Tag[] readTags(Reader reader, IProgressMonitor monitor) throws IOException;

    /**
	 * Used to write the tag data to the specified writer.
	 *
	 * @param writer the writer
	 * @param tags the tags to be written
	 * @param monitor the progress monitor
	 * @throws IOException if there is a problem writing the tags
	 */
    public void writeTags(Writer writer, Tag[] tags, IProgressMonitor monitor) throws IOException;
}
