package net.sf.mxlosgi.mxlosgifiletransferbundle;

import java.io.File;
import net.sf.mxlosgi.mxlosgifiletransferbundle.listener.FileTransferListener;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 * 
 */
public interface FileTransferManager {

    /**
	 * 
	 * @param proxy
	 */
    public void addProxy(JID proxy);

    /**
	 * 
	 * @param proxy
	 */
    public void removeProxy(JID proxy);

    /**
	 * 
	 * @return
	 */
    public JID[] getProxies();

    /**
	 * add FileTransferListener
	 * 
	 * @param fileTransferListener
	 */
    public void addFileTransferListener(FileTransferListener listener);

    /**
	 * remove FileTransferListener
	 * 
	 * @param fileTransferListener
	 */
    public void removeFileTransferListener(FileTransferListener listener);

    /**
	 * add SendFileTransferFactory
	 * 
	 * @param sendFileTransferFactory
	 */
    public void addSendFileTransferFactory(SendFileTransferFactory sendFileTransferFactory);

    /**
	 * remove SendFileTransferFactory
	 * 
	 * @param sendFileTransferFactory
	 */
    public void removeSendFileTransferFactory(SendFileTransferFactory sendFileTransferFactory);

    /**
	 * add ReceiveFileTransferFactory
	 * 
	 * @param receiveFileTransferFactory
	 */
    public void addReceiveFileTransferFactory(ReceiveFileTransferFactory receiveFileTransferFactory);

    /**
	 * remove ReceiveFileTransferFactory
	 * 
	 * @param receiveFileTransferFactory
	 */
    public void removeReceiveFileTransferFactory(ReceiveFileTransferFactory receiveFileTransferFactory);

    /**
	 * negotiate with the peer and create SendFileTransfer
	 * @param connection
	 * @param to
	 * @param file
	 * @return
	 * @throws Exception
	 */
    public SendFileTransfer createSendFileTransfer(XMPPConnection connection, JID to, File file) throws Exception;

    /**
	 * negotiate with the peer and create SendFileTransfer
	 * @param connection
	 * @param to
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
    public SendFileTransfer createSendFileTransfer(XMPPConnection connection, JID to, String filepath) throws Exception;

    /**
	 * 
	 * @param connection
	 * @param to
	 * @param file
	 * @param description
	 * @param mimeType
	 * @return
	 * @throws Exception
	 */
    public SendFileTransfer createSendFileTransfer(XMPPConnection connection, JID to, File file, String description, String mimeType) throws Exception;
}
