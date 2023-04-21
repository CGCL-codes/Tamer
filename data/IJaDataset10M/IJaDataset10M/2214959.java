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
import org.localstorm.stocktracker.camel.util.ProducerUtil;
import org.localstorm.stocktracker.exchange.StockTrackingRequest;
import org.localstorm.stocktracker.camel.util.ExchangeFactory;
import org.localstorm.stocktracker.config.Configuration;
import org.localstorm.stocktracker.config.GlobalConfiguration;
import org.localstorm.stocktracker.rest.parsers.ObjectXmlReader;
import org.localstorm.stocktracker.rest.parsers.TrackingRequestParser;
import static org.localstorm.stocktracker.rest.resources.Constants.*;

/**
 & RESTful resource for StockTrackingRequests handling 
 * @author Alexey Kuznetsov
 */
@Path("/tracking")
public class TrackingXmlResource {

    private static final Log log = LogFactory.getLog(TrackingXmlResource.class);

    private Endpoint ep;

    private Producer channel;

    private int maxRequestSize;

    private int userQuota;

    @SuppressWarnings("unchecked")
    public TrackingXmlResource() throws Exception {
        CamelContext cc = CamelService.getInstance().getCamelContext();
        this.ep = cc.getEndpoint(Endpoints.TRACKING_REQUESTS_INPUT_URI);
        this.channel = ep.createProducer();
        Configuration conf = GlobalConfiguration.getConfiguration();
        this.maxRequestSize = conf.getTrackingRequestMaxSize();
        this.userQuota = conf.getUserMaxTrackingEventsQuota();
    }

    @POST
    @Produces("text/plain")
    public Response handle(InputStream is) {
        ObjectXmlReader<StockTrackingRequest> reader = null;
        try {
            this.channel.start();
            reader = new ObjectXmlReader<StockTrackingRequest>(is, this.maxRequestSize);
            TrackingRequestParser trp = new TrackingRequestParser(this.userQuota);
            StockTrackingRequest str = reader.getObject(trp);
            DefaultExchange ex = ExchangeFactory.inOut(ep, str);
            this.channel.process(ex);
            Throwable e = (Throwable) ex.getFault().getBody();
            if (e != null) {
                return ResponseUtil.buildErrorResponse(e, HTTP_SERVER_ERROR);
            } else {
                return ResponseUtil.buildOkResponse(SUCCESS_RESPONSE);
            }
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
