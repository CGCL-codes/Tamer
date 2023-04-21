package org.iptc.ines.manager.catalog;

public class CatalogIntegrityException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8029320513118012576L;

    private String m_alias;

    public CatalogIntegrityException(String alias) {
        super();
        m_alias = alias;
    }

    @Override
    public String getMessage() {
        return m_alias + " have duplicate definition";
    }
}
