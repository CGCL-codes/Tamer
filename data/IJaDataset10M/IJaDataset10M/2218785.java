package org.opensplice.restful.service;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;

public abstract class ReaderResource extends Resource {

    private DDSReaderProxy reader;

    public ReaderResource(Context context, Request request, Response response) {
        super(context, request, response);
        String participantName = (String) request.getAttributes().get("participantname");
        String partitionName = (String) request.getAttributes().get("partitionname");
        String topicName = (String) request.getAttributes().get("topicname");
        String typeName = (String) request.getAttributes().get("typename");
        String datareaderName = (String) request.getAttributes().get("datareadername");
        DDSRootProxy rootProxy = ((RESTfulDDSAPI) getApplication()).getRootProxy();
        DDSParticipantProxy participantProxy = rootProxy.lookupOrCreateParticipant(participantName);
        DDSTopicProxy topicProxy = participantProxy.lookupTopic(topicName, typeName);
        if (topicProxy != null) {
            DDSSubscriberProxy subscriberProxy = participantProxy.lookupOrCreateSubscriber(partitionName);
            reader = subscriberProxy.lookupOrCreateReader(datareaderName, topicName, typeName, topicProxy.getTopic());
            if (reader != null) {
                getVariants().add(new Variant(MediaType.TEXT_PLAIN));
            }
        }
    }

    protected DDSReaderProxy getReader() {
        return reader;
    }
}
