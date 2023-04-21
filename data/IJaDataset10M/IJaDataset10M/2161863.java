package de.beas.explicanto.client.template;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

/**
 * ShapeAttributes - collection with the attributes of the shapes. It offers
 * specialized access to the known attributes.
 * 
 * @author marius.staicu
 * @version 1.0
 *  
 */
public class ShapeAttributes extends AttributesCollection {

    /**
	 * Constructs the collection from the parent Element
	 * 
	 * @param parent -
	 *            the parent whos attributes are captured
	 */
    public ShapeAttributes(Element parent) {
        super(parent, TemplateConstants.SHAPE_PROPERTY);
    }

    /**
	 * @return Returns the type attribute 
	 */
    public String getType() {
        return getAttribute(TemplateConstants.SHAPE_TYPE);
    }

    /**
	 * 
	 * Returns the image attribute
	 * 
	 * @param device -
	 *            the SWT device
	 * @return Image associated with the current shape
	 * @throws TemplateException
	 */
    public Image getImage(Device device) throws TemplateException {
        String resLocation = getAttribute(TemplateConstants.SHAPE_IMAGE);
        TemplateResource res = getElement().getDocument().getResource(resLocation);
        return new Image(device, res.getContent());
    }
}
