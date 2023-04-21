package com.volantis.mcs.migrate.impl.framework.graph;

import com.volantis.mcs.migrate.api.framework.Version;
import java.util.Iterator;

/**
 * A high level interface to a graph of migration steps for a particular
 * resource type.
 *
 * @mock.generate
 */
public interface Graph {

    /**
     * Return the sequence of steps required to migrate30 from the version
     * provided to the target version as an iterator.
     *
     * @param startVersion the version to start from.
     * @return the sequence of steps from the start version to the target
     *      version.
     */
    Iterator getSequence(Version startVersion);

    /**
     * Return the target version of this graph.
     *
     * @return Version the target version of this graph.
     */
    Version getTargetVersion();
}
