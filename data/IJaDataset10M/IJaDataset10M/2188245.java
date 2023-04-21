package edu.upf.da.p2p.sm;

import org.jivesoftware.smackx.filetransfer.FileTransferManager;

public interface P2PUserClient extends P2PClient {

    FileTransferManager getFileTransferManager();
}
