package in.espirit.tracer.action;

import in.espirit.tracer.database.dao.MilestoneDao;
import in.espirit.tracer.database.dao.TicketDao;
import in.espirit.tracer.model.Ticket;
import java.util.ArrayList;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.apache.log4j.Logger;

@UrlBinding("/taskboard")
public class TaskBoardActionBean extends BaseActionBean {

    private static Logger logger = Logger.getLogger(TaskBoardActionBean.class.getName());

    private static final String URL = "/WEB-INF/jsp/taskboard.jsp";

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(URL);
    }

    public ArrayList<Ticket> getBacklog() throws Exception {
        return TicketDao.getTaskBoardList(MilestoneDao.getCurrentMilestone(), "Backlog");
    }

    public ArrayList<Ticket> getDevelopment() throws Exception {
        return TicketDao.getTaskBoardList(MilestoneDao.getCurrentMilestone(), "Development");
    }

    public ArrayList<Ticket> getReadyForTest() throws Exception {
        return TicketDao.getTaskBoardList(MilestoneDao.getCurrentMilestone(), "Ready for Test");
    }

    public ArrayList<Ticket> getTesting() throws Exception {
        return TicketDao.getTaskBoardList(MilestoneDao.getCurrentMilestone(), "Testing");
    }

    public ArrayList<Ticket> getReadyForRelease() throws Exception {
        return TicketDao.getTaskBoardList(MilestoneDao.getCurrentMilestone(), "Ready for release");
    }

    public ArrayList<Ticket> getReleased() throws Exception {
        return TicketDao.getTaskBoardList(MilestoneDao.getCurrentMilestone(), "Released");
    }

    public Resolution persist() {
        boolean flag = false;
        String output = "<p>Sorry! could not save the new state </p>";
        String ticket_id = this.getContext().getRequest().getParameter("ticket_id");
        String ticket_type = this.getContext().getRequest().getParameter("ticket_type");
        String phase = this.getContext().getRequest().getParameter("phase");
        logger.debug("Ticket id:" + ticket_id + " Phase :" + phase);
        try {
            TicketDao ticket = new TicketDao();
            ticket.updatePhase(ticket_id, ticket_type, phase, getContext().getLoggedUser());
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        if (flag) {
            output = "success";
        } else {
            output = "error";
        }
        return new StreamingResolution("text/html", output);
    }
}
