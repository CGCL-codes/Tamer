package org.allesta.wsabi.monitor.core.dto;

import java.io.Serializable;

/**
 * DOCUMENT ME!
 *
 * @author Allesta, LLC
 * @version $Revision: 1.1 $ 
 */
public class MessageContextDTO implements Serializable {

    private static final long serialVersionUID = 1107389717740594310L;

    private String id;

    private long eventTimestamp;

    private String clientAddress;

    private String clientId;

    private int clientRequestByteCount;

    private String serverAddress;

    private String serviceId;

    private int serviceResponseByteCount;

    private int serviceResponseTime;

    private String serviceMethod;

    private String serviceParameters;

    private byte[] clientMessage;

    private byte[] clientHeaders;

    private byte[] serviceMessage;

    private byte[] serviceHeaders;

    private boolean fault;

    private boolean timeout;

    /**
     * DOCUMENT ME!
     *
     * @param clientAddress DOCUMENT ME!
     */
    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClientAddress() {
        return clientAddress;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientHeaders DOCUMENT ME!
     */
    public void setClientHeaders(byte[] clientHeaders) {
        this.clientHeaders = clientHeaders;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getClientHeaders() {
        return clientHeaders;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientId DOCUMENT ME!
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientMessage DOCUMENT ME!
     */
    public void setClientMessage(byte[] clientMessage) {
        this.clientMessage = clientMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getClientMessage() {
        return clientMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientRequestByteCount DOCUMENT ME!
     */
    public void setClientRequestByteCount(int clientRequestByteCount) {
        this.clientRequestByteCount = clientRequestByteCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getClientRequestByteCount() {
        return clientRequestByteCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param eventTimestamp DOCUMENT ME!
     */
    public void setEventTimestamp(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getEventTimestamp() {
        return eventTimestamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fault DOCUMENT ME!
     */
    public void setFault(boolean fault) {
        this.fault = fault;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFault() {
        return fault;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serverAddress DOCUMENT ME!
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serviceHeaders DOCUMENT ME!
     */
    public void setServiceHeaders(byte[] serviceHeaders) {
        this.serviceHeaders = serviceHeaders;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getServiceHeaders() {
        return serviceHeaders;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serviceId DOCUMENT ME!
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serviceMessage DOCUMENT ME!
     */
    public void setServiceMessage(byte[] serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getServiceMessage() {
        return serviceMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serviceMethod DOCUMENT ME!
     */
    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getServiceMethod() {
        return serviceMethod;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serviceParameters DOCUMENT ME!
     */
    public void setServiceParameters(String serviceParameters) {
        this.serviceParameters = serviceParameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getServiceParameters() {
        return serviceParameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serviceResponseByteCount DOCUMENT ME!
     */
    public void setServiceResponseByteCount(int serviceResponseByteCount) {
        this.serviceResponseByteCount = serviceResponseByteCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getServiceResponseByteCount() {
        return serviceResponseByteCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serviceResponseTime DOCUMENT ME!
     */
    public void setServiceResponseTime(int serviceResponseTime) {
        this.serviceResponseTime = serviceResponseTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getServiceResponseTime() {
        return serviceResponseTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param timeout DOCUMENT ME!
     */
    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTimeout() {
        return timeout;
    }
}
