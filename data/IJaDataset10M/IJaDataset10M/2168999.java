package test.ararat.core;

import java.io.File;
import java.util.Vector;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import junit.framework.TestCase;
import ararat.core.LiveMessage;
import ararat.core.LiveMessageInfo;
import ararat.util.CustomDate;
import ararat.util.SystemInfo;

public class LiveMessageTest extends TestCase {

    private LiveMessage message;

    private LiveMessageInfo msgInfo;

    private String from = "a.charif@cs.ucl.ac.uk";

    private String fromName = "Arman";

    private String to = "ucabach@ucl.ac.uk";

    private String subject = "Test subject";

    private String body = "Test body";

    private String folder = "inbox";

    private int status = LiveMessageInfo.READ;

    private int year = 2004, month = 5, day = 15, hours = 22, minutes = 55;

    private CustomDate date = new CustomDate(year, month, day, hours, minutes);

    private File txtFile = new File(SystemInfo.APP_HOME + "/data/test/test.txt");

    private File wavFile = new File(SystemInfo.APP_HOME + "/data/test/test.wav");

    /** Init */
    protected void setUp() throws Exception {
        msgInfo = new LiveMessageInfo(status, date);
        message = new LiveMessage(from, to, subject, body, msgInfo);
        message.setFromName(fromName);
        message.addAttachment(txtFile);
        message.addAttachment(wavFile);
    }

    /** Destroy */
    protected void tearDown() {
        message = null;
        msgInfo = null;
    }

    /** Test getFromAddress() */
    public void testGetFromAddress() {
        assertEquals(from, message.getFromAddress().getAddress());
    }

    /** Test getFromName() */
    public void testGetFromName() {
        assertEquals(fromName, message.getFromName());
    }

    /** Test getToAddress() */
    public void testGetToAddress() {
        assertEquals(to, message.getToAddress(0).toString());
        try {
            message.getToAddress(1);
            fail("Error! Exception must have been thrown.");
        } catch (Exception e) {
        }
    }

    /** Test getSubject() */
    public void testGetSubject() {
        assertEquals(subject, message.getSubject());
    }

    /** Test getBody() */
    public void testGetBody() {
        assertEquals(body, message.getBody());
    }

    /** Test getStatus() */
    public void testGetStatus() {
        assertEquals(status, message.getStatus());
    }

    /** Test getDate() */
    public void testGetDate() {
        assertEquals(date, message.getDate());
    }

    /** Test getAttachments() */
    public void testGetAttachments() {
        Vector attachments = message.getAttachments();
        File f1 = (File) attachments.get(0);
        File f2 = (File) attachments.get(1);
        assertEquals(f1, txtFile);
        assertEquals(f2, wavFile);
    }

    /** Test getVoiceAttachments() */
    public void testGetVoiceAttachments() {
        Vector voiceAttachments = message.getVoiceAttachments();
        assertEquals(wavFile, (File) voiceAttachments.get(0));
    }

    /** Test getInfo() */
    public void testGetInfo() throws Exception {
        assertEquals(msgInfo, message.getInfo());
    }

    /** Test setFromAddress() */
    public void testSetFromAddress() throws AddressException {
        String newSender = "newSender@host.com";
        message.setFromAddress(newSender);
        assertEquals(newSender, message.getFromAddress().getAddress());
    }

    /** Test setToAddresses() - single recipient */
    public void testSetToAddressesSingleRecipient() throws AddressException {
        String newRecipient = "newRecipient@host.com";
        message.setToAddresses(newRecipient);
        assertEquals(newRecipient, message.getToAddresses()[0].getAddress());
    }

    /** Test setToAddresses() - multiple recipients */
    public void testSetToAddressesMultipleRecipients() throws AddressException {
        String newRecipients = "new@one.com, new@two.com, new@three.com";
        InternetAddress[] newSendTo = InternetAddress.parse(newRecipients);
        message.setToAddresses(newRecipients);
        InternetAddress[] addresses = message.getToAddresses();
        for (int i = 0; i < addresses.length; i++) {
            assertEquals(addresses[i].getAddress(), newSendTo[i].getAddress());
        }
    }

    /** Test setSubject() */
    public void testSetSubject() {
        String newSubject = "New subject";
        message.setSubject(newSubject);
        assertEquals(newSubject, message.getSubject());
    }

    /** Test setBody() */
    public void testSetBody() {
        String newBody = "New message body";
        message.setBody(newBody);
        assertEquals(newBody, message.getBody());
    }

    /** Test setStatus() */
    public void testSetStatus() throws Exception {
        int newStatus = LiveMessageInfo.FORWARDED;
        message.setStatus(newStatus);
        assertEquals(newStatus, message.getStatus());
    }

    /** Test setDate() */
    public void testSetDate() {
        CustomDate newDate = new CustomDate(2000, 12, 31, 23, 59);
        message.setDate(newDate);
        assertTrue(newDate.equals(message.getDate()));
        assertFalse(date.equals(message.getDate()));
    }

    /** Test setInfo() */
    public void testSetInfo() throws Exception {
        int newStatus = LiveMessageInfo.SENT;
        CustomDate newDate = new CustomDate(2004, 3, 15, 17, 45);
        LiveMessageInfo newInfo = new LiveMessageInfo(newStatus, newDate);
        message.setInfo(newInfo);
        assertFalse(msgInfo.equals(message.getInfo()));
        assertTrue(newInfo.equals(message.getInfo()));
    }

