package dequestackqueue;

/**
 *
 * @author User
 */
public class List {

    List next;

    List prev;

    int value;

    List(int w, List l) {
        value = w;
        next = null;
        prev = null;
    }
}
