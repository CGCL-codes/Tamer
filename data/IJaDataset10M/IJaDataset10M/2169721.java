package gnu.java.security.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public final class CollectionCertStoreImpl extends CertStoreSpi {

    private final Collection store;

    public CollectionCertStoreImpl(CertStoreParameters params) throws InvalidAlgorithmParameterException {
        super(params);
        if (!(params instanceof CollectionCertStoreParameters)) throw new InvalidAlgorithmParameterException("not a CollectionCertStoreParameters object");
        store = ((CollectionCertStoreParameters) params).getCollection();
    }

    public Collection engineGetCertificates(CertSelector selector) throws CertStoreException {
        LinkedList result = new LinkedList();
        for (Iterator it = store.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if ((o instanceof Certificate) && selector.match((Certificate) o)) result.add(o);
        }
        return result;
    }

    public Collection engineGetCRLs(CRLSelector selector) throws CertStoreException {
        LinkedList result = new LinkedList();
        for (Iterator it = store.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if ((o instanceof CRL) && selector.match((CRL) o)) result.add(o);
        }
        return result;
    }
}
