package com.sun.tools.example.debug.tty;

import com.sun.jdi.*;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.EventRequest;

class ModificationWatchpointSpec extends WatchpointSpec {

    ModificationWatchpointSpec(ReferenceTypeSpec refSpec, String fieldId) throws MalformedMemberNameException {
        super(refSpec, fieldId);
    }

    /**
     * The 'refType' is known to match, return the EventRequest.
     */
    EventRequest resolveEventRequest(ReferenceType refType) throws NoSuchFieldException {
        Field field = refType.fieldByName(fieldId);
        EventRequestManager em = refType.virtualMachine().eventRequestManager();
        EventRequest wp = em.createModificationWatchpointRequest(field);
        wp.setSuspendPolicy(suspendPolicy);
        wp.enable();
        return wp;
    }

    public String toString() {
        return MessageOutput.format("watch modification of", new Object[] { refSpec.toString(), fieldId });
    }
}
