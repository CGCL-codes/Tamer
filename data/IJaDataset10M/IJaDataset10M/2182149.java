package net.taylor.tracker.web;

import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.taylor.lang.DateUtil;
import net.taylor.tracker.dashboard.StatusTags;
import net.taylor.tracker.entity.Milestone;
import net.taylor.tracker.entity.Tag;
import net.taylor.tracker.entity.Ticket;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.web.AbstractResource;

@Scope(ScopeType.APPLICATION)
@Name("net.taylor.tracker.web.releaseNotesResource")
@BypassInterceptors
public class ReleaseNotesResource extends AbstractResource {

    @Override
    public String getResourcePath() {
        return "/releasenotes";
    }

    @Override
    public void getResource(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        new ContextualHttpServletRequest(request) {

            @Override
            public void process() throws IOException {
                doWork(request, response);
            }
        }.run();
    }

    public void doWork(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String id = request.getParameter("milestone");
        if (StringUtils.isEmpty(id)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "milestone parameter is required.");
            return;
        } else {
            EntityManager em = (EntityManager) Component.getInstance("taylortrackerEntityManager");
            Milestone milestone = em.find(Milestone.class, Long.parseLong(id));
            if (milestone == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Milestone (" + id + ") not found.");
                return;
            }
            response.setContentType("text/plain");
            response.getOutputStream().print(milestone.getProject().getName());
            response.getOutputStream().println(" Changelog");
            response.getOutputStream().println("========================================");
            response.getOutputStream().println();
            response.getOutputStream().print("Release Notes");
            response.getOutputStream().print(" - Milestone ");
            response.getOutputStream().print(milestone.getName());
            response.getOutputStream().print(" (");
            response.getOutputStream().print(DateUtil.format(milestone.getDate()));
            response.getOutputStream().print(")");
            response.getOutputStream().println();
            Tag status = null;
            List<Ticket> tickets = getTickets(em, milestone);
            for (Ticket ticket : tickets) {
                if (!ticket.getType().equals(status)) {
                    response.getOutputStream().println();
                    response.getOutputStream().print("** ");
                    response.getOutputStream().print(ticket.getType().getName());
                    response.getOutputStream().println();
                }
                response.getOutputStream().print("    * [");
                response.getOutputStream().print(ticket.getNumber());
                response.getOutputStream().print("] - ");
                response.getOutputStream().print(ticket.getTitle());
                response.getOutputStream().println();
            }
            response.getOutputStream().flush();
        }
    }

    protected List<Ticket> getTickets(EntityManager em, Milestone milestone) {
        Query qry = em.createQuery("from Ticket " + "where milestone = :milestone " + "and status = :closed " + "order by type.name, id");
        qry.setParameter("milestone", milestone);
        qry.setParameter("closed", StatusTags.instance().getClosedStatusTag());
        return qry.getResultList();
    }
}
