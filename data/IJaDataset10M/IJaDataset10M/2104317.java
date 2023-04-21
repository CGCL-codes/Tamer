package fr.upmf.animaths.client.mvp.MathObject;

public interface IMOHasSeveralChildren<T extends MOElement<?>> {

    public void add(T child);

    public void add(int index, T child);

    public void add(T child, T refChild, boolean after);

    public T get(int i);

    public int indexOf(T child);

    public void remove(T child);

    public void remove(int i);

    public int size();
}
