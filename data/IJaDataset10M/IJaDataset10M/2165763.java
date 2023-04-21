package net.sf.solarnetwork.node;

import java.util.Date;

/**
 * An object created when a {@link PriceDatum} is uploaded.
 * 
 * <p>A single {@link PriceDatum} might be uploaded to multiple destinations.
 * Each destination should use a unique key, which can be anything such as a URL).</p>
 *
 * @author matt
 * @version $Revision: 274 $ $Date: 2009-08-06 04:02:25 -0400 (Thu, 06 Aug 2009) $
 */
public class PriceDatumUpload extends AbstractDatumUpload {

    /**
	 * Default constructor.
	 */
    public PriceDatumUpload() {
        super();
    }

    /**
	 * Construct from values.
	 * 
	 * @param priceDatumId the {@link PriceDatum#getId()} to associate to
	 * @param destination the destination key
	 * @param created the created date
	 * @param trackingId the tracking ID
	 */
    public PriceDatumUpload(Long priceDatumId, String destination, Date created, Long trackingId) {
        super(priceDatumId, destination, created, trackingId);
    }

    /**
	 * @return the priceDatumId
	 */
    public Long getPriceDatumId() {
        return getDatumId();
    }

    /**
	 * @param priceDatumId the priceDatumId to set
	 */
    public void setPriceDatumId(Long priceDatumId) {
        setDatumId(priceDatumId);
    }
}
