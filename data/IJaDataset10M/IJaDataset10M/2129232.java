package playground.anhorni.crossborder;

import java.io.Serializable;
import java.util.Comparator;

public class LinkComparator implements Comparator<MyLink>, Serializable {

    private static final long serialVersionUID = 1L;

    public int compare(final MyLink o1, final MyLink o2) {
        return Double.compare(o2.getVolume(), o1.getVolume());
    }
}
