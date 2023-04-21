package org.opennms.netmgt.ackd.readers;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.hibernate.criterion.Restrictions;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.config.ackd.Parameter;
import org.opennms.netmgt.dao.AckdConfigurationDao;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.model.AckAction;
import org.opennms.netmgt.model.OnmsAcknowledgment;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.netmgt.model.acknowledgments.AckService;

/**
 * <p>HypericAckProcessor class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class HypericAckProcessor implements AckProcessor {

    /** Constant <code>READER_NAME_HYPERIC="HypericReader"</code> */
    public static final String READER_NAME_HYPERIC = "HypericReader";

    /** Constant <code>PARAMETER_PREFIX_HYPERIC_SOURCE="source:"</code> */
    public static final String PARAMETER_PREFIX_HYPERIC_SOURCE = "source:";

    /** Constant <code>ALERTS_PER_HTTP_TRANSACTION=200</code> */
    public static final int ALERTS_PER_HTTP_TRANSACTION = 200;

    private AckdConfigurationDao m_ackdDao;

    private AlarmDao m_alarmDao;

    private AckService m_ackService;

    /**
     * <p>This class is used as the data bean for parsing XML responses from the Hyperic HQ
     * systems that are serving up our alert status groovy servlet. The expected data 
     * format is:</p>
     * <pre>
     * <?xml version="1.0" encoding="UTF-8"?>
     *   <hyperic-alert-statuses>
     *   <alert id="1" ack="true" fixed="true"/>
     *   <alert id="2" ack="true" fixed="true"/>
     *   <alert id="3" ack="true" fixed="false"/>
     *   <alert id="4" ack="false" fixed="true"/>
     *   <alert id="5" ack="false" fixed="false"/>
     * </hyperic-alert-statuses>
     * </pre>
     */
    @XmlRootElement(name = "hyperic-alert-statuses")
    static class HypericAlertStatuses {

        private List<HypericAlertStatus> statusList;

        @XmlElement
        public List<HypericAlertStatus> getStatusList() {
            return statusList;
        }

        public void setStatusList(List<HypericAlertStatus> statusList) {
            this.statusList = statusList;
        }
    }

    /**
     * <p>This class represents each individual alarm status within the message. The expected
     * format is:</p>
     * <pre>
     * <alert id="1" ack="true" fixed="true"/>
     * </pre>
     * 
     * <p>TODO: Add ackUser, ackTime, fixedUser, fixedTime attributes to objects if possible</p>
     */
    @XmlRootElement(name = "alert")
    static class HypericAlertStatus {

        private int alertId;

        private String ackUser;

        private String ackMessage;

        private Date ackTime;

        private boolean isFixed;

        private String fixUser;

        private String fixMessage;

        private Date fixTime;

        @XmlAttribute(name = "id", required = true)
        public int getAlertId() {
            return alertId;
        }

        public void setAlertId(int alertId) {
            this.alertId = alertId;
        }

        @XmlAttribute(name = "fixed", required = true)
        public boolean isFixed() {
            return isFixed;
        }

        public void setFixed(boolean isFixed) {
            this.isFixed = isFixed;
        }

        @XmlAttribute(name = "ackUser")
        public String getAckUser() {
            return ackUser;
        }

        public void setAckUser(String ackUser) {
            this.ackUser = ackUser;
        }

        @XmlAttribute(name = "ackMessage")
        public String getAckMessage() {
            return ackMessage;
        }

        public void setAckMessage(String ackMessage) {
            this.ackMessage = ackMessage;
        }

        @XmlAttribute(name = "ackTime")
        public Date getAckTime() {
            return ackTime;
        }

        public void setAckTime(Date ackTime) {
            this.ackTime = ackTime;
        }

        @XmlAttribute(name = "fixUser")
        public String getFixUser() {
            return fixUser;
        }

        public void setFixUser(String fixUser) {
            this.fixUser = fixUser;
        }

        @XmlAttribute(name = "fixMessage")
        public String getFixMessage() {
            return fixMessage;
        }

        public void setFixMessage(String fixMessage) {
            this.fixMessage = fixMessage;
        }

        @XmlAttribute(name = "fixTime")
        public Date getFixTime() {
            return fixTime;
        }

        public void setFixTime(Date fixTime) {
            this.fixTime = fixTime;
        }

        public String toString() {
            StringBuffer retval = new StringBuffer();
            retval.append("{ ");
            retval.append("id: ").append(String.valueOf(alertId)).append(", ");
            retval.append("fixed: ").append(String.valueOf(isFixed)).append(", ");
            retval.append("ackUser: ").append(String.valueOf(ackUser)).append(", ");
            retval.append("ackMessage: ").append(String.valueOf(ackMessage)).append(", ");
            retval.append("ackTime: ").append(String.valueOf(ackTime)).append(", ");
            retval.append("fixUser: ").append(String.valueOf(fixUser)).append(", ");
            retval.append("fixMessage: ").append(String.valueOf(fixMessage)).append(", ");
            retval.append("fixTime: ").append(String.valueOf(fixTime));
            retval.append(" }");
            return retval.toString();
        }
    }

    private static ThreadCategory log() {
        return ThreadCategory.getInstance(HypericAckProcessor.class);
    }

    /**
     * <p>reloadConfigs</p>
     */
    public void reloadConfigs() {
        log().debug("reloadConfigs: reloading configuration...");
        m_ackdDao.reloadConfiguration();
        log().debug("reloadConfigs: configuration reloaded");
    }

    /**
     * <p>fetchUnclearedHypericAlarms</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<OnmsAlarm> fetchUnclearedHypericAlarms() {
        OnmsCriteria criteria = new OnmsCriteria(OnmsAlarm.class, "alarm");
        criteria.add(Restrictions.eq("uei", "uei.opennms.org/external/hyperic/alert"));
        criteria.add(Restrictions.gt("severity", OnmsSeverity.NORMAL));
        return m_alarmDao.findMatching(criteria);
    }

    /**
     * <p>getUrlForHypericSource</p>
     *
     * @param source a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String getUrlForHypericSource(String source) {
        if (source == null) {
            throw new IllegalArgumentException("Cannot search for null Hyperic platform IDs inside the ackd configuration");
        } else if ("".equals(source)) {
            throw new IllegalArgumentException("Cannot search for blank Hyperic platform IDs inside the ackd configuration");
        }
        List<Parameter> params = m_ackdDao.getParametersForReader(READER_NAME_HYPERIC);
        if (params == null) {
            throw new IllegalStateException("There is no configuration for the '" + READER_NAME_HYPERIC + "' reader inside the ackd configuration");
        }
        for (Parameter param : params) {
            if ((PARAMETER_PREFIX_HYPERIC_SOURCE + source).equalsIgnoreCase(param.getKey())) {
                return param.getValue();
            }
        }
        return null;
    }

    /**
     * <p>run</p>
     */
    public void run() {
        List<OnmsAcknowledgment> acks = new ArrayList<OnmsAcknowledgment>();
        try {
            log().info("run: Processing Hyperic acknowledgments...");
            List<OnmsAlarm> unAckdAlarms = fetchUnclearedHypericAlarms();
            Map<String, List<OnmsAlarm>> organizedAlarms = new TreeMap<String, List<OnmsAlarm>>();
            int legacyAlarmCount = 0;
            for (OnmsAlarm alarm : unAckdAlarms) {
                String key = getAlertSourceParmValue(alarm);
                if (key == null || "".equals(key)) {
                    legacyAlarmCount++;
                } else {
                    List<OnmsAlarm> targetList = organizedAlarms.get(key);
                    if (targetList == null) {
                        targetList = new ArrayList<OnmsAlarm>();
                        organizedAlarms.put(key, targetList);
                    }
                    targetList.add(alarm);
                }
            }
            if (legacyAlarmCount > 0) {
                log().info(String.valueOf(legacyAlarmCount) + " Hyperic alarms without an alert.source param found, these alarms will not be processed");
            }
            for (Map.Entry<String, List<OnmsAlarm>> alarmList : organizedAlarms.entrySet()) {
                String hypericSystem = alarmList.getKey();
                List<OnmsAlarm> alarmsForSystem = alarmList.getValue();
                String hypericUrl = getUrlForHypericSource(hypericSystem);
                if (hypericUrl == null) {
                    log().warn("Could not find Hyperic host URL for the following platform ID: " + hypericSystem);
                    log().warn("Skipping processing of " + alarmsForSystem.size() + " alarms with that platform ID");
                    continue;
                }
                try {
                    List<String> alertIdList = new ArrayList<String>();
                    for (OnmsAlarm alarmForSystem : alarmList.getValue()) {
                        String alertId = getAlertIdParmValue(alarmForSystem);
                        alertIdList.add(alertId);
                    }
                    List<HypericAlertStatus> alertsForSystem = fetchHypericAlerts(hypericUrl, alertIdList);
                    for (HypericAlertStatus alert : alertsForSystem) {
                        OnmsAlarm alarm = findAlarmForHypericAlert(alarmsForSystem, hypericSystem, alert);
                        if (alarm == null) {
                            log().warn("Could not find the OpenNMS alarm for the following Hyperic alert: URL: \"" + hypericUrl + "\", id: " + alert.getAlertId());
                        } else if (alert.isFixed() && !OnmsSeverity.CLEARED.equals(alarm.getSeverity())) {
                            OnmsAcknowledgment ack = new OnmsAcknowledgment(alarm, "Ackd.HypericAckProcessor", (alert.getFixTime() != null) ? alert.getFixTime() : new Date());
                            ack.setAckAction(AckAction.CLEAR);
                            ack.setLog(alert.getFixMessage());
                            acks.add(ack);
                        } else if (alert.getAckMessage() != null && alarm.getAckTime() == null) {
                            OnmsAcknowledgment ack = new OnmsAcknowledgment(alarm, "Ackd.HypericAckProcessor", (alert.getAckTime() != null) ? alert.getAckTime() : new Date());
                            ack.setAckAction(AckAction.ACKNOWLEDGE);
                            ack.setLog(alert.getAckMessage());
                            acks.add(ack);
                        }
                    }
                } catch (Throwable e) {
                    log().warn("run: threw exception when processing alarms for Hyperic system " + hypericSystem + ": " + e.getMessage());
                    log().warn("run: " + acks.size() + " acknowledgements processed successfully before exception");
                } finally {
                    if (acks.size() > 0) {
                        m_ackService.processAcks(acks);
                    }
                }
            }
            log().info("run: Finished processing Hyperic acknowledgments (" + acks.size() + " ack(s) processed for " + unAckdAlarms.size() + " alarm(s))");
        } catch (Throwable e) {
            log().warn("run: threw exception: " + e.getMessage(), e);
        }
    }

    /**
     * <p>findAlarmForHypericAlert</p>
     *
     * @param alarms a {@link java.util.List} object.
     * @param platformId a {@link java.lang.String} object.
     * @param alert a {@link org.opennms.netmgt.ackd.readers.HypericAckProcessor.HypericAlertStatus} object.
     * @return a {@link org.opennms.netmgt.model.OnmsAlarm} object.
     */
    public static OnmsAlarm findAlarmForHypericAlert(List<OnmsAlarm> alarms, String platformId, HypericAlertStatus alert) {
        String targetPlatformId = "alert.source=" + platformId + "(string,text)";
        String targetAlertId = "alert.id=" + String.valueOf(alert.getAlertId()) + "(string,text)";
        for (OnmsAlarm alarm : alarms) {
            String parmString = alarm.getEventParms();
            String[] parms = parmString.split(";");
            for (String parm : parms) {
                if (targetPlatformId.equals(parm)) {
                    for (String alertparm : parms) {
                        if (targetAlertId.equals(alertparm)) {
                            return alarm;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * <p>getAlertSourceParmValue</p>
     *
     * @param alarm a {@link org.opennms.netmgt.model.OnmsAlarm} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getAlertSourceParmValue(OnmsAlarm alarm) {
        return getParmValueByRegex(alarm, "alert.source=(.*)[(]string,text[)]");
    }

    /**
     * <p>getAlertIdParmValue</p>
     *
     * @param alarm a {@link org.opennms.netmgt.model.OnmsAlarm} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getAlertIdParmValue(OnmsAlarm alarm) {
        return getParmValueByRegex(alarm, "alert.id=([0-9]*)[(]string,text[)]");
    }

    /**
     * <p>Some parameter values that you might be interested in inside this class:</p>
     *
     * <ul>
     * <li><code>alert.id</code>: ID of the alert in the remote Hyperic HQ system</li>
     * <li><code>alert.baseURL</code>: Base URL of the Hyperic HQ service that generated the alert</li>
     * <li><code>alert.source</code>: String key that identifies the Hyperic HQ service that generated the alert</li>
     * </ul>
     *
     * @param alarm The alarm to fetch parameters from
     * @param regex Java regex expression with a () group that will be returned
     * @return The matching group from the regex
     */
    public static String getParmValueByRegex(OnmsAlarm alarm, String regex) {
        Pattern pattern = Pattern.compile(regex);
        String parmString = alarm.getEventParms();
        String[] parms = parmString.split(";");
        for (String parm : parms) {
            Matcher matcher = pattern.matcher(parm);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    /**
     * <p>fetchHypericAlerts</p>
     *
     * @param hypericUrl a {@link java.lang.String} object.
     * @param alertIds a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws org.apache.commons.httpclient.HttpException if any.
     * @throws java.io.IOException if any.
     * @throws javax.xml.bind.JAXBException if any.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public static List<HypericAlertStatus> fetchHypericAlerts(String hypericUrl, List<String> alertIds) throws IOException, JAXBException, XMLStreamException {
        List<HypericAlertStatus> retval = new ArrayList<HypericAlertStatus>();
        if (alertIds.size() < 1) {
            return retval;
        }
        for (int i = 0; i < alertIds.size(); i++) {
            StringBuffer alertIdString = new StringBuffer();
            alertIdString.append("?");
            for (int j = 0; (j < ALERTS_PER_HTTP_TRANSACTION) && (i < alertIds.size()); j++, i++) {
                if (j > 0) alertIdString.append("&");
                alertIdString.append("id=").append(alertIds.get(i));
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
            httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
            HttpUriRequest httpMethod = new HttpGet(hypericUrl + alertIdString.toString());
            httpMethod.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "OpenNMS-Ackd.HypericAckProcessor");
            String userinfo = null;
            try {
                URI hypericUri = new URI(hypericUrl);
                userinfo = hypericUri.getUserInfo();
            } catch (URISyntaxException e) {
                log().warn("Could not parse URI to get username/password stanza: " + hypericUrl, e);
            }
            if (userinfo != null && !"".equals(userinfo)) {
                httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userinfo));
                HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {

                    public void process(final HttpRequest request, final HttpContext context) throws IOException {
                        AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
                        CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                        if (authState.getAuthScheme() == null) {
                            AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
                            Credentials creds = credsProvider.getCredentials(authScope);
                            if (creds != null) {
                                authState.setAuthScheme(new BasicScheme());
                                authState.setCredentials(creds);
                            }
                        }
                    }
                };
                httpClient.addRequestInterceptor(preemptiveAuth, 0);
            }
            try {
                HttpResponse response = httpClient.execute(httpMethod);
                retval = parseHypericAlerts(new StringReader(EntityUtils.toString(response.getEntity())));
            } finally {
            }
        }
        return retval;
    }

    /**
     * <p>parseHypericAlerts</p>
     *
     * @param reader a {@link java.io.Reader} object.
     * @return a {@link java.util.List} object.
     * @throws javax.xml.bind.JAXBException if any.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public static List<HypericAlertStatus> parseHypericAlerts(Reader reader) throws JAXBException, XMLStreamException {
        List<HypericAlertStatus> retval = new ArrayList<HypericAlertStatus>();
        JAXBContext context = JAXBContext.newInstance(new Class[] { HypericAlertStatuses.class, HypericAlertStatus.class });
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLEventReader xmler = xmlif.createXMLEventReader(reader);
        EventFilter filter = new EventFilter() {

            public boolean accept(XMLEvent event) {
                return event.isStartElement();
            }
        };
        XMLEventReader xmlfer = xmlif.createFilteredReader(xmler, filter);
        StartElement startElement = (StartElement) xmlfer.nextEvent();
        String rootElementName = context.createJAXBIntrospector().getElementName(new HypericAlertStatuses()).getLocalPart();
        if (rootElementName.equals(startElement.getName().getLocalPart())) {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            while (xmlfer.peek() != null) {
                Object object = unmarshaller.unmarshal(xmler);
                if (object instanceof HypericAlertStatus) {
                    HypericAlertStatus alertStatus = (HypericAlertStatus) object;
                    retval.add(alertStatus);
                }
            }
        } else {
            StringBuffer errorContent = new StringBuffer();
            LineNumberReader lineReader = new LineNumberReader(reader);
            try {
                String line;
                while (true) {
                    line = lineReader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        errorContent.append(line.trim());
                    }
                }
            } catch (IOException e) {
                errorContent.append("Exception while trying to print out message content: " + e.getMessage());
            }
            throw new JAXBException("Found wrong root element in Hyperic XML document, expected: \"" + rootElementName + "\", found \"" + startElement.getName().getLocalPart() + "\"\n" + errorContent.toString());
        }
        return retval;
    }

    /**
     * <p>setAckdConfigDao</p>
     *
     * @param configDao a {@link org.opennms.netmgt.dao.AckdConfigurationDao} object.
     */
    public synchronized void setAckdConfigDao(final AckdConfigurationDao configDao) {
        m_ackdDao = configDao;
    }

    /**
     * <p>setAckService</p>
     *
     * @param ackService a {@link org.opennms.netmgt.model.acknowledgments.AckService} object.
     */
    public synchronized void setAckService(final AckService ackService) {
        m_ackService = ackService;
    }

    /**
     * <p>afterPropertiesSet</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void afterPropertiesSet() throws Exception {
    }

    /**
     * <p>setAlarmDao</p>
     *
     * @param dao a {@link org.opennms.netmgt.dao.AlarmDao} object.
     */
    public synchronized void setAlarmDao(final AlarmDao dao) {
        m_alarmDao = dao;
    }
}
