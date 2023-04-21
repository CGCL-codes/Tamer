package net.sf.thyvin.core.res;

import java.io.IOException;
import java.io.InputStream;

public interface IResource {

    /**
	 * @return The name of the resource. This is the
	 * same as the one used to fetch the resource.
	 */
    public abstract String getName();

    /**
	 * @return An readable stream.
	 * @throws IOException
	 */
    public abstract InputStream getInputStream() throws IOException;

    /**
	 * @return The size of the resource in bytes.
	 */
    public abstract long getResourceSize();

    /**
	 * @return A long value representing the time the file was
	 * last modified,measured in milliseconds since the epoch
	 * (00:00:00 GMT, January 1, 1970), or 0L if not known.
	 */
    public abstract long getLastModified();
}
