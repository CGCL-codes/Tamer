package com.volantis.mcs.migrate.impl.framework.identification;

import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.impl.framework.io.RestartInputStream;
import java.io.IOException;

/**
 * An object which can identify a typed resource by version and calculate
 * the sequence of steps which can be used to migrate30 the resource to the
 * target version.
 *
 * @mock.generate
 */
public interface TypeIdentifier {

    /**
     * Return the name of the type that is identified by this object.
     *
     * @return the name of the identified type.
     */
    String getName();

    /**
     * Identify a typed resource by version, calculating the sequence of
     * steps required to migrate30 the resource to the target version if
     * identified. It may return null if either no match could be found for the
     * resource, or no sequence of migration steps could be identified because
     * no migration was required

     * @param meta Additional properties of the input stream.
     * @param input an input stream to the content of the resource.
     * @return a match containing the step sequence, or null if no match was
     * found for the resource or no migration was required for the resource.
     * @throws IOException if there was an I/O error reading the input.
     * @throws ResourceMigrationException if there was a migration error.
     */
    Match identifyResource(InputMetadata meta, RestartInputStream input) throws IOException, ResourceMigrationException;
}
