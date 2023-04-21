package org.equanda.tool.shared;

/**
 * Id class (used to id threads and application )
 *
 * @author NetRom team
 */
public final class Id {

    private String id;

    public static Id buildId(String id) {
        if (id != null) return new Id(id);
        return null;
    }

    private Id(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id)) return false;
        final Id id = (Id) o;
        if (!this.id.equals(id.id)) return false;
        return true;
    }

    public int hashCode() {
        return id.hashCode();
    }
}
