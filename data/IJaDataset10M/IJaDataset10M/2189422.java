package br.upe.dsc.caeto.core;

import java.util.Iterator;

public interface IRepository<E> {

    public void insert(E element);

    public void remove(E element);

    public void update(E element);

    public E search(String name);

    public boolean isElement(String name);

    public void empty();

    public Iterator<E> iterator();

    public Iterable<E> getElements();
}
