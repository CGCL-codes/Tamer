package jaxilstudio.business.collector;

import java.util.Comparator;

public class ListCollectorItemComparator implements Comparator<ListCollectorItem> {

    @Override
    public int compare(ListCollectorItem o1, ListCollectorItem o2) {
        return o1.getSearch().compareTo(o2.getSearch());
    }
}
