package com.cbsgmbh.xi.af.as2.jca.configuration;

import java.util.LinkedList;

@SuppressWarnings("unchecked")
public abstract class ConfigurationAbstractImpl {

    protected String adapterStatus;

    protected String authentication;

    protected String host;

    protected String password;

    protected String path;

    protected Integer port;

    protected String proxyHost;

    protected Integer proxyPort;

    protected String query;

    protected Boolean useProxy;

    protected String user;

    protected String protocol;

    protected String channelId;

    protected LinkedList receiverChannels;

    protected LinkedList senderChannels;

    protected String ungId;

    protected String unbId;

    protected String proxyPassword;

    protected String proxyUser;

    protected boolean useEncryption;

    protected boolean useSignature;

    protected String certificateForMdnSignature;

    protected String viewCertificateForMdnSignature;

    protected boolean mdnRequired;

    protected String micAlgMdn;

    protected String micAlgReq;

    /**
     * Getter for adapterStatus
     * @return String (active or inactive)
     */
    public String getAdapterStatus() {
        return this.adapterStatus;
    }

    /**
     * Getter for authentication mode
     * @return String (basic or none)
     */
    public String getAuthentication() {
        return this.authentication;
    }

    /**
     * Getter for target host
     * @return String (host)
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Getter for password
     * @return String (password)
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Getter for path within URL
     * @return String (path)
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for port within URL
     * @return Integer (port)
     */
    public Integer getPort() {
        return this.port;
    }

    /**
     * Getter for proxy host
     * @return String (proxy host)
     */
    public String getProxyHost() {
        return this.proxyHost;
    }

    /**
     * Getter for proxy port
     * @return Integer (proxy port)
     */
    public Integer getProxyPort() {
        return this.proxyPort;
    }

    /**
     * Getter for query within the URL
     * @return String (query)
     */
    public String getQuery() {
        return this.query;
    }

    /**
     * Getter for usage of proxy
     * @return Boolean (true or false)
     */
    public Boolean getUseProxy() {
        return this.useProxy;
    }

    /**
     * Getter for user
     * @return String (user)
     */
    public String getUser() {
        return this.user;
    }

    /**
     * Getter for protocol
     * @return String (protocol)
     */
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Getter for channel id
     * @return String (channel id)
     */
    public String getChannelId() {
        return this.channelId;
    }

    /**
     * Getter for list of receiver channels
     * @return LinkedList (list of receiver channels)
     */
    public LinkedList getReceiverChannels() {
        return this.receiverChannels;
    }

    /**
     * Getter for list of sender channels
     * @return LinkedList (list of sender channels)
     */
    public LinkedList getSenderChannels() {
        return this.senderChannels;
    }

    /**
     * Getter for UNG identifier
     * @return String (UNG identifier)
     */
    public String getUngId() {
        return this.ungId;
    }

    /**
     * Getter for UNB identifier
     * @return String (UNB identifier)
     */
    public String getUnbIdx() {
        return this.unbId;
    }

    /**
     * Getter for proxy password
     * @return String
     */
    public String getProxyPassword() {
        return this.proxyPassword;
    }

    /**
     * Getter for proxy user
     * @return String
     */
    public String getProxyUser() {
        return this.proxyUser;
    }

    /**
     * Getter for encryption mode
     * @return boolean true or false
     */
    public boolean getUseEncryption() {
        return this.useEncryption;
    }

    /**
     * Getter for signature mode
     * @return boolean true or false
     */
    public boolean getUseSignature() {
        return this.useSignature;
    }

    /**
     * Getter for the certificate for signature of MDN
     * @param certificateForMdnSignature
     * @return String
     */
    public String getCertificateForMdnSignature() {
        return this.certificateForMdnSignature;
    }

    /**
     * Getter for the certificate view for signature of MDN
     * @param viewCertificateForMdnSignature
     * @return String
     */
    public String getViewCertificateForMdnSignature() {
        return this.viewCertificateForMdnSignature;
    }

    /**
     * Getter for usage of MDN
     * @return boolean
     */
    public boolean getMdnRequired() {
        return this.mdnRequired;
    }

    /**
     * Getter for mic algorithm for MDN
     * @return String
     */
    public String getMicAlgMdn() {
        return this.micAlgMdn;
    }

    /**
     * Getter for mic algorithm for request
     * @return String
     */
    public String getMicAlgReq() {
        return this.micAlgReq;
    }
}
