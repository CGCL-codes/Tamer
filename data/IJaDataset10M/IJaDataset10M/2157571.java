package org.yafra.rcp.admin;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.yafra.rcp.Activator;

/**
 * @author mwn
 * 
 */
public class YafraBusRoleLabelProvider extends LabelProvider implements ITableLabelProvider {

    private static final Image CHECKED = Activator.getImageDescriptor("icons/checked.gif").createImage();

    private static final Image UNCHECKED = Activator.getImageDescriptor("icons/unchecked.gif").createImage();

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == 2) {
            if (((MGYafraBusRole) element).getFlag()) {
                return CHECKED;
            } else {
                return UNCHECKED;
            }
        }
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        MGYafraBusRole user = (MGYafraBusRole) element;
        switch(columnIndex) {
            case 0:
                return user.getName();
            case 1:
                return user.getDescription();
            case 2:
                return String.valueOf(user.getFlag());
            default:
                throw new RuntimeException(" - ERROR out of range on table label provider");
        }
    }
}
