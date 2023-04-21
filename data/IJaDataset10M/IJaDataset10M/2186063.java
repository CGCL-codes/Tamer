package jgp.interfaces;

public interface RandomAccessable {

    Object getElementAt(int i);

    void setElementAt(int i, Object o);

    int getSize();

    void insert(Object o);
}
