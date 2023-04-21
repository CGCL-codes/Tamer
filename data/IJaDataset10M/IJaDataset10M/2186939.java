package org.sqlsplatter.tinyhorror.other.structures;

/**
 * Lightweight structure containing a name and optionally its alias. 
 * 
 * @author Saverio Miroddi
 */
public class AliasableIdent {

    public final String name, alias;

    /**
	 * @param alias nullable.
	 */
    public AliasableIdent(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    /** For idents without alias */
    public AliasableIdent(String name) {
        this(name, null);
    }

    public String getResolvedName() {
        return (alias == null) ? name : alias;
    }

    @Override
    public String toString() {
        return name + (alias == null ? "" : ('[' + alias + ']'));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alias == null) ? 0 : alias.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof AliasableIdent)) return false;
        final AliasableIdent other = (AliasableIdent) obj;
        if (alias == null) {
            if (other.alias != null) {
                return false;
            }
        } else if (!alias.equals(other.alias)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
