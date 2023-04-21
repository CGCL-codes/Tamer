package org.smartcti.freeswitch.inbound.internal;

import java.util.Map;
import org.smartcti.freeswitch.inbound.response.CommandResponse;

interface ResponseBuilder {

    CommandResponse buildResponse(Map<String, String> attributes);
}
