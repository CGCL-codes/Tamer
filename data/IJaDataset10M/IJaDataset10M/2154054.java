package net.sf.jbob.core;

import java.util.Collection;

public interface Repository {

    public void add(Object o);

    public void remove(Object o);

    public void update(Object o);

    public Collection list(Class c);

    public Collection list(String consulta, Object[] params);
}
