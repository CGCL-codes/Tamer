package org.opennms.netmgt;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class holds all OpenNMS events related constants - the UEI's, parm
 * names, the event time format etc.
 * 
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * 
 */
public class EventConstants {

    /**
     * The date format string to parse a Date.toString() type string to a
     * database timestamp using the postgres to_timestamp() built-in function.
     */
    public static final String POSTGRES_DATE_FORMAT = "\'Dy Mon DD HH24:MI:SS Tz YYYY\'";

    /**
     * The string property set on JMS messages to indicate the encoding to be
     * used
     */
    public static final String JMS_MSG_PROP_CHAR_ENCODING = "char_encoding";

    /**
     * The value for the string property set on JMS messages to indicate the
     * encoding to be used
     */
    public static final String JMS_MSG_PROP_CHAR_ENCODING_VALUE = "US-ASCII";

    /**
     * The string property set on JMS messages to indicate the sender service
     */
    public static final String JMS_MSG_PROP_SENDER = "sender";

    /**
     * The string property set on JMS messages broadcast from eventd - to use
     * UEI(s) as a filter
     */
    public static final String JMS_MSG_PROP_UEI_SELECTOR = "ueiSelector";

    /**
     * The status query control event
     */
    public static final String STATUS_QUERY_CONTROL_EVENT_UEI = "uei.opennms.org/internal/control/status";

    /**
     * The start event
     */
    public static final String START_CONTROL_EVENT_UEI = "uei.opennms.org/internal/control/start";

    /**
     * The pause event
     */
    public static final String PAUSE_CONTROL_EVENT_UEI = "uei.opennms.org/internal/control/pause";

    /**
     * The resume event
     */
    public static final String RESUME_CONTROL_EVENT_UEI = "uei.opennms.org/internal/control/resume";

    /**
     * The stop event
     */
    public static final String STOP_CONTROL_EVENT_UEI = "uei.opennms.org/internal/control/stop";

    /**
     * The 'start pending' response event
     */
    public static final String CONTROL_START_PENDING_EVENT_UEI = "uei.opennms.org/internal/control/startPending";

    /**
     * The 'starting' response event
     */
    public static final String CONTROL_STARTING_EVENT_UEI = "uei.opennms.org/internal/control/starting";

    /**
     * The 'pause pending' response event
     */
    public static final String CONTROL_PAUSE_PENDING_EVENT_UEI = "uei.opennms.org/internal/control/pausePending";

    /**
     * The 'paused' response event
     */
    public static final String CONTROL_PAUSED_EVENT_UEI = "uei.opennms.org/internal/control/paused";

    /**
     * The 'resume pending' response event
     */
    public static final String CONTROL_RESUME_PENDING_EVENT_UEI = "uei.opennms.org/internal/control/resumePending";

    /**
     * The 'running' response event
     */
    public static final String CONTROL_RUNNING_EVENT_UEI = "uei.opennms.org/internal/control/running";

    /**
     * The 'stop pending' response event
     */
    public static final String CONTROL_STOP_PENDING_EVENT_UEI = "uei.opennms.org/internal/control/stopPending";

    /**
     * The 'stopped' response event
     */
    public static final String CONTROL_STOPPED_EVENT_UEI = "uei.opennms.org/internal/control/stopped";

    /**
     * The control error reponse event
     */
    public static final String CONTROL_ERROR_EVENT_UEI = "uei.opennms.org/internal/control/error";

    /**
     * The new suspect event UEI
     */
    public static final String NEW_SUSPECT_INTERFACE_EVENT_UEI = "uei.opennms.org/internal/discovery/newSuspect";

    /**
     * The discovery pause event UEI
     */
    public static final String DISC_PAUSE_EVENT_UEI = "uei.opennms.org/internal/capsd/discPause";

    /**
     * The discovery resume event UEI
     */
    public static final String DISC_RESUME_EVENT_UEI = "uei.opennms.org/internal/capsd/discResume";

    /**
     * The discovery configuration changed event UEI
     */
    public static final String DISCOVERYCONFIG_CHANGED_EVENT_UEI = "uei.opennms.org/internal/discoveryConfigChange";

    /**
     * The update server event UEI
     */
    public static final String UPDATE_SERVER_EVENT_UEI = "uei.opennms.org/internal/capsd/updateServer";

    /**
     * The update service event UEI
     */
    public static final String UPDATE_SERVICE_EVENT_UEI = "uei.opennms.org/internal/capsd/updateService";

    /**
     * The add node event UEI
     */
    public static final String ADD_NODE_EVENT_UEI = "uei.opennms.org/internal/capsd/addNode";

