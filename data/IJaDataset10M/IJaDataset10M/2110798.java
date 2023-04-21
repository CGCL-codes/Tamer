package Request;

import Response.NetDataResponse;
import java.net.*;
import java.io.ObjectOutput;

/**
 *
 * @author lcy
 */
public class RequestRdyArg {

    private long _request_id;

    private NetDataResponse _response;

    public RequestRdyArg(NetDataResponse response) {
        _response = response;
    }

    public NetDataResponse GetResponse() {
        return _response;
    }

    public void SetRequestId(long id) {
        _request_id = id;
    }

    public long GetRequestId() {
        return _request_id;
    }
}
