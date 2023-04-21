package org.statefive.feedstate.feed.command;

import org.statefive.feedstate.feed.FeedStatus;

/**
 * Response detailing the status of a feed.
 * 
 * @author rmeeking
 */
public class StatusResponse extends AbstractResponse {

    /** Status for this response. */
    private FeedStatus status;

    /**
   * Basic Constructor.
   * 
   * @param id the identifier for the request.
   */
    public StatusResponse(final String id, final FeedStatus status) {
        super(id);
        this.status = status;
    }

    /**
   * Gets the status for this response.
   * @return the status; may be <code>null</code>.
   */
    public FeedStatus getStatus() {
        return this.status;
    }

    /**
   * Sets the status for this response.
   * @param status the status to set; may be <code>null</code>.
   */
    public void setStatus(final FeedStatus status) {
        this.status = status;
    }

    /**
   * Returns human-readable info about this request.
   * @return whether this request is to pause or un-pause.
   */
    @Override
    public String toString() {
        return "Status Response: Status = " + this.getStatus();
    }
}
