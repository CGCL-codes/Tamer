package org.openremote.modeler.cache;

import java.util.Set;
import java.io.InputStream;
import org.openremote.modeler.exception.ConfigurationException;
import org.openremote.modeler.exception.NetworkException;

/**
 * Interface to define designer resource cache -- for storing images and other artifacts
 * from user's account in the designer application.  <p>
 *
 * This interface is generic and does not enforce any particular cache storage mechanism,
 * file-based, in-memory, etc.
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */
public interface ResourceCache<T> {

    /**
   * Returns an open stream from cache that can be used for reading a zip compressed archive
   * that contains all account artifacts (xml files, images, other configuration files) which
   * are currently stored in cache. <p>
   *
   * The input stream may be backed by an in-memory archive, file-based archive or other type
   * of archive storage depending on the resource cache implementation.
   *
   * @return    an open, ready-to-be-read, input stream from cache in zip compressed format
   *            containing all current cached artifacts
   *
   * @throws CacheOperationException
   *            If there's an error that is specific to the particular resource cache
   *            implementation. Resource cache implementations may subclass this exception
   *            type for more specific error handling. The errors may also be specific
   *            just to a single account rather than to the entire Designer application.
   *
   * @throws ConfigurationException
   *            If there's a misconfiguration of the designer that prevents the cache
   *            from operating correctly. Usually this type of exception indicates an issue
   *            that requires re-deployment of the designer application and is likely to
   *            impact multiple accounts.
   */
    InputStream openReadStream() throws CacheOperationException, ConfigurationException;

    /**
   * Opens a stream for writing a zip compressed archive into the cache containing
   * account resource files. <p>
   *
   * The writing uses a specialized output stream that allows (and expects) byte stream
   * to be marked complete upon closing in order for the cache to process the incoming
   * files. This helps the cache detect incomplete/broken streams at stream close. See
   * the {@link CacheWriteStream} for more information.
   *
   * @see CacheWriteStream
   * 
   * @return  a stream object that can be used to write a complete set of account artifacts
   *          into the cache as a zip compressed archive
   *
   * @throws  CacheOperationException
   *            If there's an error that is specific to the particular resource cache
   *            implementation. Resource cache implementations may subclass this exception
   *            type for more specific error handling. The errors may also be specific
   *            just to a single account rather than to the entire Designer application.
   *
   * @throws  ConfigurationException
   *            If there's a misconfiguration of the designer that prevents the cache
   *            from operating correctly. Usually this type of exception indicates an issue
   *            that requires re-deployment of the designer application and is likely to
   *            impact multiple accounts.
   */
    CacheWriteStream openWriteStream() throws CacheOperationException, ConfigurationException;

    /**
   * Updates the resource cache state from the Beehive server.
   *
   * @throws CacheOperationException
   *            If there's an error that is specific to the particular resource cache
   *            implementation. Resource cache implementations may subclass this exception
   *            type for more specific error handling. The errors may also be specific
   *            just to a single account rather than to the entire Designer application.
   *
   * @throws NetworkException
   *            If any errors occur with the network connection to Beehive server. It may be
   *            possible to recover from network exceptions by retrying the operation.
   *            Note that the exception class provides a severity level which indicates the
   *            severity of the network error and the likelyhood it can be recovered from.
   *
   * @throws ConfigurationException
   *            If there's a misconfiguration of the designer that prevents the cache
   *            from operating correctly. Usually this type of exception indicates an issue
   *            that requires re-deployment of the designer application and is likely to
   *            impact multiple accounts.
   */
    void update() throws CacheOperationException, NetworkException, ConfigurationException;

    /**
   * Indicates if the resource cache contains any resource artifacts for the associated account.
   *
   * @return    true if the resource cache contains resource artifacts, false otherwise
   *
   * @throws ConfigurationException
   *            If there's a misconfiguration of the designer that prevents the cache
   *            from operating correctly. Usually this type of exception indicates an issue
   *            that requires re-deployment of the designer application and is likely to
   *            impact multiple accounts.
   */
    boolean hasState() throws ConfigurationException;

    /**
   * TODO
   *
   * Marks specific image resources as being 'in-use' or active in the cache.
   *
   * This is added to support the currently implemented API usage towards cache, which assumes
   * dirty (or unused) resources and needs to determine the correct image resources to include.
   *
   * Should avoid increasing the usage of this method as the better solution may be to discard
   * this particular use altogether and ensure the dirty resources are appropriately removed
   * from cache (making this mark-method redundant).
   *
   * Further, the method signature would be more appropriately use ImageSource definition from
   * the domain model but the domain model is currently weighed down by so much garbage (JPA,
   * Gilead, GXT) that I don't want to pull those dependencies in. The whole domain model should
   * be rewritten first to be reusable across all project components.
   *
   * @param imageIdentifiers  types to identify image resources, specific to cache implementation
   */
    void markInUseImages(Set<T> imageIdentifiers);
}
