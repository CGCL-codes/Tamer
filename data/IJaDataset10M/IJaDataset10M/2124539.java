package webservicesapi.output;

/**
 * @author Ben Leov
 */
public interface OutputQueueListener {

    /**
     * Occurs when the output queue has received output.
     *
     * @param received The output that was received.
     */
    void onOutputReceived(Output received);
}
