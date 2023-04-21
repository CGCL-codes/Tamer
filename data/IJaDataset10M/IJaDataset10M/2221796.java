package ejb.bprocess.opac;

/**
 *
 * @author  administrator
 */
public class AuthorFileBrowseComparator implements java.util.Comparator {

    /** Creates a new instance of AuthorFileBrowseComparator */
    public AuthorFileBrowseComparator() {
    }

    public int compare(Object o1, Object o2) {
        Object ob1[] = (Object[]) o1;
        Object ob2[] = (Object[]) o2;
        String str = ((String) ob1[3]).toUpperCase();
        String str1 = ((String) ob2[3]).toUpperCase();
        return str.compareTo(str1);
    }
}
