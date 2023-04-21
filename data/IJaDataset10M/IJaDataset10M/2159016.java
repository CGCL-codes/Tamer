package com.net.server.telnet;

public interface TelnetSessionListener {

    /**
	 * 监听客户端的断开
	 */
    public void disconnected(TelnetSession session);

    /**
	 * 监听客户端发送过来的数据包
	 * @param session
	 * @param message
	 */
    public void receivedMessage(TelnetSession session, String message);
}
