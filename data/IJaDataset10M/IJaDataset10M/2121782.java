package net.m2technologies.open_arm.transport.transaction.snmp;

import ca.wengsoft.snmp.Core.*;
import net.m2technologies.open_arm.transport.transaction.AbstractTransactionMediatorImpl;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Copyright 2005 Mark Masterson<br> <br> Licensed under the Apache License, Version 2.0 (the "License");<br> you may
 * not use this file except in compliance with the License.<br> You may obtain a copy of the License at<br> <br>
 * http://www.apache.org/licenses/LICENSE-2.0<br> <br> Unless required by applicable law or agreed to in writing,
 * software<br> distributed under the License is distributed on an "AS IS" BASIS,<br> WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied.<br> See the License for the specific language governing permissions and<br>
 * limitations under the License.<br>
 * <p/>
 * <p>Description: This class outputs the information for a given transaction as SNMP v.2c traps.</p> <p>You should
 * provide a properties file for this class within its classpath/classloader scope.  The name of the properties file
 * must be the value of the PROPERTIES_FILE_NAME constant defined in this class.  Within this properties file, you
 * should define 7 properties; the managment host that traps should be sent to, the port that the managment host listens
 * for traps on, the enterprise OID that define your company's/application's traps, the application trap OID that
 * defines traps sent by your application, the time interval that you want as a threshold that must be exceeded before a
 * trap is sent, the same sort of threshold for updates, and a boolean flag that determines whether or not blocking
 * events should be forwarded as traps. <br><br> These properties must take the following form: "[standard
 * prefix].[attribute name]=[value]", where the [standard prefix] element must be <b>"net.m2technologies.open_arm.transport.transaction.snmp.SnmpMediator"
 * </b>, and the 7 attribute names are: <br><br>"managementHost", "managementHostTrapListenPort", "enterpriseOID",
 * "applicationTrapOIDValue", "transactionLengthInterval", "updateLengthInterval", "isBlockedEventTrap".<br><br> A
 * sample properties file might therefore look like the following:</p> <br><<code>
 * net.m2technologies.open_arm.transport.transaction.snmp.SnmpMediator.managementHost=127.0.0.1<br>
 * net.m2technologies.open_arm.transport.transaction.snmp.SnmpMediator.managementHostTrapListenPort=162<br>
 * net.m2technologies.open_arm.transport.transaction.snmp.SnmpMediator.enterpriseOID=1.3.6.1.2.1.2.0<br>
 * net.m2technologies.open_arm.transport.transaction.snmp.SnmpMediator.applicationTrapOIDValue=1.3.6.1.2.1.2.0.0.0.0<br>
 * net.m2technologies.open_arm.transport.transaction.snmp.SnmpMediator.transactionLengthInterval=5000<br>
 * net.m2technologies.open_arm.transport.transaction.snmp.SnmpMediator.updateLengthInterval=5000<br>
 * net.m2technologies.open_arm.transport.transaction.snmp.SnmpMediator.isBlockedEventTrap=false<br> </code> <br> <p>The
 * sample above also shows the default values that this class will use for each of these attributes, if you do not
 * provide a properties file, or if you omit one or more of the attributes.</p>
 *
 * @author Mark Masterson
 * @version 0.010
 */
public class SnmpMediator extends AbstractTransactionMediatorImpl {

    /**
     * Value = "open_arm_snmp_trap.properties"
     */
    public static final String PROPERTIES_FILE_NAME = "open_arm_snmp_trap.properties";

    private static final String SYSTEM_UPTIME_KEY = "1.3.6.1.2.1.1.3";

    private static final String TRAP_OID_KEY = "1.3.6.1.6.3.1.1.4.1";

    private static final String ENTERPRISE_OID_KEY = "1.3.6.1.6.3.1.1.4.3";

    private static final int CHUNK_LENGTH = 42;

    private String managementHost;

    private int managementHostTrapListenPort;

    private String enterpriseOID;

    private String applicationTrapOIDValue;

    private long transactionLengthInterval;

    private long updateLengthInterval;

    private boolean isBlockedEventTrap;

