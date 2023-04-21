package mybridge.core.server;

import org.apache.log4j.PropertyConfigurator;
import xnet.core.server.*;

public class MyBridgeServer {

    boolean keepalive = true;

    int threadNum = Runtime.getRuntime().availableProcessors() + 1;

    int port = 8306;

    String ip = "0.0.0.0";

    int maxConnection = 1000;

    String userName = "root";

    String passWord = "";

    Class<?> handleClass;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean isKeepalive() {
        return keepalive;
    }

    public void setKeepalive(boolean keepalive) {
        this.keepalive = keepalive;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public Class<?> getHandleClass() {
        return handleClass;
    }

    public void setHandleClass(Class<?> handleClass) {
        this.handleClass = handleClass;
    }

    /**
	 * @param args
	 * @throws Exception
	 */
    public void run() throws Exception {
        PropertyConfigurator.configure("./conf/log4j.properties");
        Config config = new Config();
        config.keepalive = true;
        config.session = MyBridgeSession.class;
        config.threadNum = threadNum;
        config.port = port;
        config.keepalive = keepalive;
        config.ip = ip;
        config.maxConnection = maxConnection;
        MyBridgeProtocal.handleClass = handleClass;
        MyBridgeProtocal.userName = userName;
        MyBridgeProtocal.passWord = passWord;
        Server server = new Server(config);
        server.run();
    }
}
