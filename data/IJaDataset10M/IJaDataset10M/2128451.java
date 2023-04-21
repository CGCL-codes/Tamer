package org.kompiro.readviewer.ui.store;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.kompiro.readviewer.service.INotificationService;

public class SaxStreamWriterTest extends TestCase {

    public void testWriter() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream stream = new BufferedOutputStream(baos);
        SaxStreamWriter writer = new SaxStreamWriter(stream);
        List<INotificationService> services = new ArrayList<INotificationService>();
        services.add(new INotificationService.Mock() {

            @Override
            public String getServiceName() {
                return "Fly me to the Ganymede";
            }

            @Override
            public String getType() {
                return "RSS";
            }

            @Override
            public Map<String, String> getProperties() {
                Map<String, String> properties = new HashMap<String, String>();
                properties.put("URL", "http://d.hatena.ne.jp/kompiro/rss2");
                properties.put("UserName", "");
                properties.put("Password", "");
                properties.put("wsse", "false");
                return properties;
            }
        });
        writer.write(services);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><SERVICES><SERVICE TYPE=\"RSS\" Password=\"\" UserName=\"\" URL=\"http://d.hatena.ne.jp/kompiro/rss2\" wsse=\"false\"/></SERVICES>", baos.toString());
    }
}
