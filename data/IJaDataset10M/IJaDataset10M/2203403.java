package net.hakulaite.maverick.model;

import net.hakulaite.maverick.MaverickPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Sami Siren
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewsElement extends MavElement implements IMavElement {

    /**
   * @param parent
   * @param name
   */
    public ViewsElement(IAdaptable parent, String name) {
        super(parent, name);
    }

    public ImageDescriptor getImageDescriptor(Object object) {
        return MaverickPlugin.getDefault().getImageRegistry().getDescriptor("views");
    }
}
