package net.sf.beatrix.ui.viewers;

import org.eclipse.jface.viewers.Viewer;

public interface ViewerProvider<T extends Viewer> {

    public T getViewer();
}
