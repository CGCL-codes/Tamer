package wsmg;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import wsmg.db.SubscriptionDB;
import wsmg.jms.JMS_WsntAdapter;

/**
 * @author yihuan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WsmgConfig {

    private boolean hasCarrier = false;

    private WsmgAdapter wsntAdapter;

    private Map subscriptions = new HashMap();

    private Map publisherRegistrationDB = new HashMap();

    /**
	 * @return Returns the hasCarrier.
	 */
    public boolean isHasCarrier() {
        return hasCarrier;
    }

    /**
	 * @return Returns the publisherRegistrationDB.
	 */
    public Map getPublisherRegistrationDB() {
        return publisherRegistrationDB;
    }

    /**
	 * @return Returns the subscriptions.
	 */
    public Map getSubscriptions() {
        return subscriptions;
    }

    /**
	 * @return Returns the wsntAdapter.
	 */
    public WsmgAdapter getWsntAdapter() {
        return wsntAdapter;
    }

    public void setCarrier(String carrier, String carrierLocation) throws WsmgAdapterException {
        if (carrier == null) {
            hasCarrier = false;
        } else {
            hasCarrier = true;
            wsntAdapter = new JMS_WsntAdapter(subscriptions, publisherRegistrationDB, carrier);
            wsntAdapter.start(carrierLocation);
        }
    }
}
