package org.simpleframework.http.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.simpleframework.http.Cookie;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.PerformanceXmlConfiguration.Method;
import org.simpleframework.xml.core.Persister;

public class PerformanceRecorder extends Thread implements Container {

    private List<PerformanceXmlConfiguration.Request> requests;

    private Persister persister;

    private Container container;

    public PerformanceRecorder(Container container) {
        this.requests = new Vector<PerformanceXmlConfiguration.Request>();
        this.persister = new Persister();
        this.container = container;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                if (requests.size() > 0) {
                    List<PerformanceXmlConfiguration.Request> list = new ArrayList<PerformanceXmlConfiguration.Request>(requests);
                    PerformanceXmlConfiguration configuration = new PerformanceXmlConfiguration(list, 1);
                    persister.write(configuration, System.out);
                    requests.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handle(Request req, Response resp) {
        try {
            System.err.println(req);
            System.err.println(req.getContent());
            System.err.println(req.getForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.set("Server", "Simple/4.0.5");
        resp.setDate("Date", System.currentTimeMillis());
        container.handle(req, resp);
        try {
            String target = req.getTarget();
            String action = req.getMethod();
            Map<String, String> request = new HashMap<String, String>();
            Map<String, String> response = new HashMap<String, String>();
            Persister persister = new Persister();
            String body = req.getContent();
            Method method = Method.getMethod(action);
            List<Cookie> cookies = req.getCookies();
            int statusCode = resp.getCode();
            for (String name : req.getNames()) {
                if (!name.equalsIgnoreCase("Cookie")) {
                    request.put(name, req.getValue(name));
                }
            }
            for (String name : resp.getNames()) {
                response.put(name, resp.getValue(name));
            }
            requests.add(new PerformanceXmlConfiguration.Request(target, method, request, cookies, body, statusCode, response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
