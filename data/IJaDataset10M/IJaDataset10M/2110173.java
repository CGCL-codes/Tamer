package org.jnbt;

public interface Searchable {

    public Tag<?> search(String name);

    public Tag<?> search(Object value);
}
