package org.raken.messaging.protocol.message;

import static org.raken.messaging.constants.Protocol.failure;
import static org.raken.messaging.constants.Protocol.success;
import static org.raken.messaging.constants.Protocol.inputKeyword;
import java.lang.reflect.Type;
import org.raken.messaging.exception.RakenException;
import org.raken.messaging.exception.ServiceError;
import org.raken.messaging.ioc.MessagingIoC;
import org.raken.messaging.json.JsonHandler;
import org.raken.service.Service;

public class ServiceCall {

    private String Service;

    private String CallStatus = null;

    private Object Output = null;

    private ServiceError Error = null;

    private transient boolean Sync = true;

    private transient String rawServiceCall;

    public void executeCall(MessagingIoC container) {
        try {
            JsonHandler jHandler = container.getJsonHandler();
            String rawInput = jHandler.getEntry(rawServiceCall, inputKeyword);
            String inputClass = container.getServiceDictionary().getInputClass(Service);
            Object inputObject = jHandler.deSerializer(rawInput, container.getType(inputClass));
            Service<Object, Object> service = (Service<Object, Object>) container.getService(Service);
            Object tmpOutput = service.execute(inputObject);
            container.getServiceDictionary().assertOutputObject(Service, tmpOutput);
            Output = tmpOutput;
            CallStatus = success;
        } catch (RakenException e) {
            CallStatus = failure;
            Error = e.getServiceError();
        }
    }

    public void setRawServiceCall(String rawServiceCall) {
        this.rawServiceCall = rawServiceCall;
    }

    public boolean isSync() {
        return Sync;
    }
}
