package percussiongenerator.model;

/**
 *
 * @author Jannes Plyson
 */
public class Pair<E, F> {

    public E first;

    public F second;

    public Pair() {
    }

    public Pair(E e, F f) {
        first = e;
        second = f;
    }
}
