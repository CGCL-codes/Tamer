package org.freelords.util;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/** A simple translator that gets objects as input, and extracts their class name,
  * which is then displayed in a list.
  *
  * @author Ulf Lorenz
  */
public class ClassNameProvider implements ILabelProvider {

    /** Returns the class name of an object. */
    public String getText(Object element) {
        return element.getClass().getSimpleName();
    }

    /** Always return true */
    public boolean isLabelProperty(Object element, String property) {
        return true;
    }

    /** There is no image representation available */
    public Image getImage(Object element) {
        return null;
    }

    /** Not implemented */
    public void addListener(ILabelProviderListener listener) {
    }

    /** Not implemented */
    public void dispose() {
    }

    /** Not implemented */
    public void removeListener(ILabelProviderListener listener) {
    }
}
