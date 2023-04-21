package net.sf.dpdesktop.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Heiner Reinhardt
 */
public class RequestTest {

    /**
     * Test of getUsername method, of class Request.
     */
    @Test
    public void testGetUsername() {
        Request instance = new Request();
        String expResult = "";
        String result = instance.getUsername();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUsername method, of class Request.
     */
    @Test
    public void testSetUsername() {
        String username = "";
        Request instance = new Request();
        instance.setUsername(username);
        assertEquals(username, instance.getUsername());
    }

    /**
     * Test of getPassword method, of class Request.
     */
    @Test
    public void testGetPassword() {
        Request instance = new Request();
        String expResult = "";
        String result = instance.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPassword method, of class Request.
     */
    @Test
    public void testSetPassword() {
        String password = "";
        Request instance = new Request();
        instance.setPassword(password);
        assertEquals(password, instance.getPassword());
    }

    /**
     * Test of getModule method, of class Request.
     */
    @Test
    public void testGetModule() {
        Request instance = new Request();
        RequestModule expResult = null;
        RequestModule result = instance.getModule();
        assertEquals(expResult, result);
    }

    /**
     * Test of setModule method, of class Request.
     */
    @Test
    public void testSetModule() {
        RequestModule module = RequestModule.CONTAINER;
        Request instance = new Request();
        instance.setModule(module);
        assertEquals(module, instance.getModule());
    }
}
