package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.DevicesConfiguration;
import com.volantis.mcs.runtime.configuration.FileRepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.JDBCRepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.UnknownDevicesLoggingConfiguration;
import com.volantis.mcs.runtime.configuration.EmailNotifierConfiguration;

/**
 * Adds digester rules for the devices element and it's sub elements.
 */
public class DevicesRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Construct an instance of this class, using the prefix provided.
     *
     * @param prefix the prefix to add the rules to the digester under.
     */
    public DevicesRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        String pattern = prefix + "/devices";
        digester.addObjectCreate(pattern, DevicesConfiguration.class);
        digester.addSetNext(pattern, "setDevices");
        digester.addSetProperties(pattern, new String[] { "default" }, new String[] { "defaultDeviceName" });
        String standardPattern = pattern + "/standard";
        addFileRepositoryRules(digester, standardPattern, "setStandardDeviceRepository");
        final String loggingPattern = pattern + "/logging";
        addLoggingRules(digester, loggingPattern);
    }

    /**
      * Add digester rules for the child elements of an individual project tag.
      *
      * @param digester the digester to add rules to.
      * @param prefix prefix to add the rules under.
      */
    private void addFileRepositoryRules(Digester digester, String prefix, String setNextMethod) {
        String pattern = prefix + "/file-repository";
        digester.addObjectCreate(pattern, FileRepositoryDeviceConfiguration.class);
        digester.addSetNext(pattern, setNextMethod);
        digester.addSetProperties(pattern, new String[] { "location" }, new String[] { "location" });
        pattern = prefix + "/jdbc-repository";
        digester.addObjectCreate(pattern, JDBCRepositoryDeviceConfiguration.class);
        digester.addSetNext(pattern, setNextMethod);
        digester.addSetProperties(pattern, new String[] { "project" }, new String[] { "project" });
    }

    /**
     * Adds digester rules for unknown/abstract devices logging.
     *
     * @param digester the digester to add rules to
     * @param prefix the prefix to add the rules under
     */
    private void addLoggingRules(final Digester digester, final String prefix) {
        digester.addObjectCreate(prefix, UnknownDevicesLoggingConfiguration.class);
        digester.addSetNext(prefix, "setUnknownDevicesLogging");
        digester.addCallMethod(prefix + "/log-file", "setFileName", 0, new Class[] { String.class });
        final String emailPattern = prefix + "/e-mail";
        digester.addObjectCreate(emailPattern, EmailNotifierConfiguration.class);
        digester.addSetNext(emailPattern, "setEmailNotifier");
        digester.addCallMethod(emailPattern + "/e-mail-sending", "setEmailSending", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/smtp/host", "setSmtpHost", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/smtp/port", "setSmtpPort", 0, new Class[] { Integer.class });
        digester.addCallMethod(emailPattern + "/config/smtp/user-name", "setSmtpUserName", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/smtp/password", "setSmtpPassword", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/from/address", "setFromAddress", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/from/name", "setFromName", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/to/address", "setToAddress", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/to/name", "setToName", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/subject", "setSubject", 0, new Class[] { String.class });
        digester.addCallMethod(emailPattern + "/config/period", "setPeriod", 0, new Class[] { String.class });
    }
}
