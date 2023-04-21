package net.openchrom.chromatogram.msd.ui.swt.internal.components.ions;

import net.openchrom.chromatogram.msd.ui.swt.Activator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author eselmeister
 */
public class MarkedIonsChooserLabelProvider extends LabelProvider implements ITableLabelProvider {

    @Override
    public String getColumnText(Object element, int columnIndex) {
        return getText(element);
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return getImage(element);
    }

    @Override
    public Image getImage(Object element) {
        ImageDescriptor descriptor = Activator.getImageDescriptor(Activator.IMAGE_ION);
        return descriptor.createImage();
    }
}
