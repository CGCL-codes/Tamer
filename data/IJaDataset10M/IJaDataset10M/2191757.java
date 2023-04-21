package net.sf.rcpmoney.ui.comparator;

import net.sf.rcpmoney.entity.Recipient;
import org.eclipse.jface.viewers.Viewer;

public class RecipientComparator extends AbstractViewerComparator {

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        Recipient r1 = (Recipient) e1;
        Recipient r2 = (Recipient) e2;
        int result = r1.getName().compareTo(r2.getName());
        if (direction == DESCENDING) {
            result = -result;
        }
        return result;
    }
}