    private int chunkCounter;

    private boolean isInitialized;

    public SnmpMediator(final Object transactionDelegate) {
        super(transactionDelegate);
    }

    public void setConfiguration(final Object configuration) {
        this.isInitialized = false;
        initialize(configuration);
    }

    private void initialize(final Object configuration) {
        if (!this.isInitialized) {
            final Properties properties;
            if (null != configuration && configuration instanceof SnmpMediatorConfiguration) {
                properties = ((SnmpMediatorConfiguration) configuration).getProperties();
            } else {
                properties = new Properties();
                try {
                    properties.load(findPropertiesFile());
                } catch (IOException e) {
                    throw new RuntimeException(new StringBuffer().append("Could not find the properties file: ").append(PROPERTIES_FILE_NAME).toString(), e);
                }
            }
            this.managementHost = properties.getProperty(new StringBuffer().append(SnmpMediatorConfiguration.CLASS_NAME_PROPERTY_KEY_PREFIX).append(".managementHost").toString(), "127.0.0.1");
            this.managementHostTrapListenPort = Integer.parseInt(properties.getProperty(new StringBuffer().append(SnmpMediatorConfiguration.CLASS_NAME_PROPERTY_KEY_PREFIX).append(".managementHostTrapListenPort").toString(), "162"));
            this.enterpriseOID = properties.getProperty(new StringBuffer().append(SnmpMediatorConfiguration.CLASS_NAME_PROPERTY_KEY_PREFIX).append(".enterpriseOID").toString(), "1.3.6.1.2.1.2.0");
            this.applicationTrapOIDValue = properties.getProperty(new StringBuffer().append(SnmpMediatorConfiguration.CLASS_NAME_PROPERTY_KEY_PREFIX).append(".applicationTrapOIDValue").toString(), "1.3.6.1.2.1.2.0.0.0.0");
            this.transactionLengthInterval = Long.parseLong(properties.getProperty(new StringBuffer().append(SnmpMediatorConfiguration.CLASS_NAME_PROPERTY_KEY_PREFIX).append(".transactionLengthInterval").toString(), "5000"));
            this.updateLengthInterval = Long.parseLong(properties.getProperty(new StringBuffer().append(SnmpMediatorConfiguration.CLASS_NAME_PROPERTY_KEY_PREFIX).append(".updateLengthInterval").toString(), "5000"));
            this.isBlockedEventTrap = Boolean.valueOf(properties.getProperty(new StringBuffer().append(SnmpMediatorConfiguration.CLASS_NAME_PROPERTY_KEY_PREFIX).append(".isBlockedEventTrap").toString(), "false")).booleanValue();
            this.isInitialized = true;
        }
    }

    private InputStream findPropertiesFile() throws FileNotFoundException {
        InputStream result = getClass().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (null == result) {
            result = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        }
        if (null == result) {
            result = new FileInputStream("open_arm_snmp_trap.properties");
        }
        return result;
    }

    private SnmpGetSetPdu buildTrapPdu(final String uniqueTrapOidSuffix, final long upTime) {
        final SnmpGetSetPdu pdu = new SnmpGetSetPdu();
        pdu.setMsgType(AsnObject.SNMPV2_TRAP);
        pdu.addNameValuePair(new AsnNameValuePair(SYSTEM_UPTIME_KEY, new AsnInteger(upTime)));
        pdu.addNameValuePair(new AsnNameValuePair(TRAP_OID_KEY, new AsnOID(new StringBuffer().append(applicationTrapOIDValue).append(uniqueTrapOidSuffix).toString())));
        return pdu;
    }