    /**
     * The delete node event UEI
     */
    public static final String DELETE_NODE_EVENT_UEI = "uei.opennms.org/internal/capsd/deleteNode";

    /**
     * The add interface event UEI
     */
    public static final String ADD_INTERFACE_EVENT_UEI = "uei.opennms.org/internal/capsd/addInterface";

    /**
     * The delete interface event UEI
     */
    public static final String DELETE_INTERFACE_EVENT_UEI = "uei.opennms.org/internal/capsd/deleteInterface";

    /**
     * The change service event UEI
     */
    public static final String CHANGE_SERVICE_EVENT_UEI = "uei.opennms.org/internal/capsd/changeService";

    /**
     * The restart polling node event UEI
     */
    public static final String RESTART_POLLING_INTERFACE_EVENT_UEI = "uei.opennms.org/nodes/restartPollingInterface";

    /**
     * The change service event UEI
     */
    public static final String XMLRPC_NOTIFICATION_EVENT_UEI = "uei.opennms.org/internal/capsd/xmlrpcNotification";

    /**
     * The node added event UEI
     */
    public static final String NODE_ADDED_EVENT_UEI = "uei.opennms.org/nodes/nodeAdded";

    /**
     * The node updated event UEI (added for the ProvisioningAdapter integration)
     */
    public static final String NODE_UPDATED_EVENT_UEI = "uei.opennms.org/nodes/nodeUpdated";

    /**
	 * The node category membership changed UEI
	 */
    public static final String NODE_CATEGORY_MEMBERSHIP_CHANGED_EVENT_UEI = "uei.opennms.org/nodes/nodeCategoryMembershipChanged";

    /**
     * The node gained interface event UEI
     */
    public static final String NODE_GAINED_INTERFACE_EVENT_UEI = "uei.opennms.org/nodes/nodeGainedInterface";

    /**
     * The node gained service event UEI
     */
    public static final String NODE_GAINED_SERVICE_EVENT_UEI = "uei.opennms.org/nodes/nodeGainedService";

    /**
     * The node lost service event UEI
     */
    public static final String NODE_LOST_SERVICE_EVENT_UEI = "uei.opennms.org/nodes/nodeLostService";

    /**
     * The service responsive event UEI
     */
    public static final String SERVICE_RESPONSIVE_EVENT_UEI = "uei.opennms.org/nodes/serviceResponsive";

    /**
     * The service unresponsive event UEI
     */
    public static final String SERVICE_UNRESPONSIVE_EVENT_UEI = "uei.opennms.org/nodes/serviceUnresponsive";

    /**
     * The service unmanaged event UEI
     */
    public static final String SERVICE_UNMANAGED_EVENT_UEI = "uei.opennms.org/nodes/serviceUnmanaged";

    /**
     * The interface down event UEI
     */
    public static final String INTERFACE_DOWN_EVENT_UEI = "uei.opennms.org/nodes/interfaceDown";

    /**
     * The snmp interface oper status down event UEI
     */
    public static final String SNMP_INTERFACE_OPER_DOWN_EVENT_UEI = "uei.opennms.org/nodes/snmp/interfaceOperDown";

    /**
     * The snmp interface admin down event UEI
     */
    public static final String SNMP_INTERFACE_ADMIN_DOWN_EVENT_UEI = "uei.opennms.org/nodes/snmp/interfaceAdminDown";

    /**
     * The node down event UEI
     */
    public static final String NODE_DOWN_EVENT_UEI = "uei.opennms.org/nodes/nodeDown";

    /**
     * The path outage event UEI
     */
    public static final String PATH_OUTAGE_EVENT_UEI = "uei.opennms.org/nodes/pathOutage";

    /**
     * The node up event UEI
     */
    public static final String NODE_UP_EVENT_UEI = "uei.opennms.org/nodes/nodeUp";

    /**
     * The interface up event UEI
     */
    public static final String INTERFACE_UP_EVENT_UEI = "uei.opennms.org/nodes/interfaceUp";

    /**
     * The snmp interface oper status up event UEI
     */
    public static final String SNMP_INTERFACE_OPER_UP_EVENT_UEI = "uei.opennms.org/nodes/snmp/interfaceOperUp";

    /**
     * The snmp interface admin up event UEI
     */
    public static final String SNMP_INTERFACE_ADMIN_UP_EVENT_UEI = "uei.opennms.org/nodes/snmp/interfaceAdminUp";

    /**
     * The node regained service event UEI
     */
    public static final String NODE_REGAINED_SERVICE_EVENT_UEI = "uei.opennms.org/nodes/nodeRegainedService";

    /**
     * The delete service event UEI
     */
    public static final String DELETE_SERVICE_EVENT_UEI = "uei.opennms.org/nodes/deleteService";

