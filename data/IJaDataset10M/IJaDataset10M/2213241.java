package fecchi;

/**
 *
 * @author rcc4
 */
public interface Downloader extends Runnable {

    public TransferRequest getTransferRequest();

    /**
     *
     * @param request 
     */
    public void prep(TransferRequest request) throws DownloaderException;

    public void run();
}
