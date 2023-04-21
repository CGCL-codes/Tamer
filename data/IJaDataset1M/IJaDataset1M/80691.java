package org.albianj.controller.client;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import org.albianj.datetime.DateTime;
import org.albianj.io.Path;
import org.albianj.kernel.AlbianBootService;
import org.albianj.kernel.AlbianState;
import org.albianj.kernel.KernelSetting;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.protocol.mgr.EngineState;
import org.albianj.protocol.mgr.ClientIptable;
import org.albianj.service.FreeAlbianService;
import org.albianj.service.parser.PropertiesParser;
import org.albianj.socket.client.TcpClient;
import org.albianj.verify.Validate;

public class ControllerClientService extends FreeAlbianService implements IControllerClientService {

    private static final String path = "../config/controller.properties";

    public void init() {
        try {
            Properties props = PropertiesParser.load(Path.getExtendResourcePath(path));
            parser(props);
        } catch (Exception e) {
            AlbianLoggerService.error(e, "load the mgr properties file is error.");
            throw new RuntimeException(e);
        }
    }

    public void parser(Properties props) {
        String host = PropertiesParser.getValue(props, "host");
        if (Validate.isNullOrEmptyOrAllSpace(host)) {
            AlbianLoggerService.error("the mgr host is null or empty/");
            throw new RuntimeException("the mgr host is null or empty.");
        }
        String port = PropertiesParser.getValue(props, "port");
        String so_keepalive = PropertiesParser.getValue(props, "so_keepalive");
        String so_rcvbuf = PropertiesParser.getValue(props, "so_rcvbuf");
        String so_reuseaddr = PropertiesParser.getValue(props, "so_reuseaddr");
        String so_sndbuf = PropertiesParser.getValue(props, "so_sndbuf");
        String so_linger = PropertiesParser.getValue(props, "so_linger");
        String so_timeout = PropertiesParser.getValue(props, "so_timeout");
        String tcp_nodelay = PropertiesParser.getValue(props, "tcp_nodelay");
        String report_timespan = PropertiesParser.getValue(props, "report_timespan");
        ControllerClientSettings.setHost(host);
        if (!Validate.isNullOrEmptyOrAllSpace(port)) {
            ControllerClientSettings.setPort(new Integer(port));
        }
        if (!Validate.isNullOrEmptyOrAllSpace(so_keepalive)) {
            ControllerClientSettings.setKeepalive(new Boolean(so_keepalive));
        }
        if (!Validate.isNullOrEmptyOrAllSpace(so_rcvbuf)) {
            ControllerClientSettings.setReceiveBufferSize(new Integer(so_rcvbuf));
        }
        if (!Validate.isNullOrEmptyOrAllSpace(so_reuseaddr)) {
            ControllerClientSettings.setReuseAddress(new Boolean(so_reuseaddr));
        }
        if (!Validate.isNullOrEmptyOrAllSpace(so_sndbuf)) {
            ControllerClientSettings.setSendBufferSize(new Integer(so_sndbuf));
        }
        if (!Validate.isNullOrEmptyOrAllSpace(so_linger)) {
            ControllerClientSettings.setSoLinger(new Integer(so_linger));
        }
        if (!Validate.isNullOrEmptyOrAllSpace(so_timeout)) {
            ControllerClientSettings.setSoTimeout(new Integer(so_timeout));
        }
        if (!Validate.isNullOrEmptyOrAllSpace(tcp_nodelay)) {
            ControllerClientSettings.setTcpNoDelay(new Boolean(tcp_nodelay));
        }
        if (!Validate.isNullOrEmptyOrAllSpace(report_timespan)) {
            ControllerClientSettings.setReport_timespan(new Integer(report_timespan));
        }
    }

    @Override
    public void loading() throws RuntimeException {
        init();
        regedit();
        super.loading();
    }

    @Override
    public void unload() {
        logout();
        super.unload();
    }

