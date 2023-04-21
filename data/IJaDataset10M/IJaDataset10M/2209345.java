package org.slasoi.infrastructure.monitoring.pubsub.notifiers;

import org.apache.log4j.Logger;
import org.slasoi.common.messaging.MessagingException;
import org.slasoi.common.messaging.pubsub.Channel;
import org.slasoi.common.messaging.pubsub.PubSubManager;
import org.slasoi.common.messaging.pubsub.PubSubMessage;
import org.slasoi.infrastructure.monitoring.InfrastructureMonitoringAgent;
import org.slasoi.infrastructure.monitoring.jpa.entities.AuditRecord;
import org.slasoi.infrastructure.monitoring.jpa.entities.Vm;
import org.slasoi.infrastructure.monitoring.jpa.managers.VmManager;
import org.slasoi.infrastructure.monitoring.pubsub.messages.GetServiceEventsResponse;
import org.slasoi.infrastructure.monitoring.pubsub.messages.ServiceEventNotification;

public class MonitoringDataNotifier {

    private static Logger log = Logger.getLogger(MonitoringDataNotifier.class);

    public static void publishServiceEventNotification(AuditRecord auditRecord) throws Exception {
        log.trace("Publishing service event (AuditRecord) notification to pubsub...");
        ServiceEventNotification message = new ServiceEventNotification();
        GetServiceEventsResponse.Event event = new GetServiceEventsResponse.Event();
        message.setEvent(event);
        event.setSource(auditRecord.getSource());
        event.setTimestamp(auditRecord.getTimestamp());
        event.setType(GetServiceEventsResponse.EventType.AUDIT_RECORD);
        event.setUserId(auditRecord.getUserId());
        event.setDescription(auditRecord.getDescription());
        if (auditRecord.getFqdn() != null) {
            Vm vm = VmManager.getInstance().findVmByFqdn(auditRecord.getFqdn());
            if (vm != null) {
                event.setServiceUri(vm.getService().getServiceUrl());
            }
        }
        publishMessage(message.toJson());
        log.trace("AuditRecord published successfully.");
    }

    private static void publishMessage(String payload) throws MessagingException {
        log.trace("Publishing message to monitoringDataRequestChannel: " + payload);
        PubSubManager pubSubManager = InfrastructureMonitoringAgent.getInstance().getPubSubManager();
        Channel monitoringDataRequestChannel = InfrastructureMonitoringAgent.getInstance().getMonitoringDataRequestChannel();
        PubSubMessage pubSubMessage = new PubSubMessage(monitoringDataRequestChannel.getName(), payload);
        pubSubManager.publish(pubSubMessage);
    }
}
