package org.brainypdm.modules.datastore.daoengine.dialect;

/***
 * The DAO dialect
 * @author <a href="mailto:nico@brainypdm.org">Nico Bagari</a>
 *
 */
public class Dialect {

    /**
	 * the default dialect implementation
	 */
    public static final Dialect DEFAULT = new Dialect("DEFAULT");

    /**
	 * the property dialect name used by HB
	 */
    public static final String HB_DIALECT_PROPERTY_NAME = "datastore.dspu.hibernate.dialect";

    /**
	 * the property used by brainy
	 */
    public static final String BRAINY_DAO_DIALECT_PROPERTY_NAME = "datastore.dao.dialect";

    /**
	 * dialect name
	 */
    String name;

    public Dialect(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Dialect other = (Dialect) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
