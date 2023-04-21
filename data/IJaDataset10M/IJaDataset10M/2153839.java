package com.aptana.ide.internet.internal.proxy;

import java.util.Collection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Monitor content provider.
 */
public class NonProxyHostsContentProvider implements IStructuredContentProvider {

    /**
	 * MonitorContentProvider constructor comment.
	 */
    public NonProxyHostsContentProvider() {
        super();
    }

    /**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 * 
	 * Disposes of this content provider.  
	 */
    public void dispose() {
    }

    /**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 * 
	 * Returns the elements to display in the viewer 
	 * when its input is set to the given element. 
	 */
    public Object[] getElements(Object inputElement) {
        Collection coll = (Collection) inputElement;
        return coll.toArray(new String[0]);
    }

    /**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 * 
	 * Notifies this content provider that the given viewer's input
	 * has been switched to a different element.
	 */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
