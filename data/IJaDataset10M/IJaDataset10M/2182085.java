package de.hdtconsulting.yahoo.finance.server.csv.connection;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * connection for getting daytrade data
 * @author hdt
 *
 */
public class YConnectionDayTrade extends AbstractYConnection implements Serializable {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 4610327561759281622L;

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(YConnectionDayTrade.class);

    private static final String HOST = "logtrade.finance.vip.ukl.yahoo.com";

    private static final String PATH = "lastTrades";

    public YConnectionDayTrade(YConnectionManager connectionManager) {
        super(connectionManager);
    }

    private URI getUrl(String urlSymbolParameter) throws URISyntaxException {
        if (logger.isDebugEnabled()) {
            logger.debug("getUrl() - start");
        }
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("output", "user"));
        qparams.add(new BasicNameValuePair("i", "eu"));
        qparams.add(new BasicNameValuePair(PARAM_SYMBOL, urlSymbolParameter));
        URI uri = URIUtils.createURI("http", HOST, PORT, PATH, URLEncodedUtils.format(qparams, "UTF-8"), null);
        if (logger.isDebugEnabled()) {
            logger.debug("getUrl() - URI uri=" + uri);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getUrl() - end");
        }
        return uri;
    }

    public String getCsv(String urlSymbolParameter) throws ClientProtocolException, IOException, URISyntaxException {
        return this.getCsv(getUrl(urlSymbolParameter));
    }
}
