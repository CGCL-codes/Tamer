package test.org.slasoi.businessManager.reporting.delivery;

import java.util.Date;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slaatsoi.business.schema.ReportFormatType;
import org.slasoi.businessManager.reporting.delivery.Actor;
import org.slasoi.businessManager.reporting.delivery.EmailReportDeliverer;
import org.slasoi.businessManager.reporting.delivery.Envelope;
import org.slasoi.businessManager.reporting.report.Report;

/**
 * @author Davide Lorenzoli
 * 
 * @date Mar 3, 2011
 */
public class TestEmailReportDeliverer {

    static {
        StringBuilder frameworkConfigurationFolder = new StringBuilder();
        String slasoiHome = System.getenv("SLASOI_HOME");
        if (slasoiHome == null) {
            PropertyConfigurator.configure(ClassLoader.getSystemResource("./conf/log4j.properties").getPath());
        } else {
            frameworkConfigurationFolder.append(slasoiHome);
            frameworkConfigurationFolder.append(System.getProperty("file.separator"));
            frameworkConfigurationFolder.append("bmanager-postsale-reporting");
            PropertyConfigurator.configure(frameworkConfigurationFolder + "/log4j.properties");
        }
    }

    Logger logger = Logger.getLogger(getClass());

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link org.slasoi.businessManager.reporting.delivery.EmailReportDeliverer#deliver(org.slasoi.businessManager.reporting.delivery.Envelope)}.
	 */
    @Test
    public void testDeliver() {
        EmailReportDeliverer emailReportDeliverer = new EmailReportDeliverer();
        Report report = new Report("R1", "SLA1", new Date(), ReportFormatType.TEXT, "Content");
        boolean result = emailReportDeliverer.deliver(getEnvelope(report));
    }

    private Envelope getEnvelope(Report report) {
        Envelope envelope = null;
        Actor sender = new Actor("Sender");
        sender.setEmails(new String[] { "slasoi.reporting@gmail.com" });
        Actor receiver = new Actor("Receiver");
        receiver.setEmails(new String[] { "davide.lorenzolo@gmail.com" });
        envelope = new Envelope("Message body", new Report[] { report }, sender, new Actor[] { receiver });
        return envelope;
    }
}
