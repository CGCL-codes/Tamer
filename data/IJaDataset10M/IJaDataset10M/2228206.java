package com.aptana.ide.syncing;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import com.aptana.ide.core.io.IVirtualFile;

/**
 * A class to decorate any items which are sync targets
 * 
 * @author Ingo Muschenetz
 */
public class VirtualFileCloakedDecorator implements ILightweightLabelDecorator {

    private static final ImageDescriptor SYNC_FOLDER;

    static {
        SYNC_FOLDER = SyncingPlugin.getImageDescriptor("icons/cloaked_overlay.gif");
    }

    /**
	 * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang.Object,
	 *      org.eclipse.jface.viewers.IDecoration)
	 */
    public void decorate(Object element, IDecoration decoration) {
        if (!(element instanceof IVirtualFile)) {
            return;
        }
        IVirtualFile lf = (IVirtualFile) element;
        if (lf.isCloaked()) {
            decoration.addOverlay(SYNC_FOLDER);
        }
    }

    /**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
    public void addListener(ILabelProviderListener listener) {
    }

    /**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
    public void dispose() {
    }

    /**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    /**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
    public void removeListener(ILabelProviderListener listener) {
    }
}
