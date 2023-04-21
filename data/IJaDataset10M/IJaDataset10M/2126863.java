package org.objectwiz.plugin.async.iop.customization;

import java.util.Map;
import javax.persistence.MappedSuperclass;
import org.objectwiz.core.Application;
import org.objectwiz.core.facet.customization.ApplicationSpecificDescriptor;
import org.objectwiz.plugin.async.AsynchroneousProcess;

/**
 * Base class for describing a process that exports data from the application.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
@MappedSuperclass
public abstract class PersistentExportDescriptor extends ApplicationSpecificDescriptor {

    public abstract AsynchroneousProcess createProcess(Application application, Map[] parametersMap);
}
