package org.doit.muffin.filter;

import java.io.InputStream;
import org.doit.muffin.*;
import java.util.Enumeration;
import uk.co.badgersinfoil.flight.recorder.AgentRecorder;
import uk.co.badgersinfoil.flight.HttpClientResponse;
import uk.co.badgersinfoil.flight.HttpClientRequest;
import uk.co.badgersinfoil.flight.HttpMessage;

public class FlightRecorderFilter implements RequestFilter, ReplyFilter {

    private Prefs prefs;

    private AgentRecorder recorder;

    private HttpClientRequest initialRequest;

    public FlightRecorderFilter(AgentRecorder r) {
        recorder = r;
    }

    public void filter(Request r) {
        HttpClientRequest request = new HttpClientRequest();
        request.setRequestURI(r.getURL());
        request.setMethod(r.getCommand());
        copyHeaders(r, request);
        InputStream in = r.getInputStream();
        if (in != null) {
            request.setInputStream(in);
        }
        initialRequest = request;
    }

    public void filter(Reply reply) {
        HttpClientResponse response = new HttpClientResponse();
        response.setStatus(reply.getStatusCode());
        response.setInputStream(reply.getContent());
        copyHeaders(reply, response);
        recorder.log(initialRequest, response);
    }

    public void setPrefs(Prefs p) {
        prefs = p;
    }

    private void copyHeaders(Message from, HttpMessage to) {
        Enumeration e = from.getHeaders();
        while (e.hasMoreElements()) {
            String header = (String) e.nextElement();
            int count = from.getHeaderValueCount(header);
            for (int i = 0; i < count; i++) {
                to.addHeader(header, from.getHeaderField(header, i));
            }
        }
    }
}
