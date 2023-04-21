package br.edu.fasete.javaroad.model;

import java.util.Iterator;

public class SuperClassIterator implements Iterator<EntityDescriptor> {

    private EntityDescriptor classe;

    public SuperClassIterator(EntityDescriptor classe) {
        this.classe = classe;
    }

    public boolean hasNext() {
        return (classe.getSuperClass() != null);
    }

    public EntityDescriptor next() {
        classe = classe.getSuperClass();
        return classe;
    }

    public void remove() {
    }
}
