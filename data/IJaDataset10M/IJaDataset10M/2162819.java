package xades4j.verification;

import org.junit.Test;
import static org.junit.Assert.*;
import xades4j.utils.XadesProfileResolutionException;

/**
 *
 * @author Luís
 */
public class XadesVerificationProfileTest {

    @Test
    public void testGetVerifier() throws XadesProfileResolutionException {
        System.out.println("getVerifier");
        XadesVerificationProfile instance = new XadesVerificationProfile(VerifierTestBase.validationProviderMySigs);
        XadesVerifier result = instance.newVerifier();
        assertNotNull(result);
    }
}
