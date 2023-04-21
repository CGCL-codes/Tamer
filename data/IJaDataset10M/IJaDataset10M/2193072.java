package org.openremote.controller.protocol.knx.ip;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import org.openremote.controller.protocol.knx.ip.message.Hpai;
import org.openremote.controller.protocol.knx.ip.message.IpConnectReq;
import org.openremote.controller.protocol.knx.ip.message.IpConnectResp;
import org.openremote.controller.protocol.knx.ip.message.IpConnectionStateReq;
import org.openremote.controller.protocol.knx.ip.message.IpConnectionStateResp;
import org.openremote.controller.protocol.knx.ip.message.IpDisconnectReq;
import org.openremote.controller.protocol.knx.ip.message.IpDisconnectResp;
import org.openremote.controller.protocol.knx.ip.message.IpMessage;
import org.openremote.controller.protocol.knx.ip.message.IpTunnelingAck;
import org.openremote.controller.protocol.knx.ip.message.IpTunnelingReq;

public class IpTunnelClient implements IpProcessorListener {

    private int channelId;

    private int seqCounter;

    private IpMessageListener messageListener;

    private IpProcessor processor;

    private InetSocketAddress destControlEndpointAddr;

    private InetSocketAddress destDataEndpointAddr;

    private Timer heartBeat;

    private Thread shutdownHook;

    public IpTunnelClient(InetAddress srcAddr, InetSocketAddress destControlEndpointAddr) {
        this.destControlEndpointAddr = destControlEndpointAddr;
        this.processor = new IpProcessor(srcAddr, this);
        this.destDataEndpointAddr = null;
        this.shutdownHook = new ShutdownHook();
    }

    public void register(IpMessageListener l) {
        this.messageListener = l;
    }

    public void unregister() {
        this.messageListener = null;
    }

    public synchronized void service(byte[] message) throws KnxIpException, InterruptedException, IOException {
        if (this.destDataEndpointAddr == null) this.connect();
        IpMessage resp = this.processor.service(new IpTunnelingReq(this.channelId, this.seqCounter, message), this.destDataEndpointAddr);
        if (resp == null) {
            throw new KnxIpException("No response");
        } else {
            if (resp instanceof IpTunnelingAck) {
                IpTunnelingAck cr = (IpTunnelingAck) resp;
                if (cr.getChannelId() == this.channelId) {
                    if (cr.getSeqCounter() == this.seqCounter) {
                        int st = cr.getStatus();
                        if (st != IpTunnelingAck.OK) {
                            throw new KnxIpException("Response error : " + st);
                        }
                    } else {
                        this.disconnect();
                        throw new KnxIpException("Tunnel failed, response wrong sequence counter value, expected " + this.seqCounter + ", got " + cr.getSeqCounter());
                    }
                } else {
                    this.disconnect();
                    throw new KnxIpException("Tunnel failed, response wrong channel id");
                }
            }
        }
        this.seqCounter = (this.seqCounter + 1 & 0xFF);
    }

    public synchronized void connect() throws KnxIpException, InterruptedException, IOException {
        if (this.destDataEndpointAddr != null) throw new KnxIpException("Already connected");
        this.processor.start();
        Hpai ep = new Hpai(this.processor.getSrcAddr());
        IpMessage resp = this.processor.service(new IpConnectReq(ep, ep), this.destControlEndpointAddr);
        if (resp instanceof IpConnectResp) {
            IpConnectResp cr = (IpConnectResp) resp;
            int st = cr.getStatus();
            if (st == IpConnectResp.OK) {
                this.channelId = cr.getChannelId();
                this.destDataEndpointAddr = cr.getDataEndpoint().getAddress();
                this.heartBeat = new Timer("KNX IP heartbeat");
                this.heartBeat.schedule(new HeartBeatTask(), 0, 60000);
                Runtime.getRuntime().addShutdownHook(IpTunnelClient.this.shutdownHook);
            } else {
                throw new KnxIpException("Connect failed, response error : " + st);
            }
        } else {
            throw new KnxIpException("Connect failed, unexpected response");
        }
    }

    public synchronized void disconnect() throws KnxIpException, InterruptedException, IOException {
        if (this.destDataEndpointAddr == null) throw new KnxIpException("Not connected");
        IpMessage resp = this.processor.service(new IpDisconnectReq(this.channelId, new Hpai(this.processor.getSrcAddr())), this.destDataEndpointAddr);
        if (resp instanceof IpDisconnectResp) {
            IpDisconnectResp cr = (IpDisconnectResp) resp;
            if (this.channelId == cr.getChannelId()) {
                Runtime.getRuntime().removeShutdownHook(IpTunnelClient.this.shutdownHook);
                this.heartBeat.cancel();
                this.processor.stop();
                this.destDataEndpointAddr = null;
                int st = cr.getStatus();
                if (st != IpDisconnectResp.OK) {
                    throw new KnxIpException("Response error : " + st);
                }
            } else {
                throw new KnxIpException("Disconnect failed, response wrong channel id");
            }
        } else {
            throw new KnxIpException("Disconnect failed, unexpected response");
        }
    }

    @Override
    public void notifyMessage(IpMessage message) {
        if (message instanceof IpTunnelingReq) {
            IpTunnelingReq req = (IpTunnelingReq) message;
            if (req.getChannelId() == this.channelId) {
                int seqCounter = req.getSeqCounter();
                try {
                    this.processor.send(new IpTunnelingAck(this.channelId, seqCounter, IpTunnelingAck.OK), this.destDataEndpointAddr);
                } catch (IOException e) {
                }
                IpMessageListener l = this.messageListener;
                if (l != null) {
                    l.receive(req.getcEmiFrame());
                }
            } else {
            }
        }
    }

    private class HeartBeatTask extends TimerTask {

        @Override
        public void run() {
            int nbErrs = 0;
            while (nbErrs < 3) {
                try {
                    this.monitor();
                    return;
                } catch (KnxIpException e) {
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                }
                nbErrs++;
            }
            try {
                IpTunnelClient.this.disconnect();
            } catch (KnxIpException e) {
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
            }
        }

        private void monitor() throws KnxIpException, InterruptedException, IOException {
            Hpai ep = new Hpai(IpTunnelClient.this.processor.getSrcAddr());
            IpMessage resp = IpTunnelClient.this.processor.service(new IpConnectionStateReq(IpTunnelClient.this.channelId, ep), IpTunnelClient.this.destControlEndpointAddr);
            if (resp instanceof IpConnectionStateResp) {
                IpConnectionStateResp cr = (IpConnectionStateResp) resp;
                int cId = cr.getChannelId();
                if (cId == IpTunnelClient.this.channelId) {
                    int st = cr.getStatus();
                    if (st != IpConnectResp.OK) {
                        throw new KnxIpException("Monitor failed, response error : " + st);
                    }
                } else {
                    throw new KnxIpException("Monitor failed, wrong channel id : " + cId);
                }
            } else {
                throw new KnxIpException("Monitor failed, unexepected response");
            }
        }
    }

    private class ShutdownHook extends Thread {

        @Override
        public void run() {
            try {
                IpTunnelClient.this.disconnect();
            } catch (KnxIpException e) {
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
            }
        }
    }
}
