package org.dhcpdj.config.xml;

import org.apache.log4j.Logger;
import org.dhcpdj.config.xml.XmlPolicyFactory;
import org.dhcpdj.config.xml.data.DhcpServer;
import org.dhcpdj.config.GlobalConfig;

/**
 * 
 * @author Stephan Hadinger
 * @version 0.73
 */
public final class XmlGlobalConfigReader {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(XmlGlobalConfigReader.class);

    public static GlobalConfig xmlGlobalConfigReader(DhcpServer.Global globalData) {
        GlobalConfig globalConfig = new GlobalConfig();
        if (globalData.getServer() != null) {
            globalConfig.setServerIdentifier(globalData.getServer().getIdentifier());
        }
        if (globalData.getServerPolicy() != null) {
            XmlPolicyFactory.parsePolicy(globalData.getServerPolicy(), globalConfig.getServerPolicy());
        }
        return globalConfig;
    }
}