    /** Test addToAddress() */
    public void testAddToAddress() throws Exception {
        String newAddress = "new@recipient.com";
        int initialSize = message.countRecipients();
        assertTrue(initialSize == 1);
        message.addToAddress(newAddress);
        assertTrue(message.countRecipients() == initialSize + 1);
        assertTrue(newAddress == message.getToAddress(initialSize).getAddress());
        assertTrue(message.removeToAddress(newAddress));
        assertTrue(message.removeToAddress(to));
        assertTrue(message.countRecipients() == 0);
        message.addToAddress(newAddress);
        assertTrue(message.countRecipients() == 1);
        assertTrue(newAddress == message.getToAddress(0).getAddress());
    }

    /** Test removeToAddress() */
    public void testRemoveToAddress() throws AddressException {
        int initialSize = message.countRecipients();
        String newAddress1 = "new1@recipient.com";
        String newAddress2 = "new2@recipient.com";
        String newAddress3 = "new3@recipient.com";
        message.addToAddress(newAddress1);
        message.addToAddress(newAddress2);
        message.addToAddress(newAddress3);
        assertTrue(message.countRecipients() == initialSize + 3);
        assertFalse(message.removeToAddress("recipient@doesnt.exist"));
        assertTrue(message.removeToAddress(newAddress2));
        assertFalse(message.removeToAddress(newAddress2));
        assertTrue(message.countRecipients() == initialSize + 2);
        assertTrue(message.removeToAddress(newAddress3));
        assertFalse(message.removeToAddress(newAddress3));
        assertTrue(message.countRecipients() == initialSize + 1);
        assertTrue(message.removeToAddress(newAddress1));
        assertFalse(message.removeToAddress(newAddress1));
        assertTrue(message.countRecipients() == initialSize);
        assertTrue(message.removeToAddress(to));
        assertFalse(message.removeToAddress(to));
        assertTrue(message.countRecipients() == 0);
    }

    /** Test addAttachment() */
    public void testAddAttachment() {
        File newAttachment = new File(SystemInfo.APP_HOME + "/data/test/other.txt");
        message.addAttachment(newAttachment);
        Vector allAttachments = message.getAttachments();
        int lastItem = allAttachments.size() - 1;
        assertEquals(newAttachment, message.getAttachments().get(lastItem));
    }

    /** Test removeAttachment() */
    public void testRemoveAttachment() {
        message.removeAttachment(txtFile);
        assertEquals(1, message.countAttachments());
        message.removeAttachment(wavFile);
        assertEquals(0, message.countAttachments());
    }

    /** Test countAttachments() */
    public void testCountAttachments() {
        assertEquals(2, message.countAttachments());
    }

    /** Test countVoiceAttachments() */
    public void testCountVoiceAttachments() {
        assertEquals(1, message.countVoiceAttachments());
    }

    /** Test hashCode() */
    public void testHashCode() {
    }

    /** Test equals() */
    public void testEquals() throws Exception {
        LiveMessage same1 = new LiveMessage(from, to, subject, body, msgInfo);
        assertTrue(message.equals(same1));
        assertTrue(message.hashCode() == same1.hashCode());
        LiveMessage same2 = new LiveMessage(from, "other", subject, body, msgInfo);
        assertTrue(message.equals(same2));
        assertTrue(message.hashCode() == same2.hashCode());
        LiveMessage diff1 = new LiveMessage("other", to, subject, body, msgInfo);
        assertFalse(message.equals(diff1));
        assertFalse(message.hashCode() == diff1.hashCode());
        LiveMessage diff2 = new LiveMessage(from, to, "other", body, msgInfo);
        assertFalse(message.equals(diff2));
        assertFalse(message.hashCode() == diff2.hashCode());
        LiveMessage diff3 = new LiveMessage(from, to, subject, "other", msgInfo);
        assertFalse(message.equals(diff3));
        assertFalse(message.hashCode() == diff3.hashCode());
        CustomDate otherDate = new CustomDate(2004, 12, 20, 14, 45);
        LiveMessageInfo newInfo = new LiveMessageInfo(status, otherDate);
        LiveMessage diff4 = new LiveMessage(from, to, subject, body, newInfo);
        assertFalse(message.equals(diff4));
        assertFalse(message.hashCode() == diff4.hashCode());
    }

    /** Test compareTo() */
    public void testCompareTo() throws Exception {
        LiveMessage same = new LiveMessage(from, to, subject, body);
        same.setDate(new CustomDate(year, month, day, hours, minutes));
        LiveMessage later = new LiveMessage(from, to, subject, body);
        later.setDate(new CustomDate(year, month + 1, day, hours, minutes));
        LiveMessage earlier = new LiveMessage(from, to, subject, body);
        earlier.setDate(new CustomDate(year, month - 1, day, hours, minutes));
        assertTrue(message.compareTo(same) == 0);
        assertTrue(message.compareTo(earlier) > 0);
        assertTrue(message.compareTo(later) < 0);
    }

    /** Test toString() */
    public void testToString() {
        System.out.println(message.toString());
    }

    /** main() - run the tests */
    public static void main(String[] args) throws Exception {
        LiveMessageTest test = new LiveMessageTest();
    }
}
