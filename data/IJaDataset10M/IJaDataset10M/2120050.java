package org.alcatel.jsce.servicecreation.ui.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;
import org.mobicents.eclipslee.servicecreation.ui.SbbResourceAdaptorTypePanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 *  Description:
 * <p>
 * Loads Ressource adatpers for the SBB creation wizard.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class LoadRAJob extends UIJob {

    private SbbResourceAdaptorTypePanel control = null;

    private String project = null;

    /**
	 * @param name
	 */
    public LoadRAJob(String name) {
        super(name);
    }

    /**
	 * @param name
	 * @param control
	 * @param project
	 */
    public LoadRAJob(String name, SbbResourceAdaptorTypePanel control, String project) {
        super(name);
        this.control = control;
        this.project = project;
    }

    /**
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
    public IStatus runInUIThread(IProgressMonitor monitor) {
        monitor.beginTask(getName(), 100);
        if (this.project != null) {
            SbbResourceAdaptorTypePanel panel = (SbbResourceAdaptorTypePanel) control;
            if (panel != null) {
                monitor.worked(10);
                panel.clearResourceAdaptorType();
                DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault().getComponents(BaseFinder.JAR_DIR, project, monitor);
                monitor.worked(90);
                for (int i = 0; i < xml.length; i++) {
                    ResourceAdaptorTypeJarXML ev = (ResourceAdaptorTypeJarXML) xml[i];
                    ResourceAdaptorTypeXML[] resourceAdaptorTypes = ev.getResourceAdaptorTypes();
                    for (int j = 0; j < resourceAdaptorTypes.length; j++) {
                        panel.addResourceAdaptorType(ev, resourceAdaptorTypes[j]);
                    }
                }
                panel.repack();
            }
        }
        IStatus result = Status.OK_STATUS;
        monitor.done();
        return result;
    }
}
