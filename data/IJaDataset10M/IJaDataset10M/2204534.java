package tests.an.xacml;

import static org.junit.Assert.*;
import org.junit.Test;
import an.xacml.DefaultXACMLElement;
import an.xacml.Version;

public class DefaultElementTest {

    @Test
    public void testGetVersion() {
        String expected = "0.1.0, XACML v2";
        DefaultXACMLElement elem = new DefaultXACMLElement() {
        };
        Version v = elem.getElementVersion();
        String strV = v.getVersion();
        int element = v.getXACMLVersion();
        assertTrue(strV.equals(expected));
        assertTrue(element == 2);
    }

    @Test
    public void testGetVersionFromManifest() {
    }
}
