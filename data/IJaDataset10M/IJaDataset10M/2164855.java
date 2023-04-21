package com.ivis.xprocess.ui.view.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import com.ivis.xprocess.core.Prioritizable;
import com.ivis.xprocess.core.PrioritizedGroup;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.util.NumberUtils;

public class PriorityGroupsTableLabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        if (element instanceof IElementWrapper && (columnIndex == 0)) {
            IElementWrapper elementWrapper = (IElementWrapper) element;
            return elementWrapper.getImage();
        }
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof IElementWrapper) {
            IElementWrapper elementWrapper = (IElementWrapper) element;
            if (elementWrapper.getElement() instanceof Prioritizable) {
                Prioritizable prioritizable = (Prioritizable) elementWrapper.getElement();
                PrioritizedGroup prioritizedGroup = prioritizable.getPriorities();
                switch(columnIndex) {
                    case 0:
                        return elementWrapper.getLabel();
                    case 1:
                        double weight;
                        if (prioritizedGroup != null) {
                            weight = prioritizedGroup.getWeight();
                            return NumberUtils.format(weight, 1);
                        }
                        return "";
                    case 2:
                        if (prioritizedGroup != null) {
                            weight = prioritizedGroup.getWeight();
                            double lowestPercentage = prioritizedGroup.getPercentLowest();
                            double lowest = weight * (lowestPercentage / 100);
                            return NumberUtils.format(lowest, 1);
                        }
                        return "";
                    default:
                        break;
                }
            }
        }
        return "";
    }

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }
}
