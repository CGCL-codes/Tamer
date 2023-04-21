package org.dizem.sanguosha.controller;

import org.apache.log4j.Logger;
import org.dizem.common.JSONUtil;
import org.dizem.sanguosha.model.vo.SGSPacket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import static org.dizem.sanguosha.model.constants.Constants.*;

/**
 * 客户端监控
 * 用于接收服务器发来的请求
 * <p/>
 * User: dizem
 * Time: 11-4-23 下午9:57
 */
public class GameClientMonitor extends Thread {

    private static Logger log = Logger.getLogger(GameClientMonitor.class);

    /**
	 * 客户端监听端口
	 */
    public int clientPort = 3000;

    /**
	 * 客户端控制类
	 */
    private GameClient client;

    /**
	 * 是否开始监听
	 */
    private boolean isReady = false;

    /**
	 * 客户端调度
	 */
    private ClientDispatcher dispatcher;

    /**
	 * 构造函数
	 *
	 * @param client 客户端
	 */
    public GameClientMonitor(GameClient client) {
        this.client = client;
        this.dispatcher = new ClientDispatcher(client);
    }

    /**
	 * 线程运行过程
	 */
    @Override
    public void run() {
        try {
            DatagramSocket ds = createSocket();
            isReady = true;
            while (true) {
                byte[] data = new byte[DATA_PACKET_SIZE];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                ds.receive(packet);
                String jsonString = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                SGSPacket dp = (SGSPacket) JSONUtil.convertToVO(jsonString, SGSPacket.class);
                log.info("receive:" + dp.getOperation());
                dispatcher.dispatch(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
	 * 递归探测端口是否可用，并创建UDP连接
	 *
	 * @return
	 */
    private DatagramSocket createSocket() {
        try {
            DatagramSocket ds = new DatagramSocket(clientPort);
            log.info("客户端在" + clientPort + "启动");
            return ds;
        } catch (SocketException e) {
            log.info("客户端端口" + clientPort + "被占用");
            clientPort++;
            return createSocket();
        }
    }

    public int getClientPort() {
        return clientPort;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
