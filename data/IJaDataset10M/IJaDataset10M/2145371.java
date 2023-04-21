package org.tolven.restful;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.tolven.doc.TolvenMessageSchedulerLocal;
import org.tolven.util.ExceptionFormatter;

@Path("scheduler")
@ManagedBean
public class SchedulerResources {

    @EJB
    private TolvenMessageSchedulerLocal tolvenMessageSchedulerLocal;

    @Path("interval")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setScheduler(@FormParam("interval") String interval) {
        try {
            tolvenMessageSchedulerLocal.setScheduler(Long.parseLong(interval));
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.status(500).type(MediaType.TEXT_PLAIN).entity(ExceptionFormatter.toSimpleString(ex, "\\n")).build();
        }
    }

    @Path("stop")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response stopScheduler() {
        try {
            tolvenMessageSchedulerLocal.stopScheduler();
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.status(500).type(MediaType.TEXT_PLAIN).entity(ExceptionFormatter.toSimpleString(ex, "\\n")).build();
        }
    }

    @Path("timeout")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getNextTimeout() {
        try {
            Date timeout = tolvenMessageSchedulerLocal.getNextTimeout();
            String timestamp = null;
            if (timeout == null) {
                timestamp = "";
            } else {
                getTimestamp(timeout);
            }
            return Response.ok(timestamp).build();
        } catch (Exception ex) {
            return Response.status(500).type(MediaType.TEXT_PLAIN).entity(ExceptionFormatter.toSimpleString(ex, "\\n")).build();
        }
    }

    private String getTimestamp(Date now) {
        GregorianCalendar nowGC = new GregorianCalendar();
        nowGC.setTime(now);
        DatatypeFactory xmlFactory = null;
        try {
            xmlFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException("Could not create instance of DatatypeFactory", ex);
        }
        XMLGregorianCalendar ts = xmlFactory.newXMLGregorianCalendar(nowGC);
        return ts.toXMLFormat();
    }
}