    /**
     * The service deleted event UEI
     */
    public static final String SERVICE_DELETED_EVENT_UEI = "uei.opennms.org/nodes/serviceDeleted";

    /**
     * The interface deleted event UEI
     */
    public static final String INTERFACE_DELETED_EVENT_UEI = "uei.opennms.org/nodes/interfaceDeleted";

    /**
     * The node deleted event UEI
     */
    public static final String NODE_DELETED_EVENT_UEI = "uei.opennms.org/nodes/nodeDeleted";

    /**
     * The low threshold exceeded event UEI
     */
    public static final String LOW_THRESHOLD_EVENT_UEI = "uei.opennms.org/threshold/lowThresholdExceeded";

    /**
     * The high threshold exceeded event UEI
     */
    public static final String HIGH_THRESHOLD_EVENT_UEI = "uei.opennms.org/threshold/highThresholdExceeded";

    /**
     * The high threshold rearm event UEI
     */
    public static final String HIGH_THRESHOLD_REARM_EVENT_UEI = "uei.opennms.org/threshold/highThresholdRearmed";

    /**
     * The low threshold rearm event UEI
     */
    public static final String LOW_THRESHOLD_REARM_EVENT_UEI = "uei.opennms.org/threshold/lowThresholdRearmed";

    /**
     * The relative change event UEI
     */
    public static final String RELATIVE_CHANGE_THRESHOLD_EVENT_UEI = "uei.opennms.org/threshold/relativeChangeExceeded";

    /**
     * The relative change event UEI
     */
    public static final String ABSOLUTE_CHANGE_THRESHOLD_EVENT_UEI = "uei.opennms.org/threshold/absoluteChangeExceeded";

    /**
     * ThresholdEvaluatorRearmingAbsoluteChange exceeded UEI
     */
    public static final String REARMING_ABSOLUTE_CHANGE_EXCEEDED_EVENT_UEI = "uei.opennms.org/threshold/rearmingAbsoluteChangeExceeded";

    /**
     * ThresholdEvaluatorRearmingAbsoluteChange exceeded UEI
     */
    public static final String REARMING_ABSOLUTE_CHANGE_REARM_EVENT_UEI = "uei.opennms.org/threshold/rearmingAbsoluteChangeRearmed";

    /**
     * The interface index changed event
     */
    public static final String INTERFACE_INDEX_CHANGED_EVENT_UEI = "uei.opennms.org/nodes/interfaceIndexChanged";

    /**
     * The interface supports SNMP event...generated during capability rescan
     * when an already managed interface gains SNMP support for the first time
     */
    public static final String INTERFACE_SUPPORTS_SNMP_EVENT_UEI = "uei.opennms.org/internal/capsd/interfaceSupportsSNMP";

    /**
     * A service scan has discovered a duplicate IP address.
     */
    public static final String DUPLICATE_IPINTERFACE_EVENT_UEI = "uei.opennms.org/internal/capsd/duplicateIPAddress";

    /**
     * The interface reparented event
     */
    public static final String INTERFACE_REPARENTED_EVENT_UEI = "uei.opennms.org/nodes/interfaceReparented";

    /**
     * The node info changed event
     */
    public static final String NODE_INFO_CHANGED_EVENT_UEI = "uei.opennms.org/nodes/nodeInfoChanged";

    /**
     * The interface IP host name changed event
     */
    public static final String INTERFACE_IP_HOSTNAME_CHANGED_EVENT_UEI = "uei.opennms.org/nodes/interfaceIPHostNameChanged";

    /**
     * The node label changed event
     */
    public static final String NODE_LABEL_CHANGED_EVENT_UEI = "uei.opennms.org/nodes/nodeLabelChanged";

    /**
     * The node label source changed event
     */
    public static final String NODE_LABEL_SOURCE_CHANGED_EVENT_UEI = "uei.opennms.org/nodes/nodeLabelSourceChanged";

    /**
     * The node deleted event UEI
     */
    public static final String DUP_NODE_DELETED_EVENT_UEI = "uei.opennms.org/nodes/duplicateNodeDeleted";

    /**
     * The primary SNMP interface changed event.
     */
    public static final String PRIMARY_SNMP_INTERFACE_CHANGED_EVENT_UEI = "uei.opennms.org/nodes/primarySnmpInterfaceChanged";

    /**
     * The reinitialize primary SNMP interface event.
     */
    public static final String REINITIALIZE_PRIMARY_SNMP_INTERFACE_EVENT_UEI = "uei.opennms.org/nodes/reinitializePrimarySnmpInterface";

    /**
     * The configure SNMP event.
     */
    public static final String CONFIGURE_SNMP_EVENT_UEI = "uei.opennms.org/internal/configureSNMP";

