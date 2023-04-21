package test.openmobster.core.security.device;

import org.apache.log4j.Logger;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openmobster.core.common.ServiceManager;
import org.openmobster.core.security.device.DeviceController;
import org.openmobster.core.security.device.Device;
import org.openmobster.core.security.device.DeviceAttribute;
import org.openmobster.core.security.identity.Identity;
import org.openmobster.core.security.identity.IdentityController;

/**
 * @author openmobster@gmail.com
 */
public class TestDeviceController extends TestCase {

    private static Logger log = Logger.getLogger(TestDeviceController.class);

    private DeviceController deviceController = null;

    private IdentityController identityController = null;

    /**
	 * 
	 */
    public void setUp() {
        ServiceManager.bootstrap();
        this.deviceController = (DeviceController) ServiceManager.locate("security://test-suite/DeviceController");
        this.identityController = (IdentityController) ServiceManager.locate("security://test-suite/IdentityController");
        this.identityController.create(new Identity("blah@gmail.com", ""));
    }

    /**
	 * 
	 */
    public void tearDown() {
        ServiceManager.shutdown();
        this.deviceController = null;
    }

    public void testCreate() throws Exception {
        String imei = "IMEI:4930051";
        assertNotNull(this.deviceController);
        Device stored = this.deviceController.read(imei);
        assertNull(stored);
        Device device = new Device(imei, this.identityController.read("blah@gmail.com"));
        Set<DeviceAttribute> attributes = new HashSet<DeviceAttribute>();
        attributes.add(new DeviceAttribute("email", "blah@gmail.com"));
        device.setAttributes(attributes);
        this.deviceController.create(device);
        stored = this.deviceController.read(imei);
        assertNotNull(stored);
        assertEquals("IMEI does not match", imei, stored.getIdentifier());
        assertNotNull("Device Attributes Not Found", stored.getAttributes());
        DeviceAttribute storedAttribute = stored.getAttributes().iterator().next();
        assertEquals("Email Attribute Not Found", "email", storedAttribute.getName());
        assertEquals("Email Value does not match", "blah@gmail.com", storedAttribute.getValue());
        assertTrue("Improper ID assigned", storedAttribute.getId() > 0);
        assertNotNull("Identity not associated", stored.getIdentity());
        assertEquals("Proper Identity Not Associated", stored.getIdentity().getPrincipal(), "blah@gmail.com");
    }

    public void testUpdateDeviceOnly() throws Exception {
        String imei = "IMEI:4930051";
        String updatedImei = imei + "/updated";
        this.testCreate();
        Device stored = this.deviceController.read(imei);
        stored.setIdentifier(updatedImei);
        this.deviceController.update(stored);
        Device mustNotExist = this.deviceController.read(imei);
        assertNull("Old Identity must not exist", mustNotExist);
        stored = this.deviceController.read(updatedImei);
        assertNotNull(stored);
        assertEquals("IMEI does not match", updatedImei, stored.getIdentifier());
        assertNotNull("Device Attributes Not Found", stored.getAttributes());
        DeviceAttribute storedAttribute = stored.getAttributes().iterator().next();
        assertEquals("Email Attribute Not Found", "email", storedAttribute.getName());
        assertEquals("Email Value does not match", "blah@gmail.com", storedAttribute.getValue());
        assertTrue("Improper ID assigned", storedAttribute.getId() > 0);
        assertNotNull("Identity not associated", stored.getIdentity());
        assertEquals("Proper Identity Not Associated", stored.getIdentity().getPrincipal(), "blah@gmail.com");
    }

    public void testUpdateDeviceAttributes() throws Exception {
        String imei = "IMEI:4930051";
        assertNotNull(this.deviceController);
        Device stored = this.deviceController.read(imei);
        assertNull(stored);
        Device device = new Device(imei, this.identityController.read("blah@gmail.com"));
        Set<DeviceAttribute> attributes = new HashSet<DeviceAttribute>();
        attributes.add(new DeviceAttribute("email", "blah@gmail.com"));
        attributes.add(new DeviceAttribute("sms", "8675309"));
        device.setAttributes(attributes);
        this.deviceController.create(device);
        stored = this.deviceController.read(imei);
        assertNotNull(stored);
        assertEquals("IMEI does not match", imei, stored.getIdentifier());
        assertTrue("Improper ID assigned", stored.getId() > 0);
        stored.addAttribute(new DeviceAttribute("phone", "911"));
        stored.removeAttribute(new DeviceAttribute("sms", "8675309"));
        stored.updateAttribute(new DeviceAttribute("email", "blah/updated@gmail.com"));
        this.deviceController.update(stored);
        stored = this.deviceController.read(imei);
        attributes = stored.getAttributes();
        log.info("-------------------------------------------------");
        for (DeviceAttribute attribute : attributes) {
            log.info("Id=" + attribute.getId() + ", Name=" + attribute.getName() + ", Value=" + attribute.getValue());
        }
        assertEquals("Phone Attribute Failed", "911", stored.readAttribute("phone").getValue());
        assertEquals("Email Attribute Failed", "blah/updated@gmail.com", stored.readAttribute("email").getValue());
        assertNull("SMS Delete Attribute Failed", stored.readAttribute("sms"));
        Session session = null;
        Transaction tx = null;
        boolean orphanDeleted = true;
        try {
            session = this.deviceController.getHibernateManager().getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            String query = "from DeviceAttribute";
            List allAttributes = session.createQuery(query).list();
            log.info("-------------------------------------------------");
            for (int i = 0; i < allAttributes.size(); i++) {
                DeviceAttribute cour = (DeviceAttribute) allAttributes.get(i);
                log.info("Id=" + cour.getId() + ", Name=" + cour.getName() + ", Value=" + cour.getValue());
                if (cour.getName().equals("sms")) {
                    orphanDeleted = false;
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        assertTrue("Orphan Deletion failed", orphanDeleted);
    }

    public void testDelete() throws Exception {
        String imei = "IMEI:4930051";
        this.testCreate();
        this.deviceController.delete(this.deviceController.read(imei));
        Device stored = this.deviceController.read(imei);
        assertNull("Device Deletion Failed", stored);
    }

    public void testDeleteFromDeviceToIdentity() throws Exception {
        String imei = "IMEI:4930051";
        this.testCreate();
        this.deviceController.delete(this.deviceController.read(imei));
        Device stored = this.deviceController.read(imei);
        assertNull("Device Deletion Failed", stored);
        Identity storedIdentity = this.identityController.read("blah@gmail.com");
        assertNotNull("If Device is Deleted, Identity should not be deleted", storedIdentity);
    }
}
