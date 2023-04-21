package com.wizzer.m3g.viewer.ui.property;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * This class is a property descriptor for a M3G Camera.
 * 
 * @author Mark Millard
 */
public class CameraPropertyDescriptor extends PropertyDescriptor {

    /**
	 * Create a property descriptor for a M3G Camera.
	 * 
	 * @param id An identifier for the camera property.
	 * @param displayName The name to display for the property.
	 */
    public CameraPropertyDescriptor(Object id, String displayName) {
        super(id, displayName);
        setLabelProvider(new LabelProvider() {

            public String getText(Object element) {
                CameraPropertySource node = (CameraPropertySource) element;
                StringBuffer buffer = new StringBuffer();
                buffer.append("Camera Properties");
                return buffer.toString();
            }
        });
    }
}
