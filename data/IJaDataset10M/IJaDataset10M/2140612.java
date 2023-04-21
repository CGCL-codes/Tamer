package org.tolven.scheduler.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.log4j.Logger;
import org.tolven.restful.client.RESTfulClient;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Scheduler extends RESTfulClient {

    private Logger logger = Logger.getLogger(RESTfulClient.class);

    public Scheduler(String appRestfulURL, String authRestfulURL, String userId, char[] password) {
        init(userId, password, appRestfulURL, authRestfulURL);
    }

    public void start(long interval) {
        try {
            WebResource webResource = getAppWebResource().path("scheduler/interval");
            MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
            formData.putSingle("interval", String.valueOf(interval));
            ClientResponse response = webResource.cookie(getTokenCookie()).accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Error: " + response.getStatus() + " POST " + getUserId() + " " + webResource.getURI() + " " + response.getEntity(String.class));
            }
            System.out.println("\nscheduler started: " + interval + "\n");
            logger.info("scheduler started: " + interval);
        } catch (Exception ex) {
            throw new RuntimeException("Could not start scheduler", ex);
        } finally {
            logout();
        }
    }

    public void stop() {
        try {
            WebResource webResource = getAppWebResource().path("scheduler/stop");
            ClientResponse response = webResource.cookie(getTokenCookie()).accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Error: " + response.getStatus() + " POST " + getUserId() + " " + webResource.getURI() + " " + response.getEntity(String.class));
            }
            System.out.println("\n scheduler stopped\n");
            logger.info(" scheduler stopped");
        } catch (Exception ex) {
            throw new RuntimeException("Could not stop scheduler", ex);
        } finally {
            logout();
        }
    }

    public void timeout() {
        try {
            WebResource webResource = getAppWebResource().path("scheduler/timeout");
            ClientResponse response = webResource.cookie(getTokenCookie()).accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Error: " + response.getStatus() + " POST " + getUserId() + " " + webResource.getURI() + " " + response.getEntity(String.class));
            }
            MultivaluedMap<String, String> propertiesMap = response.getEntity(MultivaluedMap.class);
            String timeoutString = propertiesMap.getFirst("timeout");
            if (timeoutString == null || timeoutString.length() == 0) {
                System.out.println("\nnot running\n");
            } else {
                Date nextTimeout = new Date(Long.parseLong(timeoutString));
                String dateString = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nextTimeout);
                System.out.println("\nnext timeout: " + dateString + "\n");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not get scheduler status", ex);
        } finally {
            logout();
        }
    }
}
