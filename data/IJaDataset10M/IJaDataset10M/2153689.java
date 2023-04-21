package org.apache.camel.loanbroker.webservice.version;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoanBrokerTest extends Assert {

    AbstractApplicationContext applicationContext;

    @Before
    public void startServices() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[] { "/META-INF/spring/webServiceCamelContext.xml" });
    }

    @After
    public void stopServices() throws Exception {
        if (applicationContext != null) {
            applicationContext.stop();
        }
    }

    @Test
    public void testInvocation() {
        Client client = new Client();
        String result = null;
        LoanBrokerWS loanBroker = client.getProxy(Constants.LOANBROKER_ADDRESS);
        long startTime = System.currentTimeMillis();
        result = loanBroker.getLoanQuote("Sequential SSN", 1000.54, 10);
        long endTime = System.currentTimeMillis();
        long delta1 = endTime - startTime;
        assertTrue(result.startsWith("The best rate is [ ssn:Sequential SSN bank:bank"));
        LoanBrokerWS paralleLoanBroker = client.getProxy(Constants.PARALLEL_LOANBROKER_ADDRESS);
        startTime = System.currentTimeMillis();
        result = paralleLoanBroker.getLoanQuote("Parallel SSN", 1000.54, 10);
        endTime = System.currentTimeMillis();
        long delta2 = endTime - startTime;
        assertTrue(result.startsWith("The best rate is [ ssn:Parallel SSN bank:bank"));
        assertTrue(delta2 < delta1);
    }
}
