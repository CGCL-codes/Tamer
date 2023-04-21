package edu.tufts.vue.collab.im;

import net.kano.joscar.flap.ClientFlapConn;
import net.kano.joscar.flap.FlapExceptionEvent;
import net.kano.joscar.flap.FlapExceptionHandler;
import net.kano.joscar.flap.FlapPacketEvent;
import net.kano.joscar.flap.FlapPacketListener;
import net.kano.joscar.flapcmd.DefaultFlapCmdFactory;
import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.net.ClientConnEvent;
import net.kano.joscar.net.ClientConnListener;
import net.kano.joscar.snac.ClientSnacProcessor;
import net.kano.joscar.snac.FamilyVersionPreprocessor;
import net.kano.joscar.snac.SnacPacketEvent;
import net.kano.joscar.snac.SnacPacketListener;
import net.kano.joscar.snac.SnacRequest;
import net.kano.joscar.snac.SnacRequestAdapter;
import net.kano.joscar.snac.SnacRequestListener;
import net.kano.joscar.snac.SnacResponseEvent;
import net.kano.joscar.snaccmd.DefaultClientFactoryList;
import java.net.InetAddress;

public abstract class AbstractFlapConn extends ClientFlapConn {

    protected VUEAim tester;

    protected ClientSnacProcessor snacProcessor = new ClientSnacProcessor(getFlapProcessor());

    {
        getFlapProcessor().setFlapCmdFactory(new DefaultFlapCmdFactory());
        snacProcessor.addPreprocessor(new FamilyVersionPreprocessor());
        snacProcessor.getCmdFactoryMgr().setDefaultFactoryList(new DefaultClientFactoryList());
        addConnListener(new ClientConnListener() {

            public void stateChanged(ClientConnEvent e) {
                handleStateChange(e);
            }
        });
        getFlapProcessor().addPacketListener(new FlapPacketListener() {

            public void handleFlapPacket(FlapPacketEvent e) {
                AbstractFlapConn.this.handleFlapPacket(e);
            }
        });
        getFlapProcessor().addExceptionHandler(new FlapExceptionHandler() {

            public void handleException(FlapExceptionEvent event) {
                System.out.println(event.getType() + " FLAP ERROR: " + event.getException().getMessage());
                event.getException().printStackTrace();
            }
        });
        snacProcessor.addPacketListener(new SnacPacketListener() {

            public void handleSnacPacket(SnacPacketEvent e) {
                AbstractFlapConn.this.handleSnacPacket(e);
            }
        });
    }

    protected SnacRequestListener genericReqListener = new SnacRequestAdapter() {

        public void handleResponse(SnacResponseEvent e) {
            handleSnacResponse(e);
        }
    };

    public AbstractFlapConn(VUEAim tester) {
        this.tester = tester;
    }

    public AbstractFlapConn(String host, int port, VUEAim tester) {
        super(host, port);
        this.tester = tester;
    }

    public AbstractFlapConn(InetAddress ip, int port, VUEAim tester) {
        super(ip, port);
        this.tester = tester;
    }

    public SnacRequestListener getGenericReqListener() {
        return genericReqListener;
    }

    public ClientSnacProcessor getSnacProcessor() {
        return snacProcessor;
    }

    public VUEAim getJoscarTester() {
        return tester;
    }

    public void sendRequest(SnacRequest req) {
        if (!req.hasListeners()) req.addListener(genericReqListener);
        snacProcessor.sendSnac(req);
    }

    SnacRequest request(SnacCommand cmd) {
        return request(cmd, null);
    }

    SnacRequest request(SnacCommand cmd, SnacRequestListener listener) {
        SnacRequest req = new SnacRequest(cmd, listener);
        sendRequest(req);
        return req;
    }

    protected abstract void handleStateChange(ClientConnEvent e);

    protected abstract void handleFlapPacket(FlapPacketEvent e);

    protected abstract void handleSnacPacket(SnacPacketEvent e);

    protected abstract void handleSnacResponse(SnacResponseEvent e);
}
