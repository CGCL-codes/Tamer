package ws.system;

import java.io.*;
import java.util.*;

class HTTPResponse {

    DataStore store = null;

    public HTTPResponse() throws Exception {
        store = DataStore.getInstance();
    }

    public void getResponse(HTTPurl urlData, OutputStream outStream) throws Exception {
    }

    public void getResponse(HTTPurl urlData, OutputStream outStream, HashMap<String, String> headers) throws Exception {
        getResponse(urlData, outStream);
    }
}
