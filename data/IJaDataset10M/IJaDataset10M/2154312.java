package net.sf.fileexchange.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.sf.fileexchange.api.snapshot.ResourceTreeSnapshot;
import net.sf.fileexchange.api.snapshot.events.StorageEventListener;
import net.sf.fileexchange.util.http.MovedPermanentlyException;
import net.sf.fileexchange.util.http.RequestHeader;
import net.sf.fileexchange.util.http.Resource;

public interface ResourceTree {

    /**
	 * 
	 * @return a small {@String} giving some additional information
	 *         about the root of the resource tree, like the number of children.
	 *         Usually this {@link String} is about 7 characters long and it
	 *         should not be longer then 20 characters but can be. May return
	 *         null if no meta data is available.
	 */
    String getMetaData();

    /**
	 * 
	 * @param path
	 *            the request path divided into it's components. The list never
	 *            contains the leading slash. If only the slash got requested
	 *            then this method gets called with an empty list. The path get
	 *            gets divided in such a way that the components never start
	 *            with a slash, but may end with a slash. Examples: The path
	 *            <code>"/dir/file"</code> gets divided into
	 *            <code>[&quot;dir/&quot;,&quot;file&quot;]</code> or
	 *            <code/>&quot;/dir/sub/&quot;</code> to
	 *            <code>[&quot;dir/&quot;,&quot;sub/&quot;]</code>
	 * 
	 * @param header
	 *            the header of the request.
	 * @param inputStream
	 *            the input stream from which the body of the request can be
	 *            read from.
	 * @return the requested resource or null if it does not exist.
	 * @throws InterruptedException
	 *             if the thread gets interrupted before it can complete this
	 *             method.
	 * @throws IOException
	 *             for undefined reasons.
	 * @throws MovedPermanentlyException
	 *             if the requested resource is now available at a different
	 *             path.
	 */
    Resource getResource(List<String> path, RequestHeader header, InputStream inputStream) throws InterruptedException, IOException, MovedPermanentlyException;

    boolean isDirectoryLike();

    void registerStorageEventListener(StorageEventListener<ResourceTreeSnapshot> listener);

    void unregisterStorageEventListener(StorageEventListener<ResourceTreeSnapshot> listener);
}
