package aoetec.javalang._261gererics;

public class Box4<T extends Number> {

    private T t;

    public void add(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }
}
