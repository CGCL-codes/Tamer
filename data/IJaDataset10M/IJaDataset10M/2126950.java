package ro.cuzma.larry.persistance.common;

public class Entity<T> implements Comparable<Entity<T>> {

    protected T id;

    public Entity(T id) {
        this.id = id;
    }

    public Entity() {
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public int compareTo(Entity<T> o) {
        if (id instanceof Long) {
            return ((Long) id).compareTo((Long) o.getId());
        } else if (id instanceof String) {
            return ((String) id).compareTo((String) o.getId());
        } else {
            throw new RuntimeException("Unknown entity type.");
        }
    }
}
