package cz.muni.fi.rum.sender.command.Factory;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pmikulasek
 */
public class CommandFactoryImplTest {

    public CommandFactoryImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createRequest method, of class CommandFactoryImpl.
     */
    @Test
    public void testCreateRequest() throws Exception {
        System.out.println("createRequest");
        Map<String, String> par = new HashMap<String, String>();
        par.put("Listener.port", "TEST");
        CommandFactory instance = CommandFactoryImpl.getInstance();
        instance.createRequest("Method.start", par);
    }

    /**
     * Test of createResponse method, of class CommandFactoryImpl.
     */
    @Test
    public void testCreateResponse() {
        System.out.println("createResponse");
    }
}
