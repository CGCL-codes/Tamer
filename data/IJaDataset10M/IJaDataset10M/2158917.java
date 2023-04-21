package research.ui.views.comparators;

import org.eclipse.jface.viewers.Viewer;
import research.domain.Application;

public class ApplicationDesiredDateComparator extends EntityComparator {

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        if (e1 instanceof Application && e2 instanceof Application) {
            if (isDesc()) {
                Object e = e1;
                e1 = e2;
                e2 = e;
            }
            Application res1 = (Application) e1;
            Application res2 = (Application) e2;
            return res1.getDesired_date().compareTo(res2.getDesired_date());
        }
        return super.compare(viewer, e1, e2);
    }
}
