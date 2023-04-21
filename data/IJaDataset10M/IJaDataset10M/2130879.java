package org.apache.axis2.jaxws.sample.addnumbershandler;

import java.io.FileWriter;

public class HandlerTracker {

    private static final String filelogname = "AddNumbersHandlerTests.log";

    private String classname;

    private static final String CLOSE = "CLOSE";

    private static final String GET_HEADERS = "GET_HEADERS";

    private static final String HANDLE_FAULT_INBOUND = "HANDLE_FAULT_INBOUND";

    private static final String HANDLE_MESSAGE_INBOUND = "HANDLE_MESSAGE_INBOUND";

    private static final String HANDLE_FAULT_OUTBOUND = "HANDLE_FAULT_OUTBOUND";

    private static final String HANDLE_MESSAGE_OUTBOUND = "HANDLE_MESSAGE_OUTBOUND";

    private static final String POST_CONSTRUCT = "POST_CONSTRUCT";

    private static final String PRE_DESTROY = "PRE_DESTROY";

    public HandlerTracker(String name) {
        classname = name;
    }

    public void postConstruct() {
        log_to_file(POST_CONSTRUCT);
    }

    public void preDestroy() {
        log_to_file(PRE_DESTROY);
    }

    public void close() {
        log_to_file(CLOSE);
    }

    public void getHeaders() {
        log_to_file(GET_HEADERS);
    }

    public void handleFault(boolean outbound) {
        if (outbound) {
            log_to_file(HANDLE_FAULT_OUTBOUND);
        } else {
            log_to_file(HANDLE_FAULT_INBOUND);
        }
    }

    public void handleMessage(boolean outbound) {
        if (outbound) {
            log_to_file(HANDLE_MESSAGE_OUTBOUND);
        } else {
            log_to_file(HANDLE_MESSAGE_INBOUND);
        }
    }

    public void log(String msg, boolean outbound) {
        log_to_file(msg + " " + (outbound ? "OUTBOUND" : "INBOUND"));
    }

    private void log_to_file(String msg) {
        try {
            FileWriter fw = new FileWriter(filelogname, true);
            fw.write(classname + " " + msg + "\n");
            fw.close();
        } catch (Exception e) {
        }
    }
}
