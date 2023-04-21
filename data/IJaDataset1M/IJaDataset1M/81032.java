package org.antlride.support.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Store image resources.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
public interface ImageStore {

    /**
   * Get the {@link ImageDescriptor} from the image store. This method create
   * the {@link ImageDescriptor} if necessary.
   * 
   * @param name The image name. Cannot be null.
   * @return The stored {@link ImageDescriptor}.
   */
    ImageDescriptor getImageDescriptor(NamedImage name);

    /**
   * Get the {@link ImageDescriptor} from the image store. This method create
   * the {@link ImageDescriptor} if necessary.
   * 
   * @param name The image name. Cannot be null.
   * @return The stored {@link Image}.
   */
    ImageDescriptor getOutlineImageDescriptor(NamedImage name);

    void registerImages(BaseUIPlugin owner, NamedImage[] images);
}
