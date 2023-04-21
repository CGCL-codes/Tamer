package density;

public interface MyList<T> {

    void add(T obj);

    T get(int i);
}

class MyArrayList<U> implements MyList<U> {

    Object[] array = new Object[10];

    int size = 0;

    public void add(U obj) {
        if (size == array.length) {
            Object[] newArray = new Object[size + 10];
            for (int i = 0; i < size; ++i) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
        array[size] = obj;
        size += 1;
    }

    public U get(int i) {
        return (U) array[i];
    }
}
