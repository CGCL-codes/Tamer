package gnu.javax.crypto.jce.mac;

import gnu.java.security.Registry;

public class OMacTripleDESImpl extends MacAdapter {

    public OMacTripleDESImpl() {
        super(Registry.OMAC_PREFIX + Registry.TRIPLEDES_CIPHER);
    }
}
