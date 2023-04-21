package org.atomictagging.ui.tableviewer;

import java.util.List;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

/**
 * @author work
 * 
 */
public class TagsLabelProvider implements ITableLabelProvider, IColorProvider {

    private final Device device;

    private final Color cWhite;

    private final Color cGreen;

    private List<String> tagsOriginal;

    /**
	 * @param device
	 */
    public TagsLabelProvider(final Device device) {
        this.device = device;
        cWhite = new Color(device, 255, 255, 255);
        cGreen = new Color(device, 0, 255, 0);
    }

    @Override
    public Image getColumnImage(final Object element, final int columnIndex) {
        return null;
    }

    @Override
    public String getColumnText(final Object element, final int columnIndex) {
        return (String) element;
    }

    @Override
    public void addListener(final ILabelProviderListener listener) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isLabelProperty(final Object element, final String property) {
        return false;
    }

    @Override
    public void removeListener(final ILabelProviderListener listener) {
    }

    @Override
    public Color getForeground(final Object element) {
        return null;
    }

    @Override
    public Color getBackground(final Object element) {
        if (tagsOriginal != null && tagsOriginal.contains(element)) {
            return cWhite;
        }
        return cGreen;
    }

    /**
	 * @param tagsOriginal
	 *            the tagsOriginal to set
	 */
    public void setTagsOriginal(final List<String> tagsOriginal) {
        this.tagsOriginal = tagsOriginal;
    }

    /**
	 * @return the tagsOriginal
	 */
    public List<String> getTagsOriginal() {
        return tagsOriginal;
    }
}
