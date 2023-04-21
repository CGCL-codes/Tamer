package org.opennms.netmgt.config.collector;

import java.io.File;
import org.opennms.netmgt.model.RrdRepository;

/**
 * <p>ResourceIdentifier interface.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public interface ResourceIdentifier {

    /**
     * <p>getOwnerName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOwnerName();

    /**
     * <p>getResourceDir</p>
     *
     * @param repository a {@link org.opennms.netmgt.model.RrdRepository} object.
     * @return a {@link java.io.File} object.
     */
    public File getResourceDir(RrdRepository repository);
}
