package br.com.jnfe.base.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.security.KeyStore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author mauriciofernandesdecastro
 */
public class SecurityUtilsTests {

    @Test
    public void openStore() throws Exception {
        KeyStore keyStore = SecurityUtils.openStore(new ClassPathResource("testStore"), "123456".toCharArray());
        assertTrue(keyStore.containsAlias("teste"));
    }

    @Test
    public void openTrustStore() throws Exception {
        KeyStore trustStore = SecurityUtils.openTrustStore("changeit".toCharArray());
        assertNotNull(trustStore);
    }
}
