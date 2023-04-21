package org.openuss.messaging;

import java.util.Date;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class MessageSendingCommandTest extends AbstractDependencyInjectionSpringContextTests {

    private MessageSendingCommand messageSendingCommand;

    private MessageJobDaoMock jobDao;

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        jobDao = new MessageJobDaoMock();
        messageSendingCommand.setMessageJobDao(jobDao);
    }

    public void testTextMessageSending() throws Exception {
        MessageJob job = createTextMessage();
        jobDao.create(job);
        messageSendingCommand.setDomainObject(job);
        messageSendingCommand.execute();
        validateSendState(job);
    }

    public void testTemplateMessageSending() throws Exception {
        MessageJob job = createTemplateMessage();
        jobDao.create(job);
        messageSendingCommand.setDomainObject(job);
        messageSendingCommand.execute();
        validateSendState(job);
    }

    private void validateSendState(MessageJob job) {
        for (Recipient recipient : job.getRecipients()) {
            assertTrue(SendState.TOSEND != recipient.getState());
        }
    }

    private MessageJob createTemplateMessage() {
        MessageJob job = createJob();
        TemplateMessage message = new TemplateMessageImpl();
        message.setSubject("user.password.request.subject");
        message.setTemplate("password");
        message.setSenderName("OPENUSS-PLEXUS-UNIT-TEST");
        message.addParameter("username", ">username<");
        message.addParameter("passwordresetlink", ">link<");
        job.setMessage(message);
        createRecipients(job);
        return job;
    }

    private void createRecipients(MessageJob job) {
        addRecipient(job, "de_DE", "plexus@openuss-plexus.com", "sms");
        addRecipient(job, "de", "admin@openuss-plexus.com", "sms");
        addRecipient(job, "en", "xxxopenuss-plexus", "sms");
    }

    private MessageJob createTextMessage() {
        MessageJob job = createJob();
        TextMessage message = new TextMessageImpl();
        message.setSenderName("Test E-Mail");
        message.setSubject("Subject ");
        message.setText("<body><b>OpenUSS 3.0</b><br/><p>Test E-Mail xxx</p> </body>");
        job.setMessage(message);
        createRecipients(job);
        return job;
    }

    private MessageJob createJob() {
        MessageJob job = new MessageJobImpl();
        job.setId(1234L);
        job.setCreated(new Date());
        job.setState(JobState.INQUEUE);
        return job;
    }

    private void addRecipient(MessageJob job, String locale, String email, String sms) {
        RecipientMock recipient = new RecipientMock();
        recipient.setLocale(locale);
        recipient.setEmail(email);
        recipient.setSms(sms);
        job.getRecipients().add(recipient);
    }

    protected String[] getConfigLocations() {
        return new String[] { "classpath*:applicationContext.xml", "classpath*:applicationContext-beans.xml", "classpath*:applicationContext-lucene.xml", "classpath*:applicationContext-cache.xml", "classpath*:applicationContext-messaging.xml", "classpath*:applicationContext-resources.xml", "classpath*:applicationContext-aop.xml", "classpath*:applicationContext-events.xml", "classpath*:testContext.xml", "classpath*:testSecurity.xml", "classpath*:testDataSource.xml" };
    }

    public MessageSendingCommand getMessageSendingCommand() {
        return messageSendingCommand;
    }

    public void setMessageSendingCommand(MessageSendingCommand messageSendingCommand) {
        this.messageSendingCommand = messageSendingCommand;
    }
}
