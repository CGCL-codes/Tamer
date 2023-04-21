package org.zkoss.eclipse.rpc;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * @author Ian Tsai
 * 
 * This is an message which denotes how receiver works.<br>
 * 
 */
public class Instruction implements CallbackRef {

    private static final long serialVersionUID = -5125786390637866848L;

    public static final String UPDATE_DOCUMENT = "UPDATE_DOCUMENT";

    public static final String CREATE_DOCUMENT = "CREATE_DOCUMENT";

    public static final String LOAD_DOCUMENT = "LOAD_DOCUMENT";

    public static final String WEB_APP_CHANNEL_REGISTER = "WEB_APP_CHANNEL_REGISTER";

    private Serializable[] arguments;

    private String signature;

    private String callbackId;

    private int port;

    private transient Callback callback;

    private boolean echo;

    /**
	 * 
	 * @param callback
	 */
    public Instruction(Callback cb) {
        callback = cb;
        echo = callback != null;
        port = RmiServices.getCurrentRmiServer().getRegistyServicePort();
        callbackId = ":" + port + ":" + Callback.class.getName() + ":" + new Date().getTime();
    }

    /**
	 * 
	 * @return
	 */
    public int getSenderPort() {
        return port;
    }

    /**
	 * 
	 * @param channel
	 * @throws RemoteException 
	 */
    public void send(RmiChannel channel) throws RemoteException {
        if (callback != null) {
            CallbackStore store = RmiServices.getCurrentCallbackStore();
            store.add(callbackId, callback);
        }
        channel.send(this);
    }

    /**
	 * The data that receiver needed to process<br>
	 * @return arguments that needed while processing.
	 */
    public Serializable[] getArguments() {
        return arguments;
    }

    /**
	 * The data that messenger should provide while remote receiver processing. 
	 * @param arguments  The data that messenger should provide
	 */
    public void setArguments(Serializable[] arguments) {
        this.arguments = arguments;
    }

    /**
	 * get the Instruction operation code.
	 * @return
	 */
    public String getSignature() {
        return signature;
    }

    /**
	 * Set the instruction code.
	 * @param signature
	 */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void doCallback(Serializable rs, Throwable ex) {
        if (!isEcho()) return;
        try {
            RmiChannel channel = RmiServices.getOtherRmiChannel(port);
            channel.send(new SerializableCallbackImpl(callbackId, rs, ex));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
	 * true if this instruction is a callback instruction, false otherwise.
	 * @return
	 */
    public boolean isEcho() {
        return echo;
    }
}
