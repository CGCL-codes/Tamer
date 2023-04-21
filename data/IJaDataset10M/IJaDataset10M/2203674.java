package org.tolk.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.tolk.process.node.DeviceInterrogatorNode;

/**
 * Interface implemented by DataSources to enable it to respond to read events and keep on listening for events. Once a read event
 * occurs the read data should be passed forward to next Process Nodes for the DataSource.
 * 
 * @author Johan Roets
 * @author Werner van Rensburg
 */
public abstract class ReadEventDataSource extends DataSource implements Runnable, DisposableBean, InitializingBean {

    private static Logger logger = Logger.getLogger(ReadEventDataSource.class);

    protected boolean listenOnStartup = false;

    protected boolean listening = false;

    protected Thread thread;

    private List<DeviceInterrogatorNode> deviceInterrogatorNodes = new ArrayList<DeviceInterrogatorNode>();

    /**
     * Starts listening for read events and respond to the read event.
     */
    public void startListening() {
        this.listening = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * Stops listening and responding to read events.
     */
    public void stopListening() {
        this.listening = false;
    }

    /**
     * Specifies whether this DataSource should immediately start listening and responding to read events once the bean has been
     * successfully started.
     * 
     * @param listenOnStartup
     *            true if the bean should start listening on startup.
     */
    public void setListeningOnStartup(boolean listenOnStartup) {
        this.listenOnStartup = listenOnStartup;
        this.listening = listenOnStartup;
    }

    /**
     * @return true if this bean is setup to listen on startup.
     */
    public boolean getListeningOnStartup() {
        return this.listenOnStartup;
    }

    /**
     * see {@link Runnable#run()}
     */
    public void run() {
        while (this.listening) {
            try {
                String str = read();
                if (str != null) {
                    if (str.trim().length() > 0) {
                        fwdMessageToAllNextNodes(str);
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage());
                        }
                    }
                } else {
                    this.listening = false;
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * see {@link DisposableBean#destroy()}
     */
    public void destroy() throws Exception {
        stopListening();
    }

    /**
     * see {@link InitializingBean#afterPropertiesSet()}
     */
    public void afterPropertiesSet() throws Exception {
        if (this.listenOnStartup) {
            startListening();
        }
    }

    /**
     * Forwards a message to all next nodes, if there are any.
     * 
     * @param message
     */
    @Override
    public void fwdMessageToAllNextNodes(String message) {
        if (this.deviceInterrogatorNodes != null) {
            for (DeviceInterrogatorNode deviceInterrogatorNode : this.deviceInterrogatorNodes) {
                deviceInterrogatorNode.setDataSourceMessage(message);
            }
        }
        super.fwdMessageToAllNextNodes(message);
    }

    /**
     * @return the deviceInterrogatorNodes
     */
    public List<DeviceInterrogatorNode> getDeviceInterrogatorNodes() {
        return this.deviceInterrogatorNodes;
    }

    /**
     * @param deviceInterrogatorNodes
     *            the deviceInterrogatorNodes to set
     */
    public void setDeviceInterrogatorNodes(List<DeviceInterrogatorNode> deviceInterrogatorNodes) {
        this.deviceInterrogatorNodes = deviceInterrogatorNodes;
    }
}
