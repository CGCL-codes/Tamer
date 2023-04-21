package org.deft.widgets.importdialog;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class SegmentNodeComperator extends ViewerComparator {

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        SegmentNode n1 = (SegmentNode) e1;
        SegmentNode n2 = (SegmentNode) e2;
        if (n1.getPath() == null && n2.getPath() != null) {
            return -1;
        }
        if (n1.getPath() != null && n2.getPath() == null) {
            return 1;
        }
        return super.compare(viewer, e1, e2);
    }
}
