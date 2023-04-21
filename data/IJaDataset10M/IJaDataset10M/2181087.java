package org.ogce.gfac.notification;

import java.net.URI;
import org.ogce.gfac.context.MessageContext;
import org.ogce.gfac.exception.GfacException;
import edu.indiana.extreme.lead.workflow_tracking.common.DataDurationObj;
import edu.indiana.extreme.lead.workflow_tracking.common.DataObj;
import edu.indiana.extreme.lead.workflow_tracking.common.DurationObj;

public interface NotificationService {

    DataDurationObj dataReceiveFinished(DataDurationObj dataObj, String... descriptionAndAnnotation);

    DataDurationObj dataReceiveStarted(URI dataID, URI remoteLocation, URI localLocation);

    DataDurationObj dataSendFinished(DataDurationObj dataObj, String... descriptionAndAnnotation);

    DataDurationObj dataSendStarted(DataObj dataObj, URI remoteLocation);

    void dataConsumed(URI dataID, URI replica, String type, String soapElementName);

    void dataProduced(URI dataID, URI replica, String type, String soapElementName);

    DurationObj computationDuration(long durationMillis);

    DurationObj computationFinished(DurationObj compObj);

    DurationObj computationStarted();

    void exception(String... descriptionAndAnnotation);

    void flush();

    void info(String... descriptionAndAnnotation);

    void sendingResponseFailed(Throwable trace, String... descriptionAndAnnotation);

    void sendingResponseSucceeded(String... descriptionAndAnnotation);

    void sendingResult(MessageContext messageContext) throws GfacException;

    void warning(String... descriptionAndAnnotation);

    void sendingFault(String... descriptionAndAnnotation);

    void publishURL(String title, String url, String... descriptionAndAnnotation);

    void appAudit(String name, URI jobHandle, String host, String queueName, String jobId, String dName, String projectId, String rsl, String... descriptionAndAnnotation);

    void sendResourceMappingNotifications(String hostName, String... descriptionAndAnnotation);
}