    /**
     * Collection failed
     */
    public static final String DATA_COLLECTION_FAILED_EVENT_UEI = "uei.opennms.org/nodes/dataCollectionFailed";

    /**
     * Collection succeeded
     */
    public static final String DATA_COLLECTION_SUCCEEDED_EVENT_UEI = "uei.opennms.org/nodes/dataCollectionSucceeded";

    /**
     * Thresholding failed
     */
    public static final String THRESHOLDING_FAILED_EVENT_UEI = "uei.opennms.org/nodes/thresholdingFailed";

    /**
     * Thresholding succeeded
     */
    public static final String THRESHOLDING_SUCCEEDED_EVENT_UEI = "uei.opennms.org/nodes/thresholdingSucceeded";

    /**
     * The force interface rescan event UEI
     */
    public static final String FORCE_RESCAN_EVENT_UEI = "uei.opennms.org/internal/capsd/forceRescan";

    /**
     * The suspend polling service event UEI
     */
    public static final String SUSPEND_POLLING_SERVICE_EVENT_UEI = "uei.opennms.org/internal/poller/suspendPollingService";

    /**
     * The resume polling service event UEI
     */
    public static final String RESUME_POLLING_SERVICE_EVENT_UEI = "uei.opennms.org/internal/poller/resumePollingService";

    /**
     * The snmp conflicts with db UEI
     */
    public static final String SNMP_CONFLICTS_WITH_DB_EVENT_UEI = "uei.opennms.org/internal/capsd/snmpConflictsWithDb";

    /**
     * The rescan completed UEI
     */
    public static final String RESCAN_COMPLETED_EVENT_UEI = "uei.opennms.org/internal/capsd/rescanCompleted";

    /**
     * The suspect scan completed UEI
     */
    public static final String SUSPECT_SCAN_COMPLETED_EVENT_UEI = "uei.opennms.org/internal/capsd/suspectScanCompleted";

    /**
     * The RTC subscribe event
     */
    public static final String RTC_SUBSCRIBE_EVENT_UEI = "uei.opennms.org/internal/rtc/subscribe";

    /**
     * The RTC unsubscribe event
     */
    public static final String RTC_UNSUBSCRIBE_EVENT_UEI = "uei.opennms.org/internal/rtc/unsubscribe";

    /**
     * An event used by queued to indicate that data for certain rrds should be immediately flushed to the disk
     */
    public static final String PROMOTE_QUEUE_DATA_UEI = "uei.opennms.org/internal/promoteQueueData";

    /**
     * A service poll returned an unknown status (due to a problem getting poll
     * information)
     */
    public static final String SERVICE_STATUS_UNKNOWN = "uei.opennms.org/internal/unknownServiceStatus";

    /**
     * Notification without users event
     */
    public static final String NOTIFICATION_WITHOUT_USERS = "uei.opennms.org/internal/notificationWithoutUsers";

    /**
     * A vulnerability scan on a specific interface was initiated by the user
     * via the web UI
     */
    public static final String SPECIFIC_VULN_SCAN_EVENT_UEI = "uei.opennms.org/vulnscand/specificVulnerabilityScan";

    /**
     * Demand poll service event ui
     */
    public static final String DEMAND_POLL_SERVICE_EVENT_UEI = "uei.opennms.org/internal/demandPollService";

    /**
     * An event to signal that a user has changed asset information via the web
     * UI
     */
    public static final String ASSET_INFO_CHANGED_EVENT_UEI = "uei.opennms.org/nodes/assetInfoChanged";

    /**
        * The scheduled-outages configuration was changed by the user via the web UI (or manually, for that matter)
        */
    public static final String SCHEDOUTAGES_CHANGED_EVENT_UEI = "uei.opennms.org/internal/schedOutagesChanged";

    /**
     * The threshold config was changed by the user via the web UI, or manually
     */
    public static final String THRESHOLDCONFIG_CHANGED_EVENT_UEI = "uei.opennms.org/internal/thresholdConfigChange";

    /**
     * The event config was changed by the user via the web UI, or manually, and should be reloaded
     */
    public static final String EVENTSCONFIG_CHANGED_EVENT_UEI = "uei.opennms.org/internal/eventsConfigChange";

    /**
     * The Snmp Poller config was changed by the user via the web UI, or manually, and should be reloaded
     */
    public static final String SNMPPOLLERCONFIG_CHANGED_EVENT_UEI = "uei.opennms.org/internal/reloadSnmpPollerConfig";

    /**
     * Reload Vacuumd configuration UEI
     */
    public static final String RELOAD_VACUUMD_CONFIG_UEI = "uei.opennms.org/internal/reloadVacuumdConfig";

