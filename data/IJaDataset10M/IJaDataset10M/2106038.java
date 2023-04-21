package nz.ac.massey.softwarec.group3.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author wanting
 */
public class GetDataInterfaceTest {

    public GetDataInterfaceTest() {
    }

    HttpServletRequest request;

    HttpServletResponse response;

    GetDataInterface instance;

    @Before
    public void setUp() {
        request = null;
        response = null;
        instance = new GetDataInterfaceImpl();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of processRequest method, of class GetDataInterface.
     */
    @Test
    public void testProcessRequest() {
        System.out.println("processRequest");
        instance.processRequest(request, response);
    }

    /**
     * Test of doGet method, of class GetDataInterface.
     */
    @Test
    public void testDoGet() {
        System.out.println("doGet");
        instance.doGet(request, response);
    }

    /**
     * Test of doPost method, of class GetDataInterface.
     */
    @Test
    public void testDoPost() {
        System.out.println("doPost");
        instance.doPost(request, response);
    }

    /**
     * Test of getServletInfo method, of class GetDataInterface.
     */
    @Test
    public void testGetServletInfo() {
        System.out.println("getServletInfo");
        String expResult = "";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
    }

    public class GetDataInterfaceImpl implements GetDataInterface {

        public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response) {
        }

        public void doPost(HttpServletRequest request, HttpServletResponse response) {
        }

        public String getServletInfo() {
            return "";
        }
    }
}
