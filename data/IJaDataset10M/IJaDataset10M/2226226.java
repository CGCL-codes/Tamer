package org.ucdetector.report;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.ucdetector.preferences.WarnLevel;

/**
 *
 */
public interface IUCDetctorReport {

    void reportMarker(IJavaElement javaElement, String message, int line, WarnLevel level, String markerType, String problem) throws CoreException;

    void endReport(Object[] selected, long start);
}
