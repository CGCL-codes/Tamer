package cn.edu.bit.ss.util;

public interface Queue<T> {

    public boolean isEmpty();

    public void add(T t);

    public T getNext();

    public boolean isFull();

    public int getSize();
}
