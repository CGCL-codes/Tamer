package com.ibm.realtime.flexotask.development.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import com.ibm.realtime.flexotask.editor.FlexotaskClasspathInitializer;
import com.ibm.realtime.flexotask.editor.model.GlobalTiming;

/**
 * Action to recompute the classpaths of projects that use the Flexotask Classpath container (if called on other projects
 *   nothing is done).  Used by the user to correct for various anomalies that arise when projects are added and deleted
 *   etc.
 */
public class RecomputeClasspaths extends CommonProjectAction {

    void processProject(IProject project) {
        IJavaProject javaProject = JavaCore.create(project);
        boolean hasClasspath;
        try {
            hasClasspath = hasFlexotaskClasspath(javaProject);
        } catch (JavaModelException e) {
            hasClasspath = false;
        }
        if (hasClasspath) {
            GlobalTiming.resetProviders();
            FlexotaskClasspathInitializer.recomputeClasspath(javaProject);
        }
    }
}
