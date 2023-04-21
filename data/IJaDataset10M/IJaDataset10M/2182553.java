package easyaccept.test.resources;

import java.io.InputStream;
import java.net.URL;

public class TestResourceUtil {

    public static InputStream getResourceAsStream(String name) {
        return TestResourceUtil.class.getResourceAsStream(name);
    }

    public static URL getResource(String name) {
        return TestResourceUtil.class.getResource(name);
    }
}
