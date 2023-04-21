package com.amazon.merchants.transport.preferences;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.amazon.merchants.transport.logging.AuditLogger;

/**
 * Copyright 2004 Amazon.com
 *
 * Description:
 *
 * @author hynoskij
 *
 */
public class TransportPreferences {

    private static final TransportPreferences _instance = new TransportPreferences();

    private Preferences preferences;

    private static final Log log = LogFactory.getLog(TransportPreferences.class);

    public static final TransportPreferences instance() {
        return _instance;
    }

    private TransportPreferences() {
        preferences = Preferences.systemNodeForPackage(TransportPreferences.class);
    }

    public void putPreference(TransportPreferenceEnum key, String value) {
        preferences.put(key.getName(), value);
    }

    public void putPreferenceInt(TransportPreferenceEnum key, int value) {
        preferences.putInt(key.getName(), value);
    }

    public void putPreferenceLong(TransportPreferenceEnum key, long value) {
        preferences.putLong(key.getName(), value);
    }

    public void putPreferenceDouble(TransportPreferenceEnum key, double value) {
        preferences.putDouble(key.getName(), value);
    }

    public void putPreferenceBoolean(TransportPreferenceEnum key, boolean value) {
        preferences.putBoolean(key.getName(), value);
    }

    public String getPreference(TransportPreferenceEnum key) {
        return preferences.get(key.getName(), key.getDefaultValue());
    }

    public int getPreferenceInt(TransportPreferenceEnum key) {
        return preferences.getInt(key.getName(), Integer.parseInt(key.getDefaultValue()));
    }

    public long getPreferenceLong(TransportPreferenceEnum key) {
        return preferences.getLong(key.getName(), Long.parseLong(key.getDefaultValue()));
    }

    public double getPreferenceDouble(TransportPreferenceEnum key) {
        return preferences.getDouble(key.getName(), Double.parseDouble(key.getDefaultValue()));
    }

    public boolean getPreferenceBoolean(TransportPreferenceEnum key) {
        return preferences.getBoolean(key.getName(), Boolean.getBoolean(key.getDefaultValue()));
    }

    public void store(TransportConfiguration config) {
        preferences.put(TransportPreferenceEnum.TRANSPORT_ROOT_FOLDER.getName(), config.getRootFolder());
        preferences.putInt(TransportPreferenceEnum.PROCESSING_REPORT_INTERVAL_MINUTES.getName(), config.getProcessingReportIntervalMinutes());
        preferences.putInt(TransportPreferenceEnum.SETTLEMENT_REPORT_INTERVAL_MINUTES.getName(), config.getSettlementReportIntervalMinutes());
        preferences.putInt(TransportPreferenceEnum.ORDER_REPORT_INTERVAL_MINUTES.getName(), config.getOrderReportIntervalMinutes());
        preferences.putInt(TransportPreferenceEnum.DISPATCH_INTERVAL_MINUTES.getName(), config.getDispatchIntervalMinutes());
        preferences.putInt(TransportPreferenceEnum.RETRIEVER_REPORT_TIMEOUT.getName(), config.getProcReportTimeoutMinutes());
        preferences.putInt(TransportPreferenceEnum.RETRIEVER_POST_TIMEOUT_POLL_INTERVAL.getName(), config.getProcReportPostTimeoutIntervalMinutes());
        preferences.putBoolean(TransportPreferenceEnum.IS_FLATFILE.getName(), config.isFlatFile());
        preferences.put(TransportPreferenceEnum.SERVER_LOCATIONS.getName(), config.getServerLocations());
        preferences.putInt(TransportPreferenceEnum.MONITOR_SEND_PORT.getName(), config.getMonitorSendPort());
        preferences.putInt(TransportPreferenceEnum.MONITOR_RECEIVE_PORT.getName(), config.getMonitorReceivePort());
        preferences.put(TransportPreferenceEnum.MONITOR_RECEIVE_HOST.getName(), config.getMonitorReceiveHost());
        preferences.put(TransportPreferenceEnum.MONITOR_SMTP_SERVER.getName(), config.getMonitorSMTPServer());
        preferences.put(TransportPreferenceEnum.MONITOR_NOTIFY_EMAIL.getName(), config.getMonitorNotifyEmail());
        UserAccountPreferences.instance().storeAll(config.getAccounts());
    }

