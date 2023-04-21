package reconcile.util;

import java.util.Comparator;
import reconcile.data.Annotation;

public class ByteSpanComparator implements Comparator<Annotation> {

    @Override
    public int compare(Annotation a, Annotation b) {
        if (a.getStartOffset() < b.getStartOffset()) {
            return -1;
        } else if (a.getStartOffset() == b.getStartOffset()) {
            if (a.getEndOffset() < b.getEndOffset()) {
                return -1;
            } else if (a.getEndOffset() > b.getEndOffset()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }
}
