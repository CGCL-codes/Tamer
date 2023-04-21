package net.sf.brightside.eterminals.facade;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public interface GetFacade<Type> {

    Object getById(Class<Type> clazz, Serializable id);

    Object getLast(Class<Type> clazz);

    List<Type> getAll(Class<Type> clazz);

    List<Type> getByNaziv(Class clazz, String naziv);

    Object getByUsername(Class clazz, String username);
}
