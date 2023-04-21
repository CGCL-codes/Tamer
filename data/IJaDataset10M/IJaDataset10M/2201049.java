package org.openuss.web.security;

import org.apache.log4j.Logger;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.openuss.security.UserImpl;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * 
 * @author Ingo Dueppe
 *
 */
public class PasswordEncoderTest extends AbstractDependencyInjectionSpringContextTests {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(PasswordEncoderTest.class);

    private PasswordEncoder passwordEncoder;

    private SaltSource saltSource;

    public void testPasswordEncoding() {
        UserImpl user = new UserImpl();
        user.setId(-10L);
        String hash = passwordEncoder.encodePassword("masterkey", saltSource.getSalt(user));
        assertEquals("LjMlrJI4ae9Jvdz2mKs0DA==", hash);
        logger.info("password hash is \"" + hash + "\"");
        user.setId(-11L);
        hash = passwordEncoder.encodePassword("UNKNOWN", saltSource.getSalt(user));
        assertEquals("JVTGVQDCb//31Je2jKDaqg==", hash);
        logger.info("password hash is \"" + hash + "\"");
    }

    protected String[] getConfigLocations() {
        return new String[] { "classpath*:test-security.xml" };
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }
}
