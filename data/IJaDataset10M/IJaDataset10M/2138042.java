package edu.uiuc.ncsa.security.rdf.legacy;

import edu.uiuc.ncsa.security.core.Identifiable;
import edu.uiuc.ncsa.security.core.Removable;
import java.net.URI;

/**
 * Interface for a configuration. The intent is that each configuration will acquire
 * configuration information for components (such as database tables, connectivity,
 * security information, etc.) which will be persisted in some way.  Management of multiple
 * configurations then can be done. You must extend this to make it useful.
 * <p><b>Note:</b> Each configuration is uniquely identified by a URI.
 * <p>Created by Jeff Gaynor<br>
 * on Feb 23, 2011 at  1:38:29 PM
 * @deprecated
 */
public interface OldConfiguration extends Removable, Identifiable {

    /**
     * Every root has a unique identifier. The root knows this and can be queried for it.
     *
     * @return
     */
    URI getURIIdentifier();
}
