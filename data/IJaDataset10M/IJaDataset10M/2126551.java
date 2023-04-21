package com.farata.dto2extjs.asap.env;

import org.eclipse.jdt.apt.core.env.EclipseAnnotationProcessorEnvironment;
import org.eclipse.jdt.apt.core.env.Phase;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;

public class EclipseEnvironmentInspector implements IEnvironmentInspector {

    public boolean isReconciliation(final AnnotationProcessorEnvironment env) {
        if (env instanceof EclipseAnnotationProcessorEnvironment) {
            final EclipseAnnotationProcessorEnvironment eenv = (EclipseAnnotationProcessorEnvironment) env;
            final Phase phase = eenv.getPhase();
            return Phase.RECONCILE == phase;
        } else return false;
    }

    public static final IEnvironmentInspector INSTANCE = new EclipseEnvironmentInspector();
}
