package Request;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author lcy
 */
public class RequestResultReadyEvent extends EventObject {

    private RequestRdyArg _arg;

    public RequestResultReadyEvent(Object source, RequestRdyArg arg) {
        super(source);
        _arg = arg;
    }

    public RequestRdyArg GetArg() {
        return _arg;
    }
}
