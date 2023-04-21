package be.hoornaert.tom.spring.exercise4;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Marleen
 * 
 */
public class Exercise4 {

    private static final Logger LOG = Logger.getLogger(Exercise4.class);

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("exercise4.xml");
        BankService bs = (BankService) ctx.getBean("bankService");
        LOG.info(bs.computeBalanceWithInterests(2.0d));
        LOG.info(bs.generateReport(3.0d));
        LOG.info(bs.getGateway() == (RateExchangeGateway) ctx.getBean("rateExchangeGateway"));
        bs = (BankService) ctx.getBean("singletonService");
        LOG.info(bs.computeBalanceWithInterests(2.0d));
        LOG.info(bs.generateReport(3.0d));
        LOG.info(bs.getGateway() == (RateExchangeGateway) ctx.getBean("singletonGateway"));
    }
}