    /**
     * Reload Daemon configuration UEI
     */
    public static final String RELOAD_DAEMON_CONFIG_UEI = "uei.opennms.org/internal/reloadDaemonConfig";

    public static final String RELOAD_DAEMON_CONFIG_FAILED_UEI = "uei.opennms.org/internal/reloadDaemonConfigFailed";

    public static final String RELOAD_DAEMON_CONFIG_SUCCESSFUL_UEI = "uei.opennms.org/internal/reloadDaemonConfigSuccessful";

    public static final String PARM_DAEMON_NAME = "daemonName";

    public static final String REMOTE_NODE_LOST_SERVICE_UEI = "uei.opennms.org/remote/nodes/nodeLostService";

    public static final String REMOTE_NODE_REGAINED_SERVICE_UEI = "uei.opennms.org/remote/nodes/nodeRegainedService";

    public static final String LOCATION_MONITOR_REGISTERED_UEI = "uei.opennms.org/remote/locationMonitorRegistered";

    public static final String LOCATION_MONITOR_STARTED_UEI = "uei.opennms.org/remote/locationMonitorStarted";

    public static final String LOCATION_MONITOR_STOPPED_UEI = "uei.opennms.org/remote/locationMonitorStopped";

    public static final String LOCATION_MONITOR_PAUSED_UEI = "uei.opennms.org/remote/locationMonitorPaused";

    public static final String LOCATION_MONITOR_DISCONNECTED_UEI = "uei.opennms.org/remote/locationMonitorDisconnected";

    public static final String LOCATION_MONITOR_RECONNECTED_UEI = "uei.opennms.org/remote/locationMonitorReconnected";

    public static final String LOCATION_MONITOR_CONFIG_CHANGE_DETECTED_UEI = "uei.opennms.org/remote/configurationChangeDetected";

    public static final String RELOAD_IMPORT_UEI = "uei.opennms.org/internal/importer/reloadImport";

    public static final String IMPORT_STARTED_UEI = "uei.opennms.org/internal/importer/importStarted";

    public static final String IMPORT_SUCCESSFUL_UEI = "uei.opennms.org/internal/importer/importSuccessful";

    public static final String IMPORT_FAILED_UEI = "uei.opennms.org/internal/importer/importFailed";

    public static final String PROVISIONING_ADAPTER_FAILED = "uei.opennms.org/provisioner/provisioningAdapterFailed";

    public static final String PROVISION_SCAN_COMPLETE_UEI = "uei.opennms.org/internal/provisiond/nodeScanCompleted";

    public static final String PROVISION_SCAN_ABORTED_UEI = "uei.opennms.org/internal/provisiond/nodeScanAborted";

    public static final String PARM_FAILURE_MESSAGE = "failureMessage";

    public static final String PARM_IMPORT_STATS = "importStats";

    public static final String PARM_IMPORT_RESOURCE = "importResource";

    public static final String PARM_ALARM_ID = "alarmId";

    public static final String PARM_ALARM_UEI = "alarmUei";

    public static final String PARM_TROUBLE_TICKET = "troubleTicket";

    public static final String TROUBLETICKET_CREATE_UEI = "uei.opennms.org/troubleTicket/create";

    public static final String TROUBLETICKET_UPDATE_UEI = "uei.opennms.org/troubleTicket/update";

    public static final String TROUBLETICKET_CLOSE_UEI = "uei.opennms.org/troubleTicket/close";

    public static final String TROUBLETICKET_CANCEL_UEI = "uei.opennms.org/troubleTicket/cancel";

    public static final String TL1_AUTONOMOUS_MESSAGE_UEI = "uei.opennms.org/api/tl1d/message/autonomous";

    public static final String RANCID_DOWNLOAD_SUCCESS_UEI = "uei.opennms.org/standard/rancid/traps/rancidTrapDownloadSuccess";

    public static final String RANCID_DOWNLOAD_FAILURE_UEI = "uei.opennms.org/standard/rancid/traps/rancidTrapDownloadFailure";

    public static final String RANCID_GROUP_PROCESSING_COMPLETED_UEI = "uei.opennms.org/standard/rancid/traps/rancidTrapGroupProcessingCompleted";

    public static final String DATA_LINK_FAILED_EVENT_UEI = "uei.opennms.org/internal/linkd/dataLinkFailed";

    public static final String DATA_LINK_RESTORED_EVENT_UEI = "uei.opennms.org/internal/linkd/dataLinkRestored";

    public static final String DATA_LINK_UNMANAGED_EVENT_UEI = "uei.opennms.org/internal/linkd/dataLinkUnmanaged";

    /**
     * The criticalPathIp used in determining if a node down event is
     * due to a path outage
     */
    public static final String PARM_CRITICAL_PATH_IP = "criticalPathIp";

