package com.jxva.license;

/**
 * License Enterprise Edition
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2008-12-01 14:40:45 by Jxva
 */
public class EnterpriseLicense extends AbstractLicense {

    /**
	 * @param configure
	 */
    public EnterpriseLicense(Configure configure) {
        super(configure);
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
