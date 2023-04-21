package org.test.socket.poll;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 服务器间连接
 * 
 * @author <a href="mailto:fmlou@163.com">HongzeZhang</a>
 * 
 * @version 1.0
 * 
 *          2010-6-30
 */
public class SocketWriter {

    private boolean isFree = true;

    private PrintWriter writer = null;

    private Socket socket = null;

    public SocketWriter(SocketAddress address) {
        socket = new Socket();
        try {
            socket.connect(address);
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketWriter(SocketAddress address, int connectTime) {
        socket = new Socket();
        try {
            socket.connect(address, connectTime);
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * 发送消息
	 * 
	 * @param message
	 */
    public void println(Object message) {
        if (writer != null && socket.isConnected()) {
            writer.println(message);
            isFree = true;
        }
    }

    /**
	 * 获取状态
	 * @return
	 */
    public boolean isFree() {
        return isFree;
    }

    /**
	 * 关闭连接
	 */
    public void close() {
        writer.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = null;
        socket = null;
    }

    /**
	 * 设置为忙碌
	 */
    public void setBusy() {
        this.isFree = false;
    }

    /**
	 * 判断是否已经关闭
	 * @return
	 */
    public boolean isClosed() {
        if (socket.isClosed()) return true;
        return false;
    }

    public String toString() {
        return "SocketWriter:" + socket.toString();
    }
}
