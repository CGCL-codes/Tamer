package bmaso.purest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import static bmaso.purest.UriUtils.segmentMatrixPatameterNamesAndValues;

public class UriToolsMatrixParametersTest extends TestCase {

    public void testExtractSingleMatrixParameter() throws Exception {
        Map<String, String> params = segmentMatrixPatameterNamesAndValues("/static;mp=value");
        assertEquals("matrix params", Collections.singletonMap("mp", "value"), params);
    }

    public void testExtractMultipleMatrixParameters() throws Exception {
        Map<String, String> params = segmentMatrixPatameterNamesAndValues("/static;mp1=v1;mp2=v2;mp3=v3");
        Map<String, String> expected = new HashMap<String, String>();
        expected.putAll(Collections.singletonMap("mp1", "v1"));
        expected.putAll(Collections.singletonMap("mp2", "v2"));
        expected.putAll(Collections.singletonMap("mp3", "v3"));
        assertEquals("matrix params", expected, params);
    }

    public void testMatrixParameterNamesAndValuesAreUrlDecoded() throws Exception {
        Map<String, String> params = segmentMatrixPatameterNamesAndValues("/static;%7Bmp1%7D=v1;mp2=%7Bv2%7D;%7Bmp3%7D=%7Bv3%7D");
        Map<String, String> expected = new HashMap<String, String>();
        expected.putAll(Collections.singletonMap("{mp1}", "v1"));
        expected.putAll(Collections.singletonMap("mp2", "{v2}"));
        expected.putAll(Collections.singletonMap("{mp3}", "{v3}"));
        assertEquals("matrix params", expected, params);
    }
}
