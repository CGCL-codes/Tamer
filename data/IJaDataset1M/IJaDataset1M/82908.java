package net.sourceforge.skejula.business.logic;

import java.util.List;

/**
 * 
 * @author Renato Miceli
 * 
 */
public interface DAO<E> {

    public boolean insert(E element);

    public boolean update(E element);

    public boolean remove(E element);

    public E get(E element);

    public List<E> list();

    public void clearAll();

    public void save();

    public void load();
}
