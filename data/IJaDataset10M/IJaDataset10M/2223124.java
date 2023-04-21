package net.sf.javadc.interfaces;

import java.io.IOException;
import net.sf.javadc.net.DownloadRequest;
import net.sf.javadc.net.hub.HostInfo;

/**
 * @author twe
 */
public interface IClient {

    /**
     * Add a DownloadRequest to the queue of downloads
     * 
     * @param dr DownloadRequest to be added
     * @throws IOException
     */
    public abstract void addDownload(DownloadRequest dr) throws IOException;

    /**
     * Adds the given array of DownloadRequests to the queue of downloads
     * 
     * @param drs array of DownloadReqeusts
     * @throws IOException
     */
    public abstract void addDownloads(DownloadRequest[] drs) throws IOException;

    /**
     * Checks whether downloads are still in the queue, and if yes, the topmost item is set as the DownloadRequest of
     * the ClientConnection
     * 
     * @throws IOException
     */
    public abstract void checkDownloads() throws IOException;

    /**
     * @throws IOException
     */
    public abstract void clientConnect() throws IOException;

    /**
     * Establishes a new ClientConnection, if not yet available
     * 
     * @deprecated use serverConnect and clientConnect instead
     * @throws IOException
     */
    @Deprecated
    public abstract void connect() throws IOException;

    /**
     * Get the Client Connection associated with this Client
     * 
     * @return
     */
    public abstract IConnection getConnection();

    /**
     * Get the DownloadRequest with the given index
     * 
     * @param index index of the DownloadRequest to be retrieved
     * @return
     */
    public abstract DownloadRequest getDownload(int index);

    /**
     * Get the list of downloads as an array of DownloadRequests
     * 
     * @return
     */
    public abstract DownloadRequest[] getDownloads();

    /**
     * Return the HostInfo instance associated with this Client instance
     * 
     * @return
     */
    public abstract HostInfo getHost();

    /**
     * Get the last DownloadRequest in the download queue
     * 
     * @return
     */
    public abstract DownloadRequest getLastDownloadInQueue();

    /**
     * Get the nick name of the Client
     * 
     * @return
     */
    public abstract String getNick();

    /**
     * Return whether there are DownloadRequests in the queue of not
     * 
     * @return true, if the queue is not empty and false, if it is empty
     */
    public abstract boolean hasDownloads();

    /**
     * Get whether the Client supports bzip2 compressed file lists and uploads
     * 
     * @return true, if the Client supports bzip2 and false, if not
     */
    public abstract boolean isBZSupport();

    /**
     * Remove all downloads in the queue
     */
    public abstract void removeAllDownloads();

    /**
     * Remove the given DownloadRequest
     * 
     * @param dr DownloadRequest to be removed
     */
    public abstract void removeDownload(DownloadRequest dr);

    /**
     * Remove the DownloadRequest with the given index
     * 
     * @param index index of the DownloadRequest to be removed
     */
    public abstract void removeDownload(int index);

    /**
     * @throws IOException
     */
    public abstract void serverConnect() throws IOException;

    /**
     * Set whether the Client supports bzip2 compressed file lists and uploads
     * 
     * @param b
     */
    public abstract void setBZSupport(boolean b);

    /**
     * Set the Client Connection associated with this Client
     * 
     * @param connection
     */
    public abstract void setConnection(IConnection connection);

    /**
     * Set the HostInfo instance associated with the Client instance
     * 
     * @param host
     */
    public abstract void setHost(HostInfo host);

    /**
     * Set the nick name of the Client
     * 
     * @param nick
     */
    public abstract void setNick(String nick);
}