    /**
     * The criticalPathServiceName used in determining if a node down event is
     * due to a path outage
     */
    public static final String PARM_CRITICAL_PATH_SVC = "criticalPathServiceName";

    /**
     * This parameter is set to true if a critical path outage has resulted in the
     * supression of a notification
     */
    public static final String PARM_CRITICAL_PATH_NOTICE_SUPRESSED = "noticeSupressed";

    /**
     * This parameter is set to indicate the id of the demandPoll object to store the results
     * of a demandPoll in
     */
    public static final String PARM_DEMAND_POLL_ID = "demandPollId";

    /**
     * The nodeSysName from the node table when sent as an event parm
     */
    public static final String PARM_NODE_SYSNAME = "nodesysname";

    /**
     * The nodeSysDescription from the node table when sent as an event parm
     */
    public static final String PARM_NODE_SYSDESCRIPTION = "nodesysdescription";

    /**
     * The nodeSysOid from the node table when sent as an event parm
     */
    public static final String PARM_NODE_SYSOID = "nodesysoid";

    /**
     * The nodeSysLocation from the node table when sent as an event parm
     */
    public static final String PARM_NODE_SYSLOCATION = "nodesyslocation";

    /**
     * The nodeSysContact from the node table when sent as an event parm
     */
    public static final String PARM_NODE_SYSCONTACT = "nodesyscontact";

    /**
     * The ipHostName from the ipinterface table when sent as an event parm
     */
    public static final String PARM_IP_HOSTNAME = "iphostname";

    /**
     * The original ipHostName from the ipinterface table when sent as an event
     * parm
     */
    public static final String PARM_OLD_IP_HOSTNAME = "oldiphostname";

    /**
     * Name of the method of discovery when sent as an event parm
     */
    public static final String PARM_METHOD = "method";

    /**
     * The interface sent as a parm of an event
     */
    public static final String PARM_INTERFACE = "interface";

    /**
     * The action sent as a parm of an event
     */
    public static final String PARM_ACTION = "action";

    /**
     * The DPName sent as a parm of an event
     */
    public static final String PARM_DPNAME = "dpName";

    /**
     * The old nodeid sent as a parm of the 'interfaceReparented' event
     */
    public static final String PARM_OLD_NODEID = "oldNodeID";

    /**
     * The new nodeid sent as a parm of the 'interfaceReparented' event
     */
    public static final String PARM_NEW_NODEID = "newNodeID";

    /**
     * The old ifIndex value sent as a parm of the 'interfaceIndexChanged' event
     */
    public static final String PARM_OLD_IFINDEX = "oldIfIndex";

    /**
     * The new ifIndex value sent as a parm of the 'interfaceIndexChanged' event
     */
    public static final String PARM_NEW_IFINDEX = "newIfIndex";

    /**
     * The nodeLabel from the node table when sent as an event parm
     */
    public static final String PARM_NODE_LABEL = "nodelabel";

    /**
     * The nodeLabelSource from the node table when sent as an event parm
     */
    public static final String PARM_NODE_LABEL_SOURCE = "nodelabelsource";

    /**
     * The oldNodeLabel sent as a parm of an event
     */
    public static final String PARM_OLD_NODE_LABEL = "oldNodeLabel";

    /**
     * The oldNodeLabelSource sent as a parm of an event
     */
    public static final String PARM_OLD_NODE_LABEL_SOURCE = "oldNodeLabelSource";

    /**
     * The newNodeLabel sent as a parm of an event
     */
    public static final String PARM_NEW_NODE_LABEL = "newNodeLabel";

    /**
     * The newNodeLabelSource sent as a parm of an event
     */
    public static final String PARM_NEW_NODE_LABEL_SOURCE = "newNodeLabelSource";

    /**
     * The nodeNetbiosName field from the node table when sent as an event parm
     */
    public static final String PARM_NODE_NETBIOS_NAME = "nodenetbiosname";

    /**
     * The nodeDomainName field from the node table when sent as an event parm
     */
    public static final String PARM_NODE_DOMAIN_NAME = "nodedomainname";

    /**
     * The operatingSystem field from the node table when sent as an event parm
     */
    public static final String PARM_NODE_OPERATING_SYSTEM = "nodeoperatingsystem";

    /**
     * The old value of the primarySnmpInterface field of the ipInterface table
     * when sent as an event parm.
     */
    public static final String PARM_OLD_PRIMARY_SNMP_ADDRESS = "oldPrimarySnmpAddress";

    /**
     * The new value of the primarySnmpInterface field of the ipInterface table
     * when sent as an event parm.
     */
    public static final String PARM_NEW_PRIMARY_SNMP_ADDRESS = "newPrimarySnmpAddress";

