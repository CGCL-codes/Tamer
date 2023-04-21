package iwallet.client.transport;

import iwallet.common.request.Request;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 和服务器端的iwallet.server.transport.Transport接口相对应
 * 的客户端通信接口
 * @author rhyme
 *
 */
public interface TransportClient {

    /**
     * 连接服务器
     * @throws TransportException
     */
    public void connect() throws TransportException;

    /**
     * 从服务器断开
     * @throws TransportException
     */
    public void disconnect() throws TransportException;

    /**
     * 判断调用时是否仍与服务器保持连接
     * @return 连接与否
     */
    public boolean isConnected();

    /**
     * 获得对服务器请求的输出流
     * @return 输出流
     * @throws TransportException
     */
    public ObjectOutputStream getRequestStream() throws TransportException;

    /**
     * @return 输入流
     * @throws TransportException
     */
    public ObjectInputStream getReplyStream() throws TransportException;

    /**
     * 获得服务器响应的输入流
     * @param request
     * @throws TransportException
     */
    public void sendRequest(Request request) throws TransportException;

    /**
     * @return
     * @throws TransportException
     */
    public boolean getBoolean() throws TransportException;

    public int getInt() throws TransportException;

    public Object readObject() throws TransportException;
}
