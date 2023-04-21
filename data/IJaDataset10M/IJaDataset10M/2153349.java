package org.eclipse.emf.common.ui.viewer;

import org.eclipse.jface.viewers.Viewer;

/**
 * Interface common to all objects that provide a viewer.
 */
public interface IViewerProvider {

    Viewer getViewer();
}
