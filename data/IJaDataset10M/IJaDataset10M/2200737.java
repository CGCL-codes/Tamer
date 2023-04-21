package eg.nileu.cis.nilestore.immutable.downloader.segfetcher.port;

import java.util.Map;
import se.sics.kompics.Event;
import eg.nileu.cis.nilestore.common.StatusMsg;

/**
 * The Class GetSegmentResponse.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class GetSegmentResponse extends Event {

    /** The status. */
    private final StatusMsg status;

    /** The blocks. */
    private final Map<Integer, byte[]> blocks;

    /**
	 * Instantiates a new gets the segment response.
	 * 
	 * @param status
	 *            the status
	 * @param blocks
	 *            the blocks
	 */
    public GetSegmentResponse(StatusMsg status, Map<Integer, byte[]> blocks) {
        this.status = status;
        this.blocks = blocks;
    }

    /**
	 * Gets the status.
	 * 
	 * @return the status
	 */
    public StatusMsg getStatus() {
        return status;
    }

    /**
	 * Gets the blocks.
	 * 
	 * @return the blocks
	 */
    public Map<Integer, byte[]> getBlocks() {
        return blocks;
    }
}
