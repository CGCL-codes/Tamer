package org.slasoi.llms.manager;

import junit.framework.TestCase;
import org.drools.builder.ResourceType;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MonitoringRequestXMLTest extends TestCase {

    @Test
    public void testApp() throws IOException {
        MonitoringRequest monReq = MonitoringRequest.createFromXML(MonitoringRequestXMLTest.readFileAsString("src/test/resources/MonitoringRequest.xml"));
        assertEquals(monReq.getMetrics().get(1).getMetricURL(), "slasoi://myManagedObject.company.com/DemoService/Tashi/192.168.1.26/fluent/cpu_num");
        assertEquals(monReq.getMetrics().get(0).getParams().get(1).getName(), "type");
        assertEquals(monReq.getMetrics().get(0).getParams().get(1).getValue(), "max");
        assertEquals(monReq.getRulePackages().get(0).getType(), ResourceType.DRL);
        assertEquals(monReq.getNotificationChannel(), "myCustomNotificationChannel");
    }

    private static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
