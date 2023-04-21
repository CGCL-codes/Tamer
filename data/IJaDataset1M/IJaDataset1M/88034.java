package utilities.httpserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import utilities.tcpserver.events.event;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class session extends utilities.events.dispatcher implements HttpHandler, utilities.events.listener {

    private boolean headerSent = false;

    protected HttpExchange httpexchange;

    protected InputStreamReader in;

    protected OutputStream out;

    protected String body = "";

    protected int responseStatus;

    private void sendHeaders(int length) throws IOException {
        if (headerSent == false) {
            headerSent = true;
            httpexchange.sendResponseHeaders(responseStatus, length);
        }
    }

    private void send(String response) {
        try {
            sendHeaders(response.length());
            out = httpexchange.getResponseBody();
            out.write(response.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void close() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public session() {
        responseStatus = 200;
        this.addListener(this);
    }

    public void handle(HttpExchange ex) throws IOException {
        body = "";
        headerSent = false;
        httpexchange = ex;
        in = new InputStreamReader(httpexchange.getRequestBody());
        boolean ready = false;
        try {
            ready = in.ready();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ready) {
            int buf = 0;
            while (buf != -1) {
                if (buf != 0) body += buf;
                try {
                    buf = in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        fireEvent(new event(this, body, event.EventCodes.MESSAGE_RECIVED));
    }

    public String getURI() {
        URI uri = httpexchange.getRequestURI();
        return uri.toString();
    }

    public HashMap<String, String> getRequest() {
        HashMap<String, String> request = new HashMap<String, String>();
        String uri = getURI();
        String[] splitted = uri.split("\\?");
        if (splitted.length > 1) {
            splitted = splitted[1].split("&");
            for (int i = 0; i < splitted.length; i++) {
                String req = splitted[i];
                String[] reqs = req.split("=");
                if (reqs.length > 1) request.put(reqs[0], reqs[1]); else request.put(req, null);
            }
        }
        return request;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getResponseStatus(int responseStatus) {
        return responseStatus;
    }

    public void write(String response) {
        send(response);
        close();
    }

    public void write(String response, utilities.api.type type) {
        utilities.api.handler handler = utilities.api.factory.getHandler(type);
        write((String) handler.processRequest(response));
    }

    public void write() {
        this.send("");
    }

    public abstract void eventOccurred(utilities.events.event evt);
}
