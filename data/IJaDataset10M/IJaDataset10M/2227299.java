package camelinaction;

import java.net.ConnectException;
import javax.sql.DataSource;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @version $Revision: 214 $
 */
public class RiderAutoPartsPartnerTXTest extends CamelSpringTestSupport {

    private JdbcTemplate jdbc;

    @Before
    public void setupDatabase() throws Exception {
        DataSource ds = context.getRegistry().lookup("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);
        jdbc.execute("create table partner_metric " + "( partner_id varchar(10), time_occurred varchar(20), status_code varchar(3), perf_time varchar(10) )");
    }

    @After
    public void dropDatabase() throws Exception {
        jdbc.execute("drop table partner_metric");
    }

    @Test
    public void testSendPartnerReportIntoDatabase() throws Exception {
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));
        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>200911150815</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);
        Thread.sleep(5000);
        assertEquals(1, jdbc.queryForInt("select count(*) from partner_metric"));
    }

    @Test
    public void testNoConnectionToDatabase() throws Exception {
        RouteBuilder rb = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("jdbc:*").skipSendToOriginalEndpoint().throwException(new ConnectException("Cannot connect to the database"));
            }
        };
        context.getRouteDefinition("partnerToDB").adviceWith(context, rb);
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));
        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>200911150815</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);
        Thread.sleep(10000);
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));
        String dlq = consumer.receiveBodyNoWait("activemq:queue:ActiveMQ.DLQ", String.class);
        assertNotNull("Should not lose message", dlq);
    }

    @Test
    public void testFailFirstTime() throws Exception {
        RouteBuilder rb = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("jdbc:*").to("log:intercepted?showAll=true").choice().when(header("JMSRedelivered").isEqualTo("false")).throwException(new ConnectException("Cannot connect to the database")).end();
            }
        };
        context.getRouteDefinition("partnerToDB").adviceWith(context, rb);
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));
        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>200911150815</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);
        Thread.sleep(5000);
        assertEquals(1, jdbc.queryForInt("select count(*) from partner_metric"));
        String dlq = consumer.receiveBodyNoWait("activemq:queue:ActiveMQ.DLQ", String.class);
        assertNull("Should not be in the DLQ", dlq);
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/RiderAutoPartsPartnerTXTest.xml");
    }
}
