package xades4j.production;

import com.google.inject.Inject;
import java.security.ProviderException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xades4j.algorithms.Algorithm;
import xades4j.algorithms.ExclusiveCanonicalXMLWithoutComments;
import xades4j.properties.SignaturePolicyBase;
import xades4j.properties.SignaturePolicyImpliedProperty;
import xades4j.providers.MessageDigestEngineProvider;
import xades4j.providers.SignaturePolicyInfoProvider;
import xades4j.providers.impl.DefaultAlgorithmsProviderEx;
import xades4j.providers.impl.DefaultTimeStampTokenProvider;
import xades4j.providers.impl.FirstCertificateSelector;
import xades4j.providers.impl.PKCS11KeyStoreKeyingDataProvider;

/**
 *
 * @author Luís
 */
public class SignerTTest extends SignerTestBase {

    static class TestTimeStampTokenProvider extends DefaultTimeStampTokenProvider {

        @Inject
        public TestTimeStampTokenProvider(MessageDigestEngineProvider messageDigestProvider) {
            super(messageDigestProvider);
        }

        @Override
        protected String getTSAUrl() {
            return "http://tsa.starfieldtech.com/";
        }
    }

    static class ExclusiveC14nForTimeStampsAlgorithmsProvider extends DefaultAlgorithmsProviderEx {

        @Override
        public Algorithm getCanonicalizationAlgorithmForTimeStampProperties() {
            return new ExclusiveCanonicalXMLWithoutComments("ds", "xades");
        }

        @Override
        public Algorithm getCanonicalizationAlgorithmForSignature() {
            return new ExclusiveCanonicalXMLWithoutComments();
        }
    }

    @Test
    public void testSignTExclusiveC14NWithoutPolicy() throws Exception {
        System.out.println("signTExclusiveC14NWithoutPolicy");
        Document doc = getTestDocument();
        Element elemToSign = doc.getDocumentElement();
        SignerT signer = (SignerT) new XadesTSigningProfile(keyingProviderMy).withTimeStampTokenProvider(TestTimeStampTokenProvider.class).withAlgorithmsProviderEx(ExclusiveC14nForTimeStampsAlgorithmsProvider.class).newSigner();
        new Enveloped(signer).sign(elemToSign);
        outputDocument(doc, "document.signed.t.bes.xml");
    }

    @Test
    public void testSignTWithPolicy() throws Exception {
        System.out.println("signTWithPolicy");
        Document doc = getTestDocument();
        Element elemToSign = doc.getDocumentElement();
        SignerT signer = (SignerT) new XadesTSigningProfile(keyingProviderMy).withPolicyProvider(new SignaturePolicyInfoProvider() {

            @Override
            public SignaturePolicyBase getSignaturePolicy() {
                return new SignaturePolicyImpliedProperty();
            }
        }).newSigner();
        new Enveloped(signer).sign(elemToSign);
        outputDocument(doc, "document.signed.t.epes.xml");
    }

    @Test
    public void testSignTPtCC() throws Exception {
        System.out.println("signTPtCitizenCard");
        if (!onWindowsPlatform()) {
            fail("Test written for the Windows platform");
        }
        Document doc = getTestDocument();
        Element elemToSign = doc.getDocumentElement();
        try {
            PKCS11KeyStoreKeyingDataProvider ptccKeyingDataProv = new PKCS11KeyStoreKeyingDataProvider("C:\\Windows\\System32\\pteidpkcs11.dll", "PT_CC", new FirstCertificateSelector(), null, null, false);
            SignerT signer = (SignerT) new XadesTSigningProfile(ptccKeyingDataProv).withAlgorithmsProviderEx(PtCcAlgorithmsProvider.class).newSigner();
            new Enveloped(signer).sign(elemToSign);
            outputDocument(doc, "document.signed.t.bes.ptcc.xml");
        } catch (ProviderException ex) {
            fail("PT CC PKCS#11 provider not configured");
        }
    }
}
