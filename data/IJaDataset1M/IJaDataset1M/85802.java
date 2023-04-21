package net.nothinginteresting.ylib.viewers;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;

public interface IContentViewer extends IViewer {

    IContentProvider getContentProvider();

    ContentViewer getViewer();

    void setContentProvider(IContentProvider contentProvider);

    IBaseLabelProvider getLabelProvider();

    void setLabelProvider(IBaseLabelProvider labelProvider);
}
