package fildiv.jremcntl.server.core.ext;

import fildiv.jremcntl.common.core.cn.Connection;
import fildiv.jremcntl.common.core.cn.DataTransferException;
import fildiv.jremcntl.common.core.msg.Message;

public class DefaultExtConnection implements DeviceConnection {

    private Connection cn;

    public DefaultExtConnection(Connection cn) {
        this.cn = cn;
    }

    public void sendMessage(Message message) throws DataTransferException {
        cn.sendMessage(message);
    }
}
