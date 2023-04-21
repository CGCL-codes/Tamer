package net.sf.jqql.net;

import java.net.InetSocketAddress;
import net.sf.jqql.QQClient;

/**
 * 连接策略工厂类
 *
 * @author luma
 */
public interface IConnectionPolicyFactory {

    /**
     * 创建一个连接策略类
     *
     * @param client
     * @param id
     * @param supportedFamily
     * @param relocatedFamily
     * @param proxy
     * @param proxyUsername
     * @param proxyPassword
     * @return
     */
    public IConnectionPolicy createPolicy(QQClient client, String id, int supportedFamily, int relocatedFamily, InetSocketAddress proxy, String proxyUsername, String proxyPassword);
}
