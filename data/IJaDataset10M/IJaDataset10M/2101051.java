package test.openmobster.device.comet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import org.openmobster.cloud.api.push.PushService;
import org.openmobster.core.security.device.Device;
import org.openmobster.core.security.device.PushApp;
import org.openmobster.core.security.device.PushAppController;

/**
 * Usecase being tested:
 * 
 * A device is activated but not connected
 * The twitterChannel should have messages in queue while its offline
 * The device waits for a bit and then connects to the network
 * 
 * Expected:
 * The comet messages waiting in queue for this device must be delivered
 * 
 * 
 * @author openmobster@gmail.com
 */
public class IntegTestIPhonePush extends AbstractCometTest {

    public void setUp() throws Exception {
        super.setUp();
        PushApp app1 = new PushApp();
        app1.setAppId("app1");
        app1.addDevice("IMEI:12345");
        app1.addChannel("twitterChannel");
        app1.setCertificatePassword("password");
        app1.setCertificate(this.getProductionCertificate());
        PushAppController.getInstance().create(app1);
        PushApp app2 = new PushApp();
        app2.setAppId("app2");
        app2.addDevice("IMEI:67890");
        app2.addChannel("mockMobileTicket");
        PushAppController.getInstance().create(app2);
    }

    public void test() throws Exception {
        synchronized (this) {
            this.device_12345.activateDevice();
            this.registerDeviceType("IMEI:12345", "iphone", "ec0f1e1c 632411ab 0312747f 589db653 420a82dc 8cea7dc4 89b5c69c bd37d8aa");
            Device device = this.findDevice("IMEI:12345");
            PushService pushService = PushService.getInstance();
            pushService.push(device.getIdentity().getPrincipal(), "app1", "Hello From Push", "Title", "Details");
            wait();
        }
    }

    private byte[] getCertificate() throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream("/Users/openmobster/desktop/aps_dev.p12");
            bos = new ByteArrayOutputStream();
            int readByte;
            while ((readByte = fis.read()) != -1) {
                bos.write(readByte);
            }
            return bos.toByteArray();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }

    private byte[] getProductionCertificate() throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream("/Users/openmobster/desktop/aps_prod.p12");
            bos = new ByteArrayOutputStream();
            int readByte;
            while ((readByte = fis.read()) != -1) {
                bos.write(readByte);
            }
            return bos.toByteArray();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
}
