package org.kaleidofoundry.core.store;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javax.sql.DataSource;
import org.kaleidofoundry.core.lang.annotation.NotNull;
import org.kaleidofoundry.core.lang.annotation.Stateful;

/**
 * Resource store manager interface (stateful store), used to handle persistence of a single instance :
 * <ul>
 * <li>{@link #get()}
 * <li>{@link #unload()}
 * <li>{@link #reload()}
 * <li>{@link #store(Object)}
 * </ul>
 * a given resource, identify by its resource binding (like a path)
 * 
 * @author Jerome RADUGET
 * @param <B> type of the resource access {@link URI}, {@link URL}, {@link String}, {@link DataSource} path...
 * @param <R> type of the resource to store
 */
@Stateful
public interface SingleStore<B, R> {

    /**
    * @return resource binding informations to access the store single resource<br/>
    *         <p>
    *         you can use {@link URI}, {@link URL}, {@link String} as B if R is a {@link File} or {@link InputStream},<br/>
    *         you can use {@link DataSource} to access a database table<br/>
    *         ...<br/>
    *         </p>
    *         think that the resource pointed to by B can be load or update between multiple node cluster,<br/>
    *         for a database no problem, concurrent access handled by the database,<br/>
    *         but if resource is a file path (http url, ftp url, file uri, ...), be careful to shared file via dfs
    */
    @NotNull
    B getResourceBinding();

    /**
    * @return resource store has been loaded ?
    */
    boolean isLoaded();

    /**
    * connect resource to load it <br/>
    * multi-thread access,it have to be synchronized
    * 
    * @return current instance
    * @throws ResourceException
    */
    @NotNull
    R get() throws ResourceException;

    /**
    * unload it <br/>
    * multi-thread access,it have to be synchronized
    * 
    * @throws ResourceException
    */
    void unload() throws ResourceException;

    /**
    * reload it <br/>
    * multi-thread access,it have to be synchronized
    * 
    * @return current instance
    * @throws ResourceException
    */
    @NotNull
    R reload() throws ResourceException;

    /**
    * store updates on current R instance<br/>
    * multi-thread access,it have to be synchronized
    * 
    * @param r
    * @return current instance
    * @throws ResourceException
    */
    @NotNull
    R store(R r) throws ResourceException;
}
