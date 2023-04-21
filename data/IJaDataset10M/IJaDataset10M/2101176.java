package com.jxva.license;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2008-12-01 11:18:02 by Jxva
 */
public class DevelopmentLicense extends AbstractLicense {

    public DevelopmentLicense(Configure configure) {
        super(configure);
    }

    public boolean isValid() {
        return super.isValid() && getServerAddr().equals(Network.DEFAULT_HOST_ADDRESS);
    }
}