    public TransportConfiguration getConfig() {
        TransportConfiguration config = new TransportConfiguration();
        config.setRootFolder(preferences.get(TransportPreferenceEnum.TRANSPORT_ROOT_FOLDER.getName(), TransportPreferenceEnum.TRANSPORT_ROOT_FOLDER.getDefaultValue()));
        config.setProcessingReportIntervalMinutes(preferences.getInt(TransportPreferenceEnum.PROCESSING_REPORT_INTERVAL_MINUTES.getName(), Integer.parseInt(TransportPreferenceEnum.PROCESSING_REPORT_INTERVAL_MINUTES.getDefaultValue())));
        config.setSettlementReportIntervalMinutes(preferences.getInt(TransportPreferenceEnum.SETTLEMENT_REPORT_INTERVAL_MINUTES.getName(), Integer.parseInt(TransportPreferenceEnum.SETTLEMENT_REPORT_INTERVAL_MINUTES.getDefaultValue())));
        config.setOrderReportIntervalMinutes(preferences.getInt(TransportPreferenceEnum.ORDER_REPORT_INTERVAL_MINUTES.getName(), Integer.parseInt(TransportPreferenceEnum.ORDER_REPORT_INTERVAL_MINUTES.getDefaultValue())));
        config.setDispatchIntervalMinutes(preferences.getInt(TransportPreferenceEnum.DISPATCH_INTERVAL_MINUTES.getName(), Integer.parseInt(TransportPreferenceEnum.DISPATCH_INTERVAL_MINUTES.getDefaultValue())));
        config.setProcReportTimeoutMinutes(preferences.getInt(TransportPreferenceEnum.RETRIEVER_REPORT_TIMEOUT.getName(), Integer.parseInt(TransportPreferenceEnum.RETRIEVER_REPORT_TIMEOUT.getDefaultValue())));
        config.setProcReportPostTimeoutIntervalMinutes(preferences.getInt(TransportPreferenceEnum.RETRIEVER_POST_TIMEOUT_POLL_INTERVAL.getName(), Integer.parseInt(TransportPreferenceEnum.RETRIEVER_POST_TIMEOUT_POLL_INTERVAL.getDefaultValue())));
        config.setFlatFile(preferences.getBoolean(TransportPreferenceEnum.IS_FLATFILE.getName(), Boolean.getBoolean(TransportPreferenceEnum.IS_FLATFILE.getDefaultValue())));
        config.setServerLocations(preferences.get(TransportPreferenceEnum.SERVER_LOCATIONS.getName(), TransportPreferenceEnum.SERVER_LOCATIONS.getDefaultValue()));
        config.setMonitorSendPort(preferences.getInt(TransportPreferenceEnum.MONITOR_SEND_PORT.getName(), Integer.parseInt(TransportPreferenceEnum.MONITOR_SEND_PORT.getDefaultValue())));
        config.setMonitorReceivePort(preferences.getInt(TransportPreferenceEnum.MONITOR_RECEIVE_PORT.getName(), Integer.parseInt(TransportPreferenceEnum.MONITOR_RECEIVE_PORT.getDefaultValue())));
        config.setMonitorReceiveHost(preferences.get(TransportPreferenceEnum.MONITOR_RECEIVE_HOST.getName(), TransportPreferenceEnum.MONITOR_RECEIVE_HOST.getDefaultValue()));
        config.setMonitorSMTPServer(preferences.get(TransportPreferenceEnum.MONITOR_SMTP_SERVER.getName(), TransportPreferenceEnum.MONITOR_SMTP_SERVER.getDefaultValue()));
        config.setMonitorNotifyEmail(preferences.get(TransportPreferenceEnum.MONITOR_NOTIFY_EMAIL.getName(), TransportPreferenceEnum.MONITOR_NOTIFY_EMAIL.getDefaultValue()));
        return config;
    }

    public void reload() {
        try {
            preferences.sync();
        } catch (BackingStoreException bex) {
            log.debug(ExceptionUtils.getFullStackTrace(bex));
            AuditLogger.instance().logSeriousError("Could not read preferences.");
        }
    }

    public boolean preferencesExist() {
        if (preferences == null || preferences.get(TransportPreferenceEnum.TRANSPORT_ROOT_FOLDER.getName(), null) == null) {
            return false;
        }
        return true;
    }

    public void removeAll() {
        try {
            String[] keys = preferences.keys();
            for (int i = 0; i < keys.length; i++) {
                preferences.remove(keys[i]);
            }
        } catch (BackingStoreException bex) {
        }
    }
}
