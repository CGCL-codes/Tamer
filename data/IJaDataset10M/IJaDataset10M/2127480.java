package de.mpiwg.vspace.diagram.edit.policies;

import org.eclipse.gef.Request;
import de.mpiwg.vspace.images.service.Image;

public class DropLocalImageRequest extends Request {

    public static final String REQ_DROP_LOCALIMAGE = "drop LocalImage";

    private Image dndImage;

    private de.mpiwg.vspace.metamodel.Image dropImage;

    public DropLocalImageRequest() {
        this.setType(REQ_DROP_LOCALIMAGE);
    }

    public void setDNDImage(Image imageData) {
        this.dndImage = imageData;
    }

    public Image getDNDImage() {
        return dndImage;
    }

    public void setDropImage(de.mpiwg.vspace.metamodel.Image dropImage) {
        this.dropImage = dropImage;
    }

    public de.mpiwg.vspace.metamodel.Image getDropImage() {
        return dropImage;
    }
}
