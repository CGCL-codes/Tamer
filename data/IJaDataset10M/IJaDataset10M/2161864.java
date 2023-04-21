package slevnik;

/**
 *
 * @author Eduard Krak, Martin Penak
 */
public class CategoryImpl implements Category {

    private int id;

    private String name;

    public CategoryImpl() {
    }

    public CategoryImpl(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CategoryImpl other = (CategoryImpl) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.id;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
