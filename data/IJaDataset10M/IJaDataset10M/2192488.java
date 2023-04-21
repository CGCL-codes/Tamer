package org.localstorm.stocktracker.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.stream.XMLStreamException;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.localstorm.stocktracker.camel.CamelService;
import org.localstorm.stocktracker.camel.Endpoints;
import org.localstorm.stocktracker.camel.util.ExchangeFactory;
import org.localstorm.stocktracker.camel.util.ProducerUtil;
import org.localstorm.stocktracker.config.Configuration;
import org.localstorm.stocktracker.config.GlobalConfiguration;
import org.localstorm.stocktracker.exchange.StockPriceRequest;
import org.localstorm.stocktracker.rest.parsers.ObjectXmlReader;
import org.localstorm.stocktracker.rest.parsers.StockPriceRequestParser;
import static org.localstorm.stocktracker.rest.resources.Constants.*;

/**
 * RESTful resource for StockPricesRequests handling
 * @author Alexey Kuznetsov
 */
@Path("/prices")
public class StockPricesXmlResource {

    private static final Log log = LogFactory.getLog(StockPricesXmlResource.class);

    private Endpoint ep;

    private Producer channel;

    private int pricesMaxRequestSize;

    private int maxIssuers;

    @SuppressWarnings("unchecked")
    public StockPricesXmlResource() throws Exception {
        CamelContext cc = CamelService.getInstance().getCamelContext();
        this.ep = cc.getEndpoint(Endpoints.STOCK_PRICES_INPUT_URI);
        this.channel = ep.createProducer();
        Configuration conf = GlobalConfiguration.getConfiguration();
        this.pricesMaxRequestSize = conf.getPricesRequestMaxSize();
        this.maxIssuers = conf.getPricesRequestMaxSize();
    }

    @POST
    @Produces("text/plain")
    public Response handle(InputStream is) {
        ObjectXmlReader<StockPriceRequest> reader = null;
        try {
            this.channel.start();
            reader = new ObjectXmlReader<StockPriceRequest>(is, this.pricesMaxRequestSize);
            StockPriceRequest spr = reader.getObject(new StockPriceRequestParser(this.maxIssuers));
            DefaultExchange ex = ExchangeFactory.inOut(ep, spr);
            this.channel.process(ex);
            return ResponseUtil.buildOkResponse(SUCCESS_RESPONSE);
        } catch (XMLStreamException e) {
            log.error(e);
            return ResponseUtil.buildErrorResponse(e, HTTP_BAD_REQUEST);
        } catch (IOException e) {
            log.error(e);
            return ResponseUtil.buildErrorResponse(e, HTTP_BAD_REQUEST);
        } catch (Exception e) {
            log.error(e);
            return ResponseUtil.buildErrorResponse(e, HTTP_SERVER_ERROR);
        } finally {
            if (reader != null) {
                reader.close();
            }
            ProducerUtil.stopQuietly(channel);
        }
    }
}
