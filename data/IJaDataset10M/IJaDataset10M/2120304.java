package com.net.client;

import java.io.IOException;
import com.cell.net.io.MessageHeader;
import com.net.AbstractSession;

public interface ServerSession extends AbstractSession {

    public void dispose();

    public boolean send(MessageHeader message);

    public boolean sendRequest(int pnum, MessageHeader message);

    public boolean connect(String host, int port, long timeout, ServerSessionListener listener) throws IOException;

    public boolean connect(String host, int port, ServerSessionListener listener) throws IOException;

    /**
	 * 获取Session的本端地址
	 */
    public String getLocalAddress();

    /**
	 * 获取Session的对端地址
	 */
    public String getRemoteAddress();
}
