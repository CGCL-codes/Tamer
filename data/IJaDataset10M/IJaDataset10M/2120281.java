package org.coos.messaging.plugin;

import org.coos.messaging.Channel;
import org.coos.messaging.Connectable;
import org.coos.messaging.ConnectingException;
import org.coos.messaging.Endpoint;
import org.coos.messaging.Link;
import org.coos.messaging.Message;
import org.coos.messaging.Processor;
import org.coos.messaging.ProcessorException;
import org.coos.messaging.Transport;
import org.coos.messaging.impl.DefaultChannel;
import org.coos.messaging.impl.DefaultMessage;
import org.coos.messaging.impl.DefaultProcessor;
import org.coos.messaging.util.LogFactory;
import org.coos.messaging.util.UuidHelper;
import java.util.Hashtable;

/**
 * A channel connecting plugins to coos instances (router nodes)
 *
 * @author Knut Eilif Husa, Tellu AS
 *
 */
public class PluginChannel extends DefaultChannel {

    private static final String PROPERTY_CONNECTION_TIMEOUT = "connectionTimeout";

    private static final String PROPERTY_STARTUP_ORDERED = "startupOrdered";

    private int connectionTimeout = 10000;

    private boolean startupOrdered = true;

    private String connectionErrorCause = null;

    public PluginChannel() {
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public boolean isStartupOrdered() {
        return startupOrdered;
    }

    public void setStartupOrdered(boolean startupOrdered) {
        this.startupOrdered = startupOrdered;
    }

    public void addInFilter(Processor filter) {
        inLink.addFilterProcessor(filter);
    }

    public void addOutFilter(Processor filter) {
        outLink.addFilterProcessor(filter);
    }

    public void setProperties(Hashtable properties) {
        super.setProperties(properties);
        if (properties.get(PROPERTY_CONNECTION_TIMEOUT) != null) {
            connectionTimeout = Integer.valueOf((String) properties.get(PROPERTY_CONNECTION_TIMEOUT)).intValue();
        }
    }

    public synchronized void connect(Connectable connectable) throws ConnectingException {
        if (!(connectable instanceof Endpoint)) {
            throw new ConnectingException("This channel can only be connected to Endpoints.");
        }
        if (connected) {
            throw new ConnectingException("This channel is already connected.");
        }
        this.connectable = connectable;
        final Endpoint endpoint = (Endpoint) connectable;
        try {
            if (transport == null) {
                String className = "org.coos.messaging.transport.JvmTransport";
                Class transportClass = Class.forName(className);
                transport = (Transport) transportClass.newInstance();
                transport.setChannel(this);
            }
            transport.setChainedProcessor(new DefaultProcessor() {

                public void processMessage(Message msg) throws ProcessorException {
                    synchronized (PluginChannel.this) {
                        if (msg.getType().equals(Message.TYPE_ERROR)) {
                            endpoint.setEndpointState(Endpoint.STATE_STARTUP_FAILED);
                            connectionErrorCause = msg.getHeader(Message.ERROR_REASON);
                        } else if (msg.getName().equals(CONNECT_ACK)) {
                            String allocUuid = msg.getHeader(CONNECT_ALLOCATED_UUID);
                            String routerUuid = msg.getHeader(CONNECT_ROUTER_UUID);
                            transport.setChainedProcessor(inLink);
                            outLink.setDestinationUuid(routerUuid);
                            if (allocUuid != null) {
                                inLink.setDestinationUuid(allocUuid);
                                endpoint.setEndpointUuid(allocUuid);
                                if (endpoint.getEndpointUri() == null) {
                                    endpoint.setName(allocUuid);
                                    endpoint.setEndpointUri("coos://" + allocUuid);
                                }
                                if (segment.equals("")) {
                                    segment = UuidHelper.getSegmentFromSegmentOrEndpointUuid(allocUuid);
                                }
                            }
                            connected = true;
                        }
                        PluginChannel.this.notifyAll();
                        if (!connected && !startupOrdered) {
                            throwConnectingException();
                        }
                    }
                }
            });
            DefaultMessage msg = new DefaultMessage(Channel.CONNECT);
            if (endpoint.getEndpointUuid() == null) {
                msg.setHeader(CONNECT_SEGMENT, segment);
            } else {
                msg.setHeader(CONNECT_UUID, endpoint.getEndpointUuid());
            }
            Hashtable props = new Hashtable();
            props.put(Link.ALIASES, endpoint.getAliases());
            msg.setBody(props);
            inLink.setChainedProcessor(endpoint);
            for (int i = 0; i < protocols.size(); i++) {
                endpoint.addLink((String) protocols.elementAt(i), outLink);
            }
            outLink.setChainedProcessor(transport);
            transport.start();
            outLink.start();
            inLink.start();
            transport.processMessage(msg);
            if (startupOrdered) {
                if (!connected) {
                    this.wait(connectionTimeout);
                }
                if (!connected) {
                    throwConnectingException();
                }
            }
        } catch (Exception e) {
            disconnect();
            throw new ConnectingException("Connecting channel: " + name + " failed, cause: " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    private void throwConnectingException() throws ConnectingException {
        if (connectionErrorCause == null) {
            throw new ConnectingException("Timeout, no response from connecting coos router");
        } else {
            throw new ConnectingException(connectionErrorCause);
        }
    }

    public synchronized void disconnect() {
        if (!connected) {
            return;
        }
        try {
            connected = false;
            if (connectable != null) {
                connectable.removeLinkById(outLink.getLinkId());
            }
            outLink.stop();
            inLink.stop();
            if (transport != null) {
                transport.stop();
            }
        } catch (Exception e) {
            LogFactory.getLog(this.getClass()).warn("Exception when diconnecting", e);
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
