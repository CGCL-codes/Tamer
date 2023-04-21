package de.beas.explicanto.client.rcp.viewerproviders;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * Generic table label provider for <code>TableViewers</code>
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public abstract class GenericTableLabelProvider implements ITableLabelProvider {

    public static String LABEL = "label";

    /**
     * By default returns <code>null</code>.
     * Overwrite if necesary
     */
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public abstract String getColumnText(Object element, int columnIndex);

    /**
     * By default does nothing
     */
    public void addListener(ILabelProviderListener listener) {
    }

    /**
     * By default does nothing
     */
    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return property.equals(LABEL);
    }

    /**
     * By default does nothing
     */
    public void removeListener(ILabelProviderListener listener) {
    }
}
