package org.opennms.netmgt.ncs.rest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.dao.EventDao;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsEvent;
import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.sun.jersey.spi.resource.PerRequest;

/**
 * Basic Web Service using REST for NCS Components
 *
 * @author <a href="mailto:brozow@opennms.org">Matt Brozowski</a>
 */
@Component
@PerRequest
@Scope("prototype")
@Path("NCS")
@Transactional
public class NCSRestService {

    private static Logger s_log = LoggerFactory.getLogger(NCSRestService.class);

    @XmlRootElement(name = "components")
    public static class ComponentList extends LinkedList<NCSComponent> {

        private static final long serialVersionUID = 8031737923157780179L;

        private int m_totalCount;

        /**
	     * <p>Constructor for OnmsNodeList.</p>
	     */
        public ComponentList() {
            super();
        }

        /**
	     * <p>Constructor for OnmsNodeList.</p>
	     *
	     * @param c a {@link java.util.Collection} object.
	     */
        public ComponentList(Collection<? extends NCSComponent> c) {
            super(c);
        }

        /**
	     * <p>getNodes</p>
	     *
	     * @return a {@link java.util.List} object.
	     */
        @XmlElement(name = "component")
        public List<NCSComponent> getComponents() {
            return this;
        }

        /**
	     * <p>setNodes</p>
	     *
	     * @param components a {@link java.util.List} object.
	     */
        public void setComponents(List<NCSComponent> components) {
            clear();
            addAll(components);
        }

        /**
	     * <p>getCount</p>
	     *
	     * @return a {@link java.lang.Integer} object.
	     */
        @XmlAttribute(name = "count")
        public int getCount() {
            return this.size();
        }

        public void setCount(final int count) {
        }

        /**
	     * <p>getTotalCount</p>
	     *
	     * @return a int.
	     */
        @XmlAttribute(name = "totalCount")
        public int getTotalCount() {
            return m_totalCount;
        }

        /**
	     * <p>setTotalCount</p>
	     *
	     * @param count a int.
	     */
        public void setTotalCount(int count) {
            m_totalCount = count;
        }
    }

    @Autowired
    NCSComponentRepository m_componentRepo;

    @Autowired
    EventDao m_eventDao;

    @Autowired
    AlarmDao m_alarmDao;

    @Context
    UriInfo m_uriInfo;

    /**
     * <p>getNodes</p>
     *
     * @return a {@link org.opennms.netmgt.model.OnmsNodeList} object.
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("{type}/{foreignSource}:{foreignId}")
    public NCSComponent getComponent(@PathParam("type") String type, @PathParam("foreignSource") String foreignSource, @PathParam("foreignId") String foreignId) {
        if (m_componentRepo == null) {
            throw new IllegalStateException("components is null");
        }
        NCSComponent component = m_componentRepo.findByTypeAndForeignIdentity(type, foreignSource, foreignId);
        if (component == null) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        return component;
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response addComponent(NCSComponent component) {
        s_log.info("addComponent: Adding component " + component);
        m_componentRepo.save(component);
        return Response.ok(component).build();
    }

    @DELETE
    @Path("{type}/{foreignSource}:{foreignId}")
    public Response deleteComponent(@PathParam("type") String type, @PathParam("foreignSource") String foreignSource, @PathParam("foreignId") String foreignId) {
        s_log.info(String.format("deleteComponent: Deleting component of type %s and foreignIdentity %s:%s", type, foreignSource, foreignId));
        NCSComponent component = m_componentRepo.findByTypeAndForeignIdentity(type, foreignSource, foreignId);
        if (component == null) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        List<NCSComponent> parents = m_componentRepo.findComponentsThatDependOn(component);
        for (NCSComponent parent : parents) {
            parent.getSubcomponents().remove(component);
        }
        m_componentRepo.delete(component);
        OnmsCriteria criteria = new OnmsCriteria(OnmsEvent.class).add(Restrictions.like("eventParms", "%componentForeignSource=" + foreignSource + "%")).add(Restrictions.like("eventParms", "%componentForeignId=" + foreignId + "%"));
        List<OnmsEvent> events = m_eventDao.findMatching(criteria);
        for (OnmsEvent event : events) {
            m_eventDao.delete(event);
        }
        m_eventDao.flush();
        OnmsCriteria alarmCriteria = new OnmsCriteria(OnmsAlarm.class).add(Restrictions.like("eventParms", "%componentForeignSource=" + foreignSource + "%")).add(Restrictions.like("eventParms", "%componentForeignId=" + foreignId + "%"));
        List<OnmsAlarm> alarms = m_alarmDao.findMatching(alarmCriteria);
        for (OnmsAlarm alarm : alarms) {
            m_alarmDao.delete(alarm);
        }
        m_alarmDao.flush();
        return Response.ok().build();
    }

    @GET
    @Path("attributes")
    public ComponentList getComponentsByAttributes() {
        List<NCSComponent> components = m_componentRepo.findComponentsWithAttribute("jnxVpnPwVpnName", "ge-3/1/4.2");
        return new ComponentList(components);
    }
}
