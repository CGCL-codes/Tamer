package com.psm.core.plugins.test.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.view.json.JsonStringWriter;
import org.springframework.web.servlet.view.json.JsonWriterConfiguratorTemplateRegistry;
import org.springframework.web.servlet.view.json.writer.sojo.SojoJsonStringWriter;

public class JsonTest extends TestCase {

    public void test() {
        MenuItem item = new MenuItem(new IInfoMock());
        Map map = new HashMap();
        map.put("lista", item);
        JsonStringWriter x = new SojoJsonStringWriter();
        Writer sw = new StringWriter();
        MockHttpServletRequest req = new MockHttpServletRequest();
        try {
            x.convertAndWrite(map, JsonWriterConfiguratorTemplateRegistry.load(req), sw, null);
            System.out.println(sw.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
