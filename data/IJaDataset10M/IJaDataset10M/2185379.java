package org.genie.browser.contentprovider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.genie.browser.Activator;
import org.genie.browser.contentprovider.ModelProjectContentProvider.MetamodelProxy;
import org.genie.browser.contentprovider.ModelProjectContentProvider.ModelProxy;
import org.genie.model.ModelProject;
import org.xmi.infoset.XMIExtension;
import org.xmi.infoset.XMIStructure;

/**
 * The LabelProvider for the Browser <p>
 * This LabelProvider will lookup registered providers.
 * @author e3a
 */
public class ModelProjectLabelProvider extends LabelProvider {

    @Override
    public String getText(Object obj) {
        if (obj != null) {
            try {
                if (obj instanceof ModelProject) return ((ModelProject) obj).getId(); else if (obj instanceof ModelProxy) return "Model"; else if (obj instanceof XMIExtension) return ((XMIExtension) obj).getName(); else if (obj instanceof MetamodelProxy) return ((MetamodelProxy) obj).namespace; else if (obj instanceof IAdaptable) {
                    XMIStructure structure = (XMIStructure) ((IAdaptable) obj).getAdapter(XMIStructure.class);
                    return Activator.getDefault().getLabelProvider(structure.getXmiNamespace()).getText(obj);
                } else if (obj instanceof String) return (String) obj; else return obj.getClass().getSimpleName();
            } catch (CoreException e) {
                System.err.println("CoreException:getText:" + this.getClass().getSimpleName() + ":" + e.toString());
                e.printStackTrace();
            }
        }
        return "(null)";
    }

    @Override
    public Image getImage(Object element) {
        try {
            if (element instanceof ModelProject) {
                ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
                return sharedImages.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER).createImage();
            } else if (element instanceof ModelProxy) {
                ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
                return sharedImages.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER).createImage();
            } else if (element instanceof IAdaptable) {
                XMIStructure structure = (XMIStructure) ((IAdaptable) element).getAdapter(XMIStructure.class);
                if (structure != null) {
                    ILabelProvider provider = loadLabelProvider(structure);
                    if (provider != null) {
                        return provider.getImage(element);
                    }
                }
            }
        } catch (CoreException e) {
            System.err.println("CoreException:getImage:" + this.getClass().getSimpleName() + ":" + e.toString());
            e.printStackTrace();
        }
        return super.getImage(element);
    }

    private ILabelProvider loadLabelProvider(XMIStructure element) throws CoreException {
        return Activator.getDefault().getLabelProvider(element.getXmiNamespace());
    }
}
