package org.columba.ristretto.auth;

import java.util.List;
import org.columba.ristretto.auth.mechanism.PlainMechanism;
import org.junit.Assert;
import org.junit.Test;

public class AuthenticationFactoryTest {

    @Test
    public void testSupports() {
        Assert.assertEquals(AuthenticationFactory.getInstance().isSupported("PLAIN"), true);
        Assert.assertEquals(AuthenticationFactory.getInstance().isSupported("XYZ"), false);
    }

    @Test
    public void testGetSupportedMechanisms() {
        List mechanisms = AuthenticationFactory.getInstance().getSupportedMechanisms();
        Assert.assertNotNull(mechanisms);
        Assert.assertEquals(mechanisms.size() >= 2, true);
    }

    @Test
    public void testGetSecurestMechanism() {
        try {
            Assert.assertEquals(AuthenticationFactory.getInstance().getSecurestMethod("AUTH LOGIN PLAIN"), "LOGIN");
        } catch (NoSuchAuthenticationException e) {
            Assert.fail(e.getMessage());
        }
        try {
            Assert.assertEquals(AuthenticationFactory.getInstance().getSecurestMethod("AUTH XYZ PLAIN"), "PLAIN");
        } catch (NoSuchAuthenticationException e) {
            Assert.fail(e.getMessage());
        }
        try {
            AuthenticationFactory.getInstance().getSecurestMethod("AUTH XYZ UVW");
            Assert.fail();
        } catch (NoSuchAuthenticationException e) {
            Assert.assertEquals(e.getMessage(), "AUTH XYZ UVW");
        }
    }

    @Test
    public void testGetAuthenticationMethod() {
        try {
            Assert.assertTrue(AuthenticationFactory.getInstance().getAuthentication("PLAIN") instanceof PlainMechanism);
        } catch (NoSuchAuthenticationException e) {
            Assert.fail(e.getMessage());
        }
        try {
            AuthenticationFactory.getInstance().getAuthentication("XYZ");
            Assert.fail();
        } catch (NoSuchAuthenticationException e) {
            Assert.assertEquals(e.getMessage(), "XYZ");
        }
    }
}