    /**
     * The first IP address in a range of IP addresses when sent as an event
     * parm.
     */
    public static final String PARM_FIRST_IP_ADDRESS = "firstIPAddress";

    /**
     * The last IP address in a range of IP addresses when sent as an event
     * parm.
     */
    public static final String PARM_LAST_IP_ADDRESS = "lastIPAddress";

    /**
     * The SNMP community string when sent as an event parm.
     */
    public static final String PARM_COMMUNITY_STRING = "communityString";

    /**
     * Service monitor qualifier when sent as an event parm
     */
    public static final String PARM_QUALIFIER = "qualifier";

    /**
     * The URL to which information is to be sent, sent as a parm to the rtc
     * subscribe and unsubscribe events
     */
    public static final String PARM_URL = "url";

    /**
     * The category for which information is to be sent, sent as a parm to the
     * rtc subscribe event
     */
    public static final String PARM_CAT_LABEL = "catlabel";

    /**
     * The username when sent as a parameter(like for the rtc subscribe)
     */
    public static final String PARM_USER = "user";

    /**
     * The passwd when sent as a parameter(like for the rtc subscribe)
     */
    public static final String PARM_PASSWD = "passwd";

    /**
     * The status of a service as returned from a service monitor
     */
    public static final String PARM_SERVICE_STATUS = "serviceStatus";

    /**
     * The external transaction number of an event to process.
     */
    public static final String PARM_TRANSACTION_NO = "txno";

    /**
     * The uei of a source event to report to external xmlrpc server.
     */
    public static final String PARM_SOURCE_EVENT_UEI = "sourceUei";

    /**
     * The message to explain a source event.
     */
    public static final String PARM_SOURCE_EVENT_MESSAGE = "eventMessage";

    /**
     * The status to indicate which kind of external xmlrpc command to invoke.
     */
    public static final String PARM_SOURCE_EVENT_STATUS = "eventStatus";

    /**
     * Used for retaining the reason from a monitor determines SERVICE_UNAVAILABLE
     */
    public static final String PARM_LOSTSERVICE_REASON = "eventReason";

    /**
     * Used for setting the value for  PARM_LOSTSERVICE_REASON when the lost
     * service is due to a critical path outage
     */
    public static final String PARM_VALUE_PATHOUTAGE = "pathOutage";

    /**
     * Parms used for passive status events sent to the PassiveServiceKeeper
     */
    public static final String PARM_PASSIVE_NODE_LABEL = "passiveNodeLabel";

    public static final String PARM_PASSIVE_IPADDR = "passiveIpAddr";

    public static final String PARM_PASSIVE_SERVICE_NAME = "passiveServiceName";

    public static final String PARM_PASSIVE_SERVICE_STATUS = "passiveStatus";

    public static final String PARM_PASSIVE_REASON_CODE = "passiveReasonCode";

    /**
     * Parm used to importer event
     */
    public static final String PARM_FOREIGN_SOURCE = "foreignSource";

    public static final String PARM_FOREIGN_ID = "foreignId";

    /**
     * Parms used for configureSnmp events
     */
    public static final String PARM_VERSION = "version";

    public static final String PARM_TIMEOUT = "timeout";

    public static final String PARM_RETRY_COUNT = "retryCount";

    public static final String PARM_PORT = "port";

    public static final String PARM_LOCATION_MONITOR_ID = "locationMonitorId";

    /**
     * Parm use for promoteEnqueuedData event
     */
    public static final String PARM_FILES_TO_PROMOTE = "filesToPromote";

    /**
     * Parameter used in event snmp poller definition
     */
    public static final String PARM_SNMP_INTERFACE_IFINDEX = "snmpifindex";

    public static final String PARM_SNMP_INTERFACE_IP = "ipaddr";

    public static final String PARM_SNMP_INTERFACE_NAME = "snmpifname";

    public static final String PARM_SNMP_INTERFACE_DESC = "snmpifdescr";

    public static final String PARM_SNMP_INTERFACE_ALIAS = "snmpifalias";

    public static final String PARM_SNMP_INTERFACE_MASK = "mask";

    /**
     * Status code used to indicate which external xmlrpc command to invoke to
     * report the occurrence of selected events.
     */
    public static final int XMLRPC_NOTIFY_RECEIVED = 0;

    public static final int XMLRPC_NOTIFY_SUCCESS = 1;

    public static final int XMLRPC_NOTIFY_FAILURE = 2;

    /**
     * Enumerated values for severity being indeterminate
     * @deprecated see OnmsSeverity.class
     */
    public static final int SEV_INDETERMINATE = 1;

