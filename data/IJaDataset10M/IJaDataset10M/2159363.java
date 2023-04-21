package org.middleheaven.global.address;

import java.text.Format;

public interface AddressModel {

    /**
	 * 
	 * @return <code>true</code> if this model supports postal code.<code>false</code> otherwise.
	 */
    public boolean supportsPostalCode();

    public Format getPostalCodeFormat();
}
