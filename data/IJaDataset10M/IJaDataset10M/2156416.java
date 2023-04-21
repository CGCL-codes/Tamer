package com.google.code.sagetvaddons.sagealert.server.events;

import java.util.Arrays;
import java.util.List;
import com.google.code.sagetvaddons.sagealert.server.ApiInterpreter;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
public abstract class ClientConnectionEvent implements SageAlertEvent {

    public static final List<String> EVENT_ARG_TYPES = Arrays.asList(new String[] { Client.class.getName() });

    private Client clnt;

    private SageAlertEventMetadata metadata;

    public ClientConnectionEvent(Client c, SageAlertEventMetadata data) {
        clnt = c;
        metadata = data;
    }

    public String getLongDescription() {
        return new ApiInterpreter(new Object[] { clnt }, metadata.getLongMsg()).interpret();
    }

    public String getMediumDescription() {
        return new ApiInterpreter(new Object[] { clnt }, metadata.getMedMsg()).interpret();
    }

    public String getShortDescription() {
        return new ApiInterpreter(new Object[] { clnt }, metadata.getShortMsg()).interpret();
    }

    public abstract String getSource();

    public String getSubject() {
        return new ApiInterpreter(new Object[] { clnt }, metadata.getSubject()).interpret();
    }
}
