package org.dcw.lgt.dao.configuration;

import org.dcw.lgt.dao.configuration.base.BaseConfiguration;

public class Configuration extends BaseConfiguration {

    private static final long serialVersionUID = 1L;

    public Configuration() {
        super();
    }

    /**
	 * Constructor for primary key
	 */
    public Configuration(java.lang.String id) {
        super(id);
    }

    /**
	 * Constructor for required fields
	 */
    public Configuration(java.lang.String id, java.lang.String value) {
        super(id, value);
    }
}
