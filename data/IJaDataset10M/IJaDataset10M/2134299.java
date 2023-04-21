package jtmsmon.im;

import net.kano.joscar.flap.ClientFlapConn;
import net.kano.joscar.flap.FlapPacketEvent;
import net.kano.joscar.flap.FlapPacketListener;
import net.kano.joscar.flapcmd.DefaultFlapCmdFactory;
import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.net.ClientConnEvent;
import net.kano.joscar.net.ClientConnListener;
import net.kano.joscar.net.ConnDescriptor;
import net.kano.joscar.net.ConnProcessorExceptionEvent;
import net.kano.joscar.net.ConnProcessorExceptionHandler;
import net.kano.joscar.snac.ClientSnacProcessor;
import net.kano.joscar.snac.FamilyVersionPreprocessor;
import net.kano.joscar.snac.SnacPacketEvent;
import net.kano.joscar.snac.SnacPacketListener;
import net.kano.joscar.snac.SnacRequest;
import net.kano.joscar.snac.SnacRequestAdapter;
import net.kano.joscar.snac.SnacRequestListener;
import net.kano.joscar.snac.SnacResponseEvent;
import net.kano.joscar.snaccmd.DefaultClientFactoryList;

/**
 * Class description
 *
 *
 * @version    Enter version here..., 2006-12-02
 * @author     Enter your name here...
 */
public abstract class AbstractFlapConn extends ClientFlapConn {

    /**
   * Constructs ...
   *
   *
   * @param cd
   */
    protected AbstractFlapConn(ConnDescriptor cd) {
        super(cd);
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public SnacRequestListener getGenericReqListener() {
        return genericReqListener;
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public ClientSnacProcessor getSnacProcessor() {
        return snacProcessor;
    }

    /**
   * Method description
   *
   *
   * @param cmd
   *
   * @return
   */
    SnacRequest request(SnacCommand cmd) {
        return request(cmd, null);
    }

    /**
   * Method description
   *
   *
   * @param cmd
   * @param listener
   *
   * @return
   */
    SnacRequest request(SnacCommand cmd, SnacRequestListener listener) {
        SnacRequest req = new SnacRequest(cmd, listener);
        sendRequest(req);
        return req;
    }

    /**
   * Method description
   *
   *
   * @param req
   */
    void sendRequest(SnacRequest req) {
        if (!req.hasListeners()) {
            req.addListener(genericReqListener);
        }
        snacProcessor.sendSnac(req);
    }

    /**
   * Method description
   *
   *
   * @param e
   */
    protected abstract void handleFlapPacket(FlapPacketEvent e);

    /**
   * Method description
   *
   *
   * @param e
   */
    protected abstract void handleSnacPacket(SnacPacketEvent e);

    /**
   * Method description
   *
   *
   * @param e
   */
    protected abstract void handleSnacResponse(SnacResponseEvent e);

    /**
   * Method description
   *
   *
   * @param e
   */
    protected abstract void handleStateChange(ClientConnEvent e);

    /** Field description */
    protected ClientSnacProcessor snacProcessor = new ClientSnacProcessor(getFlapProcessor());

    /** Field description */
    protected SnacRequestListener genericReqListener = new SnacRequestAdapter() {

        public void handleResponse(SnacResponseEvent e) {
            handleSnacResponse(e);
        }
    };

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
        getFlapProcessor().addExceptionHandler(new ConnProcessorExceptionHandler() {

            public void handleException(ConnProcessorExceptionEvent event) {
                System.err.println(event.getType() + " FLAP ERROR: " + event.getException().getMessage());
                event.getException().printStackTrace();
            }
        });
        snacProcessor.addPacketListener(new SnacPacketListener() {

            public void handleSnacPacket(SnacPacketEvent e) {
                AbstractFlapConn.this.handleSnacPacket(e);
            }
        });
    }
}
