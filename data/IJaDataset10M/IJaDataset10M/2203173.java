package org.apache.harmony.security.tests.support.cert;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.util.Collection;

/**
 * Additional class for verification CertStoreSpi
 * and CertStore
 * 
 */
public class MyCertStoreSpi extends CertStoreSpi {

    public MyCertStoreSpi(CertStoreParameters params) throws InvalidAlgorithmParameterException {
        super(params);
        if (!(params instanceof MyCertStoreParameters)) {
            throw new InvalidAlgorithmParameterException("Invalid params");
        }
    }

    public Collection engineGetCertificates(CertSelector selector) throws CertStoreException {
        if (selector == null) {
            throw new CertStoreException("Parameter is null");
        }
        return null;
    }

    public Collection engineGetCRLs(CRLSelector selector) throws CertStoreException {
        if (selector == null) {
            throw new CertStoreException("Parameter is null");
        }
        return null;
    }
}