    private void sendTrap(final String message, final String uniqueTrapOidSuffix, final long upTime) {
        final SnmpClient snmpClient = new SnmpClient();
        final SnmpMessage snmpMessage = new SnmpMessage();
        final SnmpGetSetPdu pdu = buildTrapPdu(uniqueTrapOidSuffix, upTime);
        this.chunkCounter = 0;
        addMessageToTrap(pdu, uniqueTrapOidSuffix, message);
        pdu.addNameValuePair(new AsnNameValuePair(ENTERPRISE_OID_KEY, new AsnOID(this.enterpriseOID)));
        snmpMessage.setPdu(pdu);
        snmpMessage.setSnmpVersion(AsnObject.SNMP_VERSION_2);
        snmpMessage.setCommunity("public");
        try {
            snmpClient.sendSnmpMessage(this.managementHost, this.managementHostTrapListenPort, snmpMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMessageToTrap(final SnmpGetSetPdu pdu, final String uniqueTrapOidSuffix, final String message) {
        if (null != message) {
            if (5 > this.chunkCounter) {
                if (CHUNK_LENGTH > message.length()) {
                    appendMessageTextToTrap(pdu, uniqueTrapOidSuffix, message);
                } else {
                    handleChunks(message, pdu, uniqueTrapOidSuffix);
                }
            } else {
                appendMessageTextToTrap(pdu, uniqueTrapOidSuffix, message);
                this.chunkCounter = 0;
            }
        }
    }

    private void handleChunks(final String message, final SnmpGetSetPdu pdu, final String uniqueTrapOidSuffix) {
        final String chunk = message.substring(0, CHUNK_LENGTH);
        final String remainder = message.substring(CHUNK_LENGTH);
        appendMessageTextToTrap(pdu, uniqueTrapOidSuffix, chunk);
        this.chunkCounter++;
        addMessageToTrap(pdu, uniqueTrapOidSuffix, remainder);
    }

    private void appendMessageTextToTrap(final SnmpGetSetPdu pdu, final String uniqueTrapOidSuffix, final String message) {
        pdu.addNameValuePair(new AsnNameValuePair(new StringBuffer().append(applicationTrapOIDValue).append(uniqueTrapOidSuffix).toString(), new AsnOctets(message)));
    }

    /**
     * Sends a trap with the standard 'blocked' message text.  Appends a ".1" to the application trap OID value defined
     * in the properties file (the default is "1.3.6.1.2.1.2.0.0.0.0").
     *
     * @param tmpHandle
     */
    protected void doBlocked(final long tmpHandle) {
        if (!this.isInitialized) initialize(null);
        if (this.isBlockedEventTrap) {
            sendTrap(getBlockedMessage(tmpHandle), ".1", System.currentTimeMillis());
        }
    }

    /**
     * Sends a trap with the standard 'stop' message text.  Appends a ".2" to the application trap OID value defined in
     * the properties file (the default is "1.3.6.1.2.1.2.0.0.0.0").
     *
     * @param elapsedTime
     * @param totalElapsedTime
     * @param status
     * @param diagnosticDetail
     */
    protected void doStop(final long elapsedTime, final long totalElapsedTime, final int status, final String diagnosticDetail) {
        if (!this.isInitialized) initialize(null);
        if (this.transactionLengthInterval < elapsedTime || this.transactionLengthInterval < totalElapsedTime) {
            sendTrap(getStopMessage(elapsedTime, totalElapsedTime, status, diagnosticDetail), ".2", elapsedTime);
        }
    }

    /**
     * Sends a trap with the standard 'unblocked' message text.  Appends a ".3" to the application trap OID value
     * defined in the properties file (the default is "1.3.6.1.2.1.2.0.0.0.0").
     *
     * @param blockHandle
     */
    protected void doUnblocked(final long blockHandle) {
        if (!this.isInitialized) initialize(null);
        if (this.isBlockedEventTrap) {
            sendTrap(getUnblockedMessage(blockHandle), ".3", System.currentTimeMillis());
        }
    }

    /**
     * Sends a trap with the standard 'update' message text.  Appends a ".4" to the application trap OID value defined
     * in the properties file (the default is "1.3.6.1.2.1.2.0.0.0.0").
     *
     * @param runningTime
     * @param totalRunningTime
     */
    protected void doUpdate(final long runningTime, final long totalRunningTime) {
        if (!this.isInitialized) initialize(null);
        if (this.updateLengthInterval < runningTime || this.updateLengthInterval < totalRunningTime) {
            sendTrap(getUpdateMessage(runningTime, totalRunningTime), ".4", runningTime);
        }
    }
}
