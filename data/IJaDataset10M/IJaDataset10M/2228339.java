package com.akunagroup.uk.postcode;

import java.util.HashMap;

/**
 * Interface for Address Lookup Web Service.
 * http://sourceforge.net/tracker/index.php?func=detail&aid=1741222&group_id=176962&atid=879335
 * The Address lookup class interface
 */
public interface AddressLookupInterface {

    public int lookupPostcode(String postcode);

    public void setPassword(String password);

    public void setClientID(String clientID);

    public void setServerUrl(String serverUrl);

    public HashMap<String, Object> getAddressData();

    public AddressLookupInterface newInstance();
}
