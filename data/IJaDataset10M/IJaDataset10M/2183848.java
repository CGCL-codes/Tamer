package org.remus.infomngmnt.efs.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.remus.infomngmnt.Adapter;
import org.remus.infomngmnt.common.ui.image.ResourceManager;
import org.remus.infomngmnt.efs.EFSActivator;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class EncryptedProjectDecorator extends LabelProvider implements ILightweightLabelDecorator {

    public void decorate(final Object element, final IDecoration decoration) {
        Object adapter = null;
        if (element instanceof Adapter) {
            adapter = ((Adapter) element).getAdapter(IProject.class);
        } else if (element instanceof IProject) {
            adapter = element;
        }
        if (adapter != null && adapter instanceof IProject) {
            try {
                if (((IProject) adapter).getDescription().getLocationURI() != null && ((IProject) adapter).getDescription().getLocationURI().getScheme().startsWith("encrypted")) {
                    decoration.addOverlay(ResourceManager.getPluginImageDescriptor(EFSActivator.getDefault(), "icons/iconexperience/lock.png"));
                }
            } catch (CoreException e) {
            }
        }
    }
}
