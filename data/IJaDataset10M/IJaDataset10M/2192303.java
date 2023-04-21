package eg.nileu.cis.nilestore.immutable.peertracker.port;

import se.sics.kompics.Event;

/**
 * The Class GetShares.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class GetShares extends Event {

    /** The storage index. */
    private final String storageIndex;

    /**
	 * Instantiates a new gets the shares.
	 * 
	 * @param storageIndex
	 *            the storage index
	 */
    public GetShares(String storageIndex) {
        this.storageIndex = storageIndex;
    }

    /**
	 * Gets the storage index.
	 * 
	 * @return the storage index
	 */
    public String getStorageIndex() {
        return storageIndex;
    }
}
