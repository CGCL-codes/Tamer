package es.caib.signatura.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertStoreException;
import javax.mail.MessagingException;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.x509.NoSuchStoreException;
import es.caib.signatura.api.SMIMEParser;
import es.caib.signatura.api.Signature;
import es.caib.signatura.api.SignatureDataException;
import es.caib.signatura.api.SignatureException;

/**
 * Proxy to access to the S/MIME parser implementation.
 * 
 * @author u91940
 *
 */
public class SMIMEParserProxy implements SMIMEParser {

    /**
	 * S/MIME parser implementation reference
	 */
    SMIMEParser impl = null;

    /**
	 * Parser factory.
	 */
    SMIMEParser smimeFactory = null;

    /**
	 * 
	 * 
	 * @param smime
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws SignatureException
	 */
    public SMIMEParserProxy(InputStream smime) throws InstantiationException, IllegalAccessException, IOException, SignatureException {
        ClassLoaderFactory loaderFactory = ClassLoaderFactory.getFactory();
        try {
            smimeFactory = (SMIMEParser) loaderFactory.getMasterClassLoader().loadClass("es.caib.signatura.provider.impl.common.SMIMEImpl").newInstance();
            impl = smimeFactory.getInstance(smime);
        } catch (SignatureDataException e) {
            throw new SignatureException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new SignatureException(e.getMessage(), e);
        }
    }

    public SMIMEParser getInstance(InputStream smime) throws InstantiationException, IllegalAccessException, IOException, SignatureException {
        return new SMIMEParserProxy(smime);
    }

    public Signature[] getSignatures() {
        if (impl != null) return impl.getSignatures(); else return null;
    }

    public void parse(InputStream mimeIS) throws IOException, InstantiationException, IllegalAccessException, SignatureException {
        impl = smimeFactory.getInstance(mimeIS);
    }

    public Object getSignedObject() {
        if (impl != null) return impl.getSignedObject(); else return null;
    }
}
