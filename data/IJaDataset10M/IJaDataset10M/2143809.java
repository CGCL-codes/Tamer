package gloodb;

import java.io.Serializable;

public class SimpleSerializableEmbedded implements Serializable, Cloneable {

    private static final long serialVersionUID = -5981796444710412403L;

    @Identity
    final Serializable id;

    Embedded<Long> embeddedValue;

    private transient Repository repository;

    public SimpleSerializableEmbedded(Serializable id) {
        this.id = id;
        this.embeddedValue = new Embedded<Long>(id, 1).set(0L);
    }

    public Serializable getId() {
        return id;
    }

    public Serializable getEmbeddedId() {
        return this.embeddedValue.getId();
    }

    public Embedded<Long> getEmbeddedValue() {
        return this.embeddedValue;
    }

    public Long getValue() {
        return embeddedValue.fetch(repository).get();
    }

    public SimpleSerializableEmbedded setValue(Long value) {
        embeddedValue.set(value);
        return this;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, value: %s", id.toString(), this.embeddedValue.toString());
    }

    @PreCreate
    @PreUpdate
    void preCreateOrUpdate(Repository repository) {
        this.repository = repository;
        this.embeddedValue.flush(repository);
    }

    @PreRemove
    void postRemove(Repository repository) {
        this.embeddedValue.remove();
        this.embeddedValue.flush(repository);
    }

    @PostRestore
    void setRepository(Repository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        SimpleSerializableEmbedded copy;
        try {
            copy = (SimpleSerializableEmbedded) super.clone();
            copy.embeddedValue = (Embedded<Long>) embeddedValue.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new GlooException(e);
        }
    }
}
