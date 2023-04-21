package net.sf.rcpforms.wrapper;

import org.eclipse.jface.viewers.ContentViewer;

/**
 * controls which are capable to provide a viewer
 * @author Marco van Meegen
 */
public interface IViewer {

    ContentViewer getViewer();
}
