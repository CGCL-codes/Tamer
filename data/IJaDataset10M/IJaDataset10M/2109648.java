package org.docflower.printforms.components.rcp;

import java.util.List;
import org.eclipse.jface.viewers.*;

public class ComboContentProvider implements IStructuredContentProvider {

    @Override
    public Object[] getElements(Object inputElement) {
        List<?> items = (List<?>) inputElement;
        return items.toArray();
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    @Override
    public void dispose() {
    }
}
