package de.schlund.pfixcore.editor2.core.spring;

import org.pustefixframework.editor.common.dom.Project;
import org.pustefixframework.editor.common.dom.Target;
import de.schlund.pfixxml.targets.AuxDependencyFile;

/**
 * Service providing methods to create Target objects
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public interface TargetFactoryService {

    /**
     * Creates Target using a Target object from the Pustefix generator
     * 
     * @param pfixTarget Pustefix generator Target object
     * @param project Project target belongs to
     * @return New Target object
     */
    Target getTargetFromPustefixTarget(de.schlund.pfixxml.targets.Target pfixTarget, Project project);

    /**
     * Creates Target using an AuxDependency object from the Pustefix generator.
     * Only intended to be used for auxilliary leaf targets.
     * 
     * @param auxdep AuxDependency object to use
     * @return New Target object
     */
    Target getLeafTargetFromPustefixAuxDependency(AuxDependencyFile auxdep);
}
