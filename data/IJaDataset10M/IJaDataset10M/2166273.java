package de.uni_leipzig.lots.common.util;

public interface Queue {

    public Object peek();

    public Object pop();

    public Object push(Object o);

    public boolean isEmpty();

    public int size();
}
