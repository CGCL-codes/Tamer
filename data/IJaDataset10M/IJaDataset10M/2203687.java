package org.parallelj.tools.typeselector.ext.primitives;

import org.eclipse.swt.graphics.Image;
import org.parallelj.tools.typeselector.core.types.TypeInfo;

/**
 * TypeInfo extension for Primitive Types
 * 
 * @author Atos Worldline
 */
public class PrimitiveTypeInfo extends TypeInfo {

    private static final String IMAGE_PLUGINID = "org.eclipse.jdt.ui";

    private static final String IMAGE_FILEPATH = "/icons/full/obj16/typevariable_obj.gif";

    /**
	 * Container name constant for primitive Types
	 */
    private static final String CONTAINER_NAME = "Java Primitive Type";

    /**
	 * Creates new PrimitiveTypeInfo
	 * 
	 * @param elementName
	 *            : name of the element
	 */
    public PrimitiveTypeInfo(String elementName) {
        super(elementName, null, PrimitiveTypeInfo.CONTAINER_NAME);
    }

    @Override
    public Image getImage() {
        return Activator.getDefault().getImage(IMAGE_PLUGINID, IMAGE_FILEPATH);
    }

    @Override
    public Image getContainerImage() {
        return Activator.getDefault().getImage(IMAGE_PLUGINID, IMAGE_FILEPATH);
    }
}
