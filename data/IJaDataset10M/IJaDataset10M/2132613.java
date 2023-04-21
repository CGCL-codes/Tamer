package pyrasun.eio.protocols;

import pyrasun.eio.Endpoint;
import pyrasun.eio.EIOReasonCode;

public interface ServerProtocolListener {

    public void endpointCreated(Endpoint endpoint);

    public void endpointClosed(Endpoint endpoint, EIOReasonCode why, String msg, Exception e);

    public void serverError(EIOReasonCode what, String msg, Exception e);
}
