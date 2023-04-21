package irrigator.service;

import java.net.MalformedURLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

public class QuoteService implements IQuoteService {

    private static Log logger = LogFactory.getLog(QuoteService.class);

    public QuoteService() {
    }

    public static void main(String[] args) {
        QuoteService test = new QuoteService();
        logger.info(test.getQuote());
    }

    public String getQuote() {
        Service serviceModel = new ObjectServiceFactory().create(IQuoteService.class);
        logger.debug("got service model.");
        XFire xfire = XFireFactory.newInstance().getXFire();
        XFireProxyFactory factory = new XFireProxyFactory(xfire);
        String serviceUrl = "http://webservices.codingtheweb.com/bin/qotd";
        IQuoteService client = null;
        try {
            client = (IQuoteService) factory.create(serviceModel, serviceUrl);
        } catch (MalformedURLException e) {
            logger.error(e.toString());
            return "\"No good URL, no quote\" (Your Web Server)";
        }
        String serviceResponse = "";
        try {
            serviceResponse = client.getQuote();
        } catch (Exception e) {
            logger.error(e.toString());
            serviceResponse = "No quote, because of: " + e.toString();
        }
        logger.debug("No quote available. See error log.");
        return serviceResponse;
    }
}