    /**
     * Enumerated values for severity being unimplemented at this time
     * @deprecated see OnmsSeverity.class
     */
    public static final int SEV_CLEARED = 2;

    /**
     * Enumerated values for severity indicates a warning
     * @deprecated see OnmsSeverity.class
     */
    public static final int SEV_NORMAL = 3;

    /**
     * Enumerated values for severity indicates a warning
     * @deprecated see OnmsSeverity.class
     */
    public static final int SEV_WARNING = 4;

    /**
     * Enumerated values for severity is minor
     * @deprecated see OnmsSeverity.class
     */
    public static final int SEV_MINOR = 5;

    /**
     * Enumerated values for severity is major
     * @deprecated see OnmsSeverity.class
     */
    public static final int SEV_MAJOR = 6;

    /**
     * Enumerated values for severity is critical
     * @deprecated see OnmsSeverity.class
     */
    public static final int SEV_CRITICAL = 7;

    /**
     * Enumerated value for the state(tticket and forward) when entry is active
     */
    public static final int STATE_ON = 1;

    /**
     * Enumerated value for the state(tticket and forward) when entry is not
     * active
     */
    static final int STATE_OFF = 0;

    /**
     * UEI used for requesting an acknowledgment of an OnmsAcknowledgeable
     */
    public static final String ACKNOWLEDGE_EVENT_UEI = "uei.opennms.org/ackd/acknowledge";

    /**
     * UEI used for indicating an OnmsAcknowledgeable has been acknowledged
     */
    public static final String EVENT_ACKNOWLEDGED_UEI = "uei.opennms.org/ackd/acknowledgment";

    /**
     * UEI used for indicating a change management event
     */
    public static final String NODE_CONFIG_CHANGE_UEI = "uei.opennms.org/internal/translator/entityConfigChanged";

    /**
     * Used for indicating a reason message in an event or alarm.
     */
    public static final String PARM_REASON = "reason";

    /**
     * Used for indication the first endpoint to a map link
     */
    public static final String PARM_ENDPOINT1 = "endPoint1";

    /**
     * Used for indication the second endpoint to a map link
     */
    public static final String PARM_ENDPOINT2 = "endPoint2";

    /**
     * An utility method to parse a string into a 'Date' instance. Note that the
     * string should be in the locale specific DateFormat.FULL style for both
     * the date and time.
     * 
     * @see java.text.DateFormat
     */
    public static final Date parseToDate(String timeString) throws ParseException {
        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).parse(timeString);
    }

    /**
     * An utility method to format a 'Date' into a string in the local specific
     * FULL DateFormat style for both the date and time.
     * 
     * @see java.text.DateFormat
     */
    public static final String formatToString(Date date) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }

    /**
     * An utility method to format a 'Date' into a string in the local specific
     * DEFALUT DateFormat style for both the date and time. This is used by the
     * webui and a change here should get all time display in the webui changed.
     * 
     * @see java.text.DateFormat
     * @deprecated This is no longer used by the UI. All WebUI-specific code
     *             should under the org.opennms.web packages.
     * @see org.opennms.web.Util.formatDateToUIString
     */
    public static final String formatToUIString(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date);
    }

    /**
     * Converts the severity to an integer
     * 
     * @return integer equivalent for the severity
     */
    public static int getSeverity(String sev) {
        int rc = SEV_INDETERMINATE;
        if (sev != null) {
            sev = sev.trim();
            if (sev.equalsIgnoreCase("normal")) {
                rc = SEV_NORMAL;
            } else if (sev.equalsIgnoreCase("warning")) {
                rc = SEV_WARNING;
            } else if (sev.equalsIgnoreCase("minor")) {
                rc = SEV_MINOR;
            } else if (sev.equalsIgnoreCase("major")) {
                rc = SEV_MAJOR;
            } else if (sev.equalsIgnoreCase("critical")) {
                rc = SEV_CRITICAL;
            } else if (sev.equalsIgnoreCase("cleared")) {
                rc = SEV_CLEARED;
            }
        }
        return rc;
    }

    /**
     * Returns a severity constant as a printable string.
     * 
     * @param sev
     * @return A capitalized String representing severity.
     */
    public static String getSeverityString(int sev) {
        String retString = null;
        switch(sev) {
            case SEV_CLEARED:
                retString = "Cleared";
                break;
            case SEV_CRITICAL:
                retString = "Critical";
                break;
            case SEV_MAJOR:
                retString = "Major";
                break;
            case SEV_MINOR:
                retString = "Minor";
                break;
            case SEV_NORMAL:
                retString = "Normal";
                break;
            case SEV_WARNING:
                retString = "Warning";
                break;
            default:
                retString = "Indeterminate";
        }
        return retString;
    }
}