    public void regedit() {
        ClientIptable table = new ClientIptable();
        TcpClient client = new TcpClient();
        Socket socket = null;
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            table.setIp(ip4.getHostAddress());
            table.setAppName(KernelSetting.getAppName());
            table.setKernelId(KernelSetting.getKernelId());
            table.setStartTime(DateTime.getDateTimeString(AlbianBootService.getStartDateTime()));
            table.setState(EngineState.Starting);
            table.setSerialId(AlbianBootService.getSerialId());
            socket = client.create(ControllerClientSettings.getHost(), ControllerClientSettings.getPort(), ControllerClientSettings.getKeepalive(), ControllerClientSettings.getReceiveBufferSize(), ControllerClientSettings.getReuseAddress(), ControllerClientSettings.getSendBufferSize(), ControllerClientSettings.getSoLinger(), ControllerClientSettings.getSoTimeout(), ControllerClientSettings.getTcpNoDelay());
            AlbianLoggerService.info("regedit message:%s.", table.toString());
            client.regist(socket, table);
        } catch (UnknownHostException e) {
            AlbianLoggerService.warn(e, "regedit the albianj state is error.");
        } finally {
            if (null != socket) client.close(socket);
        }
    }

    public void report() {
        Runnable run = new Runnable() {

            public void run() {
                while (true) {
                    if (AlbianState.Running != AlbianBootService.getLifeState()) {
                        unload();
                        break;
                    }
                    ClientIptable table = new ClientIptable();
                    TcpClient client = new TcpClient();
                    Socket socket = null;
                    try {
                        InetAddress ip4 = Inet4Address.getLocalHost();
                        table.setIp(ip4.getHostAddress());
                        table.setAppName(KernelSetting.getAppName());
                        table.setKernelId(KernelSetting.getKernelId());
                        table.setStartTime(DateTime.getDateTimeString(AlbianBootService.getStartDateTime()));
                        table.setSerialId(AlbianBootService.getSerialId());
                        table.setState(EngineState.Runing);
                        AlbianLoggerService.info("report message:%s.", table.toString());
                        socket = client.create(ControllerClientSettings.getHost(), ControllerClientSettings.getPort(), ControllerClientSettings.getKeepalive(), ControllerClientSettings.getReceiveBufferSize(), ControllerClientSettings.getReuseAddress(), ControllerClientSettings.getSendBufferSize(), ControllerClientSettings.getSoLinger(), ControllerClientSettings.getSoTimeout(), ControllerClientSettings.getTcpNoDelay());
                        client.report(socket, table);
                    } catch (UnknownHostException e) {
                        AlbianLoggerService.warn(e, "report the  albianj state is error.");
                    } finally {
                        if (null != socket) client.close(socket);
                    }
                    try {
                        Thread.sleep(ControllerClientSettings.getReport_timespan() * 1000);
                    } catch (InterruptedException e) {
                        AlbianLoggerService.error(e, "sleep the thread is error when report the  albianj state.");
                    }
                }
            }
        };
        run.run();
    }

    public void logout() {
        ClientIptable table = new ClientIptable();
        TcpClient client = new TcpClient();
        Socket socket = null;
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            table.setIp(ip4.getHostAddress());
            table.setAppName(KernelSetting.getAppName());
            table.setKernelId(KernelSetting.getKernelId());
            table.setStartTime(DateTime.getDateTimeString(AlbianBootService.getStartDateTime()));
            table.setSerialId(AlbianBootService.getSerialId());
            table.setState(EngineState.Stoped);
            AlbianLoggerService.info("logout message:%s.", table.toString());
            socket = client.create(ControllerClientSettings.getHost(), ControllerClientSettings.getPort(), ControllerClientSettings.getKeepalive(), ControllerClientSettings.getReceiveBufferSize(), ControllerClientSettings.getReuseAddress(), ControllerClientSettings.getSendBufferSize(), ControllerClientSettings.getSoLinger(), ControllerClientSettings.getSoTimeout(), ControllerClientSettings.getTcpNoDelay());
            client.logout(socket, table);
        } catch (UnknownHostException e) {
            AlbianLoggerService.warn(e, "report the  albianj state is error.");
        } finally {
            if (null != socket) client.close(socket);
        }
    }
}
