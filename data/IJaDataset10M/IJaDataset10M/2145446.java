package com.sun.org.apache.xml.internal.security.keys.keyresolver;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import org.w3c.dom.Element;

/**
 * This class is abstract class for a child KeyInfo Elemnet.
 *
 * If you want the your KeyResolver, at firstly you must extand this class, and register
 * as following in config.xml
 * <PRE>
 *  &lt;KeyResolver URI="http://www.w3.org/2000/09/xmldsig#KeyValue"
 *   JAVACLASS="MyPackage.MyKeyValueImpl"//gt;
 * </PRE>
 *
 * @author $Author: raul $
 * @version $Revision: 1.11 $
 */
public abstract class KeyResolverSpi {

    /** {@link java.util.logging} logging facility */
    static java.util.logging.Logger log = java.util.logging.Logger.getLogger(KeyResolverSpi.class.getName());

    /**
    * This method helps the {@link com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver} to decide whether a
    * {@link com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi} is able to perform the requested action.
    *
    * @param element
    * @param BaseURI
    * @param storage
    * @return true if can resolve the key in the element
    */
    public abstract boolean engineCanResolve(Element element, String BaseURI, StorageResolver storage);

    /**
    * Method engineResolvePublicKey
    *
    * @param element
    * @param BaseURI
    * @param storage
    * @return resolved public key from the registered from the element.
    * 
    * @throws KeyResolverException
    */
    public abstract PublicKey engineResolvePublicKey(Element element, String BaseURI, StorageResolver storage) throws KeyResolverException;

    /**
    * Method engineResolveCertificate
    *
    * @param element
    * @param BaseURI
    * @param storage
    * @return resolved X509Certificate key from the registered from the elements
    *
    * @throws KeyResolverException
    */
    public abstract X509Certificate engineResolveX509Certificate(Element element, String BaseURI, StorageResolver storage) throws KeyResolverException;

    /**
    * Method engineResolveSecretKey
    *
    * @param element
    * @param BaseURI
    * @param storage
    * @return resolved SecretKey key from the registered from the elements
    *
    * @throws KeyResolverException
    */
    public abstract SecretKey engineResolveSecretKey(Element element, String BaseURI, StorageResolver storage) throws KeyResolverException;

    /** Field _properties */
    protected java.util.Map _properties = new java.util.HashMap(10);

    /**
    * Method engineSetProperty
    *
    * @param key
    * @param value
    */
    public void engineSetProperty(String key, String value) {
        java.util.Iterator i = this._properties.keySet().iterator();
        while (i.hasNext()) {
            String c = (String) i.next();
            if (c.equals(key)) {
                key = c;
                break;
            }
        }
        this._properties.put(key, value);
    }

    /**
    * Method engineGetProperty
    *
    * @param key
    * @return obtain the property appointed by key
    */
    public String engineGetProperty(String key) {
        java.util.Iterator i = this._properties.keySet().iterator();
        while (i.hasNext()) {
            String c = (String) i.next();
            if (c.equals(key)) {
                key = c;
                break;
            }
        }
        return (String) this._properties.get(key);
    }

    /**
    * Method engineGetPropertyKeys
    *
    * @return the keys of properties known by this resolver
    */
    public String[] engineGetPropertyKeys() {
        return new String[0];
    }

    /**
    * Method understandsProperty
    *
    * @param propertyToTest
    * @return true if understood the property
    */
    public boolean understandsProperty(String propertyToTest) {
        String[] understood = this.engineGetPropertyKeys();
        if (understood != null) {
            for (int i = 0; i < understood.length; i++) {
                if (understood[i].equals(propertyToTest)) {
                    return true;
                }
            }
        }
        return false;
    }
}
