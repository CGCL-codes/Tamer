package net.turingcomplete.phosphor.shared;

import net.turingcomplete.phosphor.shared.*;
import java.util.*;

/**
 * Super class of ServerOptions and LoadServerOptions.
 * <p>
 * REVISION HISTORY:
 * <p>
 */
public class GeneralServerOptions extends LimitOptions {

    private int maxClientConnections;

    private int maxServerConnections;

    private int clientConnectionSocketQueueLength;

    private int serverConnectionSocketQueueLength;

    private boolean updateLoadServer;

    private int numAttemptsUpdateLoadServer;

    private int defaultRegistrationRenewalTime;

    private int minRegistrationRenewalTime;

    private int preemptiveRegistrationRenewalTime;

    private int retryRegistrationRenewalTime;

    private int loadServerToTimeout;

    public GeneralServerOptions(String fileName) {
        super(fileName);
        loadMyOptions();
    }

    public void loadOptions() {
        super.loadOptions();
        loadMyOptions();
    }

    private void loadMyOptions() {
        clientConnectionSocketQueueLength = 3;
        serverConnectionSocketQueueLength = 2;
        updateLoadServer = true;
        numAttemptsUpdateLoadServer = 10;
        defaultRegistrationRenewalTime = 1000 * 60 * 30;
        minRegistrationRenewalTime = 60 * 1000;
        preemptiveRegistrationRenewalTime = 60 * 1000;
        retryRegistrationRenewalTime = 10 * 1000;
        loadServerToTimeout = 10 * 1000;
        maxClientConnections = 400;
        maxServerConnections = 10;
    }

    public int getServerConnectionSocketQueueLength() {
        return get("serverConnectionSocketQueueLength", serverConnectionSocketQueueLength);
    }

    public void setServerConnectionSocketQueueLength(int val) {
        set("serverConnectionSocketQueueLength", val);
    }

    public int getClientConnectionSocketQueueLength() {
        return get("clientConnectionSocketQueueLength", clientConnectionSocketQueueLength);
    }

    public void setClientConnectionSocketQueueLength(int val) {
        set("clientConnectionSocketQueueLength", val);
    }

    public int getMaxServerConnections() {
        return get("maxServerConnections", maxServerConnections);
    }

    public void setMaxServerConnections(int val) {
        set("maxServerConnections", val);
    }

    public int getMaxClientConnections() {
        return get("maxClientConnections", maxClientConnections);
    }

    public void setMaxClientConnections(int val) {
        set("maxClientConnections", val);
    }

    public int getLoadServerToTimeout() {
        return get("loadServerToTimeout", loadServerToTimeout);
    }

    public void setLoadServerToTimeout(int val) {
        set("loadServerToTimeout", val);
    }

    public boolean isUpdateLoadServer() {
        return get("updateLoadServer", updateLoadServer);
    }

    public void setUpdateLoadServer(boolean val) {
        set("updateLoadServer", val);
    }

    public int getNumAttemptsUpdateLoadServer() {
        return get("numAttemptsUpdateLoadServer", numAttemptsUpdateLoadServer);
    }

    public void setNumAttemptsUpdateLoadServer(int val) {
        set("numAttemptsUpdateLoadServer", val);
    }

    public int getDefaultRegistrationRenewalTime() {
        return get("defaultRegistrationRenewalTime", defaultRegistrationRenewalTime);
    }

    public void setDefaultRegistrationRenewalTime(int val) {
        set("RegistrationRenewalTime", val);
    }

    public int getMinRegistrationRenewalTime() {
        return get("minRegistrationRenewalTime", minRegistrationRenewalTime);
    }

    public void setMinRegistrationRenewalTime(int val) {
        set("minRegistrationRenewalTime", val);
    }

    public int getPreemptiveRegistrationRenewalTime() {
        return get("preemptiveRegistrationRenewalTime", preemptiveRegistrationRenewalTime);
    }

    public void setPreemptiveRegistrationRenewalTime(int val) {
        set("preemptiveRegistrationRenewalTime", val);
    }

    public int getRetryRegistrationRenewalTime() {
        return get("retryRegistrationRenewalTime", retryRegistrationRenewalTime);
    }

    public void setRetryRegistrationRenewalTime(int val) {
        set("retryRegistrationRenewalTime", val);
    }

    public void setBlockedServersList(Set val) {
        writeObject("blockedServersList", val);
    }

    public Set getBlockedServersList() {
        try {
            return (Set) readObject("blockedServersList");
        } catch (Exception e) {
            return Collections.synchronizedSet(new TreeSet(new InetAddressComparator()));
        }
    }
}
