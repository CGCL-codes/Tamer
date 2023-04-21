package net.sf.lanwork.server;

import java.io.IOException;
import java.net.*;
import net.sf.lanwork.client.*;
import net.sf.lanwork.connect.*;
import net.sf.lanwork.connect.action.*;
import net.sf.lanwork.nfs.*;

/**
 * 处理接收到的连接。根据发送的HelloMessage进行分配。
 * @author Thomas Ting
 * @version 0.1 2009.1.3
 */
public class SocketDispatcher {

    public void dispatch(Socket e) {
        ActionChain chain = new ActionChain();
        SocketProcessor sp = new SocketProcessor();
        Action dispatchAction = new DispatchAction(sp);
        chain.addAction(MessageType.FileBrowserWanted, dispatchAction);
        chain.addAction(MessageType.Online, dispatchAction);
        try {
            sp.initialize(e, chain, null);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
	 * 对于NetFileSystemServer,设置共享目录。
	 * @param path 要共享的目录
	 */
    public void setSharePath(String path) {
        sharePath = path;
    }

    /**
	 * 得到共享目录。
	 * @return 返回共享目录
	 */
    public String getSharePath() {
        return sharePath;
    }

    private class DispatchAction implements Action {

        public DispatchAction(SocketProcessor parent) {
            this.parent = parent;
        }

        public Data process(Data data) {
            if (data.getMessageType() == MessageType.FileBrowserWanted) {
                new NetFileSystemServer(sharePath, parent);
            } else if (data.getMessageType() == MessageType.Online) {
                parent.addAction(MessageType.HostInformation, new HostInformationAction());
            }
            return null;
        }

        private SocketProcessor parent = null;
    }

    private String sharePath = null;
}
