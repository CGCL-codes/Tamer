package generics;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 10.05.11
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public abstract class Limitations<T> {

    private T obj;

    private T[] obj_array;

    public abstract T createT();

    public abstract T[] createArrayOfT();

    public void init() {
        obj = createT();
        obj_array = createArrayOfT();
    }
}
