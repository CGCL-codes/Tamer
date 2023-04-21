package br.gov.framework.demoiselle.web.security;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WebSecurityExceptionTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWebSecurityException() {
        try {
            throw new WebSecurityException();
        } catch (Exception e) {
            assertTrue(e instanceof WebSecurityException);
        }
    }

    @Test
    public void testWebSecurityExceptionString() {
        try {
            throw new WebSecurityException("Teste");
        } catch (Exception e) {
            assertEquals("Teste", e.getMessage());
        }
    }

    @Test
    public void testWebSecurityExceptionStringThrowable() {
        try {
            Throwable cause = createMock(Throwable.class);
            replay(cause);
            throw new WebSecurityException("Teste", cause);
        } catch (Exception e) {
            assertEquals("Teste", e.getMessage());
        }
    }
}
