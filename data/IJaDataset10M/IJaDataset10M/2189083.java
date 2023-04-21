package com.gjzq.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;
import com.thinkive.base.config.Configuration;

/**
 * ����:  passport��ؼ���
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 liwei
 * �汾:	 1.0
 * ��������: 2008-8-27
 * ����ʱ��: ����02:28:45
 */
public class PassportGateWayListener extends Thread {

    private static Logger logger = Logger.getLogger(PassportGateWayListener.class);

    int sleeptimes = 20000;

    GateWayManager gateWayManage = new GateWayManager();

    public PassportGateWayListener() {
        String IP_Str = Configuration.getString("passport.gateway");
        sleeptimes = Configuration.getInt("passport.scanInterval");
        String[] gateArry = IP_Str.split("\\|");
        int port = 0;
        if (IP_Str != null) {
            for (int i = 0; i < gateArry.length; i++) {
                String[] ipPortArry = gateArry[i].split(":");
                String ipStr = ipPortArry[0];
                if (ipPortArry.length > 1) {
                    port = Integer.parseInt(ipPortArry[1]);
                }
                NetAddress netAddr = new NetAddress();
                netAddr.setIP(ipStr);
                netAddr.setPort(port);
                netAddr.setAlive(false);
                gateWayManage.addPassportServer(netAddr);
            }
        }
    }

    public void run() {
        while (true) {
            if (gateWayManage.getPassportIPListSize() > 0) {
                for (int i = 0; i < gateWayManage.getPassportIPListSize(); i++) {
                    NetAddress nt = (NetAddress) gateWayManage.getPassportIPList().get(i);
                    String ipStr = nt.getIP();
                    int port = nt.getPort();
                    boolean isAlive = connectServer(ipStr, port, sleeptimes);
                    nt.setAlive(isAlive);
                    if (!isAlive) logger.info("passport��ط�����[" + ipStr + ":" + port + "]�Ƿ���=" + isAlive);
                }
            } else {
                logger.info("û��ȡ��passport���IP��ַ");
                break;
            }
            try {
                Thread.sleep(sleeptimes);
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
    }

    /**
	 *
	 * @param server   ������IP��ַ
	 * @param port     �˿�
	 * @param timeout  �趨���ٺ���connect��ʱ (��λ:����)
	 * @return
	 */
    private boolean connectServer(String server, int port, int timeout) {
        SocketChannel channel = null;
        boolean isAlive = false;
        try {
            channel = SocketChannel.open();
            SocketAddress address = new InetSocketAddress(server, port);
            channel.configureBlocking(true);
            channel.socket().setSoTimeout(timeout);
            isAlive = channel.connect(address);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            return false;
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
        }
        return isAlive;
    }
}
