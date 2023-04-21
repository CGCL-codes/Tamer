package org.racsor.flex.connect;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.racsor.jmeter.flex.messaging.util.UtilsFlexMessage;
import flex.messaging.io.amf.client.AMFConnection;
import flex.messaging.io.amf.client.exceptions.ClientStatusException;
import flex.messaging.io.amf.client.exceptions.ServerStatusException;

/**
 * A simple test that creates a sample AMF request from an xml file and then
 * pipes the output back into the AMF MessageDeserializer.
 * <p/>
 * A metric for the current build is recorded as "AMF Deserialization Time" in
 * milliseconds.
 * </p>
 * 
 * @author Peter Farland
 */
public class CC_connectTest extends TestCase {

    public CC_connectTest(String name) {
        super(name);
    }

    protected void setUp() {
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(CC_connectTest.class);
    }

    public void testMultiple() throws Exception {
        for (int i = 0; i < 1; i++) {
            _testDoJobAndCollectMoneyFairview();
        }
    }

    public void _testDoJobAndCollectMoneyFairview() throws Exception {
        HttpURLConnection connection = null;
        File docSerializeMoney = new File("C:\\TIC_LOCAL\\EclipseProjects\\OpenSourceRacsor\\cc_request\\jmeter\\pickupItemMoney.binary");
        File docSerializeCollection = new File("C:\\TIC_LOCAL\\EclipseProjects\\OpenSourceRacsor\\cc_request\\jmeter\\pickupItemCollection.binary");
        File doc = new File("C:\\TIC_LOCAL\\EclipseProjects\\OpenSourceRacsor\\cc_request\\jmeter\\doJob_car_Fairview.binary");
        byte[] theFile = FileUtils.readFileToByteArray(doc);
        String urlString = "http://ct-google.crimecitygame.com/ct-google/index.php/amf_gateway?f=104995124568475809013&r=43&t=none";
        String contentType = "application/x-amf";
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(theFile);
        wr.flush();
        wr.close();
        System.out.println("responsecode:" + connection.getResponseCode());
        InputStream is = connection.getInputStream();
        UtilsFlexMessage utilsFlex = new UtilsFlexMessage();
        utilsFlex.parseInputStream(is);
        String content = utilsFlex.messageToXML();
        System.out.println(content);
        String pickupItem = "pickup_item:" + StringUtils.substringBetween(content, "<string>pickup_item:", "</string>");
        System.out.println(pickupItem);
        String oldPickupItem = "pickup_item:276698290864898721:917:dn34ppe0ireh";
        theFile = FileUtils.readFileToByteArray(docSerializeMoney);
        ByteArrayInputStream bais = new ByteArrayInputStream(theFile);
        DataInputStream din = new DataInputStream(bais);
        utilsFlex = new UtilsFlexMessage();
        utilsFlex.parseInputStream(din);
        String update = utilsFlex.updateFlexMessage(oldPickupItem, pickupItem);
        byte[] newdataPickupItem = utilsFlex.serializeMessage(update);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        wr = new DataOutputStream(connection.getOutputStream());
        wr.write(newdataPickupItem);
        wr.flush();
        wr.close();
        System.out.println("responsecode:" + connection.getResponseCode());
        int temp = StringUtils.indexOf(content, "<string>collection_loot_item_id</string>\n                  <null/>");
        System.out.println(temp);
        if (temp != -1) {
            theFile = FileUtils.readFileToByteArray(docSerializeCollection);
            bais = new ByteArrayInputStream(theFile);
            din = new DataInputStream(bais);
            utilsFlex = new UtilsFlexMessage();
            utilsFlex.parseInputStream(din);
            update = utilsFlex.updateFlexMessage(oldPickupItem, pickupItem);
            newdataPickupItem = utilsFlex.serializeMessage(update);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            wr = new DataOutputStream(connection.getOutputStream());
            wr.write(newdataPickupItem);
            wr.flush();
            wr.close();
            System.out.println("responsecode:" + connection.getResponseCode());
        }
    }

    public void _testUrlConnectionCC() throws Exception {
        HttpURLConnection connection = null;
        File doc = new File("C:\\TIC_LOCAL\\EclipseProjects\\OpenSourceRacsor\\cc_request\\jmeter\\doJob_car_Fairview.binary");
        byte[] theFile = FileUtils.readFileToByteArray(doc);
        String urlString = "http://ct-google.crimecitygame.com/ct-google/index.php/amf_gateway?f=104995124568475809013&r=43&t=none";
        String content = "application/x-amf";
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Type", content);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(theFile);
        wr.flush();
        wr.close();
        System.out.println("responsecode:" + connection.getResponseCode());
        InputStream is = connection.getInputStream();
        UtilsFlexMessage deserializer = new UtilsFlexMessage();
        deserializer.parseInputStream(is);
    }

    public void _testConnectionCC() {
        try {
            AMFConnection amfConnection = new AMFConnection();
            String url = "http://ct-google.crimecitygame.com/ct-google/index.php/amf_gateway?f=104995124568475809013&r=43&t=none";
            String service = "BatchController.call";
            try {
                amfConnection.connect(url);
            } catch (ClientStatusException cse) {
                System.out.println(cse);
                return;
            }
            try {
                Object result = amfConnection.call(service);
                System.out.println("results: " + result.toString());
            } catch (ClientStatusException cse) {
                System.out.println(cse);
            } catch (ServerStatusException sse) {
                System.out.println(sse);
            }
            amfConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
