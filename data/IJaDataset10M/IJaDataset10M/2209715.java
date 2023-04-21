package eg.nileu.cis.nilestore.storage.port.network;

import java.util.Map;
import eg.nileu.cis.nilestore.common.ComponentAddress;
import eg.nileu.cis.nilestore.common.ExtMessage;

/**
 * The Class GetBucketsResponse.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class GetBucketsResponse extends ExtMessage {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4351615272764554616L;

    /** The shares. */
    private final Map<Integer, String> shares;

    /**
	 * Instantiates a new gets the buckets response.
	 * 
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @param shares
	 *            the shares
	 */
    public GetBucketsResponse(ComponentAddress source, ComponentAddress destination, Map<Integer, String> shares) {
        super(source, destination);
        this.shares = shares;
    }

    /**
	 * Gets the shares.
	 * 
	 * @return the shares
	 */
    public Map<Integer, String> getShares() {
        return shares;
    }
}
