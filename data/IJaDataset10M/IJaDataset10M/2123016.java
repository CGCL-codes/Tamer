package burster.sender;

import burster.other.DocumentBursterTestCase;
import burster.other.Helpers;
import burster.settings.Settings;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

public class TestEmailSender extends DocumentBursterTestCase {

    private Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private final MultiPartEmail mockEmail = context.mock(MultiPartEmail.class);

    public void testSendSuccessSimpleTokenNoSubjectNoMessage() throws Exception {
        Settings settings = new Settings(Helpers.testConfigFolder);
        EmailSender emailSender = new EmailSender(settings, null) {

            protected Object getSender() {
                return mockEmail;
            }
        };
        context.checking(new Expectations() {

            {
                one(mockEmail).setAuthentication(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setHostName(with(aNonNull(String.class)));
                one(mockEmail).setSmtpPort(with(aNonNull(int.class)));
                one(mockEmail).setFrom(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setTLS(with(aNonNull(Boolean.class)));
                one(mockEmail).setSSL(with(aNonNull(Boolean.class)));
                one(mockEmail).setDebug(with(aNonNull(Boolean.class)));
                one(mockEmail).setSubject(with(aNonNull(String.class)));
                one(mockEmail).setMsg(with(aNonNull(String.class)));
                one(mockEmail).attach(with(aNonNull(EmailAttachment.class)));
                one(mockEmail).addTo(with(aNonNull(String.class)));
                one(mockEmail).send();
            }
        });
        emailSender.send("john@yahoo.com");
        assertTrue(emailSender.isSent());
        assertFalse(emailSender.isQuarantined());
    }

    public void testSendSuccessSimpleTokenWithSubjectWithMessage() throws Exception {
        Settings settings = new Settings(Helpers.testConfigFolder) {

            public String getDefaultEmailSubject() {
                return "subject";
            }

            public String getDefaultEmailText() {
                return "text";
            }
        };
        EmailSender emailSender = new EmailSender(settings, null) {

            protected Object getSender() {
                return mockEmail;
            }
        };
        context.checking(new Expectations() {

            {
                one(mockEmail).setAuthentication(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setHostName(with(aNonNull(String.class)));
                one(mockEmail).setSmtpPort(with(aNonNull(int.class)));
                one(mockEmail).setFrom(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setTLS(with(aNonNull(Boolean.class)));
                one(mockEmail).setSSL(with(aNonNull(Boolean.class)));
                one(mockEmail).setDebug(with(aNonNull(Boolean.class)));
                one(mockEmail).setSubject(with(aNonNull(String.class)));
                one(mockEmail).setMsg(with(aNonNull(String.class)));
                one(mockEmail).attach(with(aNonNull(EmailAttachment.class)));
                one(mockEmail).addTo(with(aNonNull(String.class)));
                one(mockEmail).send();
            }
        });
        emailSender.send("john@yahoo.com");
        assertTrue(emailSender.isSent());
        assertFalse(emailSender.isQuarantined());
    }

    public void testSendSuccessDestination() throws Exception {
        Settings settings = new Settings(Helpers.testConfigFolder);
        EmailSender emailSender = new EmailSender(settings, settings.getEmailDestination("sampleOfEmail")) {

            protected Object getSender() {
                return mockEmail;
            }
        };
        context.checking(new Expectations() {

            {
                one(mockEmail).setAuthentication(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setHostName(with(aNonNull(String.class)));
                one(mockEmail).setSmtpPort(with(aNonNull(int.class)));
                one(mockEmail).setFrom(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setTLS(with(aNonNull(Boolean.class)));
                one(mockEmail).setSSL(with(aNonNull(Boolean.class)));
                one(mockEmail).setDebug(with(aNonNull(Boolean.class)));
                one(mockEmail).setSubject(with(aNonNull(String.class)));
                one(mockEmail).setMsg(with(aNonNull(String.class)));
                one(mockEmail).attach(with(aNonNull(EmailAttachment.class)));
                exactly(3).of(mockEmail).addTo(with(aNonNull(String.class)));
                exactly(2).of(mockEmail).addCc(with(aNonNull(String.class)));
                exactly(2).of(mockEmail).addBcc(with(aNonNull(String.class)));
                one(mockEmail).send();
            }
        });
        emailSender.send("sampleOfEmail");
        assertTrue(emailSender.isSent());
        assertFalse(emailSender.isQuarantined());
    }

    public void testSendFailedWithoutQuarantine() throws Exception {
        Settings settings = new Settings(Helpers.testConfigFolder) {

            public boolean isQuarantineFiles() {
                return false;
            }
        };
        EmailSender emailSender = new EmailSender(settings, null) {

            protected Object getSender() {
                return mockEmail;
            }
        };
        context.checking(new Expectations() {

            {
                one(mockEmail).setAuthentication(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setHostName(with(aNonNull(String.class)));
                one(mockEmail).setSmtpPort(with(aNonNull(int.class)));
                one(mockEmail).setFrom(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setTLS(with(aNonNull(Boolean.class)));
                one(mockEmail).setSSL(with(aNonNull(Boolean.class)));
                one(mockEmail).setDebug(with(aNonNull(Boolean.class)));
                one(mockEmail).setSubject(with(aNonNull(String.class)));
                one(mockEmail).setMsg(with(aNonNull(String.class)));
                one(mockEmail).attach(with(aNonNull(EmailAttachment.class)));
                one(mockEmail).addTo(with(aNonNull(String.class)));
                one(mockEmail).send();
                will(throwException(new EmailException()));
            }
        });
        emailSender.send("john@yahoo.com");
        assertFalse(emailSender.isSent());
        assertFalse(emailSender.isQuarantined());
    }

    public void testSendFailedWithQuarantine() throws Exception {
        Settings settings = new Settings(Helpers.testConfigFolder);
        EmailSender email = new EmailSender(settings, null) {

            protected Object getSender() {
                return mockEmail;
            }
        };
        context.checking(new Expectations() {

            {
                one(mockEmail).setAuthentication(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setHostName(with(aNonNull(String.class)));
                one(mockEmail).setSmtpPort(with(aNonNull(int.class)));
                one(mockEmail).setFrom(with(aNonNull(String.class)), with(aNonNull(String.class)));
                one(mockEmail).setTLS(with(aNonNull(Boolean.class)));
                one(mockEmail).setSSL(with(aNonNull(Boolean.class)));
                one(mockEmail).setDebug(with(aNonNull(Boolean.class)));
                one(mockEmail).setSubject(with(aNonNull(String.class)));
                one(mockEmail).setMsg(with(aNonNull(String.class)));
                one(mockEmail).attach(with(aNonNull(EmailAttachment.class)));
                one(mockEmail).addTo(with(aNonNull(String.class)));
                one(mockEmail).send();
                will(throwException(new EmailException()));
            }
        });
        email.attachFile(Helpers.testInputDocumentPath);
        email.setQuarantineFolder(Helpers.testQuarantineFolder);
        email.send("john@yahoo.com");
        assertFalse(email.isSent());
        assertTrue(email.isQuarantined());
    }
}
