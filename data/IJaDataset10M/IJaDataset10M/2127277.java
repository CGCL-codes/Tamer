package hu.sztaki.lpds.pgportal.services.is.mds2;

import java.util.Comparator;

/**
  *
  * @author  boci
  */
public class MDSResourceComparator implements Comparator {

    /** Creates a new instance of MDSResourceComparator */
    public MDSResourceComparator() {
    }

    public int compare(Object obj1, Object obj2) {
        return ((MDSResource) obj1).getHost().compareToIgnoreCase(((MDSResource) obj2).getHost());
    }
}
