package com.hs.test.config;

import java.io.File;
import com.hs.framework.common.util.LogUtil;
import com.hs.framework.common.util.config.DOM4JConfiguration;

public class TestXMLConfig {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        TestXMLConfig txc = new TestXMLConfig();
        txc.testReadXML1();
        txc.testReadXML2();
    }

    public void testReadXML1() {
        try {
            DOM4JConfiguration dj = new DOM4JConfiguration(new File("hsbbs\\WEB-INF\\classes\\configXML.xml"));
            LogUtil.getLogger().info("################## 测试相对路径XMl配置文件 ##################");
            LogUtil.getLogger().info(dj.getString("dbaDB.type"));
            LogUtil.getLogger().info(dj.getString("dbaDB.driver"));
            LogUtil.getLogger().info(dj.getString("dbaDB.url"));
            LogUtil.getLogger().info(dj.getString("dbaDB.username"));
            LogUtil.getLogger().info(dj.getString("dbaDB.password"));
            LogUtil.getLogger().info("################## 测试相对路径XMl配置文件 end ##################");
            LogUtil.getLogger().info("file path: " + dj.getBasePath() + dj.getFileName());
            LogUtil.getLogger().info("file path: " + dj.getFile().getAbsoluteFile());
        } catch (Exception e) {
            LogUtil.getLogger().error(e);
        }
    }

    public void testReadXML2() {
        try {
            DOM4JConfiguration dj = new DOM4JConfiguration(new File("D:\\objecte3\\hsbbs\\hsbbs\\WEB-INF\\classes\\configXML.xml"));
            LogUtil.getLogger().info("################## 测试绝对路径XMl配置文件 ##################");
            LogUtil.getLogger().info(dj.getString("dbaDB.type"));
            LogUtil.getLogger().info(dj.getString("dbaDB.driver"));
            LogUtil.getLogger().info(dj.getString("dbaDB.url"));
            LogUtil.getLogger().info(dj.getString("dbaDB.username"));
            LogUtil.getLogger().info(dj.getString("dbaDB.password"));
            LogUtil.getLogger().info("################## 测试绝对路径XMl配置文件 end ##################");
        } catch (Exception e) {
            LogUtil.getLogger().error(e);
        }
    }
}
