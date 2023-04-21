package org.jmlspecs.samples.list.iterator;

public interface Iterator {

    public void next();

    public boolean isDone();

    public Object currentItem();
}
