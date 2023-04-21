package garbage;

import java.util.Vector;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class ImageDescriptorReferences {

    private Image image;

    private ImageDescriptor imageDescriptor;

    private Vector references = new Vector();

    ImageDescriptorReferences(ImageDescriptor imageDescriptor) {
        this.imageDescriptor = imageDescriptor;
    }

    public Image getImage() {
        if (image == null) image = imageDescriptor.createImage();
        return image;
    }

    public ImageDescriptor getImageDescriptor() {
        return imageDescriptor;
    }

    public void connect(Object reference) {
        if (!references.contains(reference)) references.add(reference);
    }

    public void disconnect(Object reference) {
        references.remove(reference);
        if (references.size() == 0 && image != null) {
            image.dispose();
            image = null;
        }
    }
}
