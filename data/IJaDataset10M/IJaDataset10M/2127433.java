package de.jaret.util.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * Simple "toString"-LabelProvider.
 * 
 * @author Peter Kliem
 * @version $Id: DefaultLabelProvider.java 242 2007-02-11 21:05:07Z olk $
 */
public class DefaultLabelProvider implements ILabelProvider {

    public Image getImage(Object arg0) {
        return null;
    }

    public String getText(Object o) {
        return o.toString();
    }

    public void addListener(ILabelProviderListener arg0) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object arg0, String arg1) {
        return false;
    }

    public void removeListener(ILabelProviderListener arg0) {
    }
}
