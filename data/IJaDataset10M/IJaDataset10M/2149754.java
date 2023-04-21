package in.espirit.tracer.action;

import in.espirit.tracer.database.dao.TicketDao;
import in.espirit.tracer.model.Ticket;
import java.util.ArrayList;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/relatedlist/{type}/{id}")
public class RelatedListActionBean extends BaseActionBean {

    private static final String URL = "/WEB-INF/jsp/list_rel.jsp";

    private String type, id;

    public ArrayList<? extends Ticket> getRelatedList() throws Exception {
        return TicketDao.getRelatedTickets(id, type);
    }

    @DefaultHandler
    public Resolution open() {
        return new ForwardResolution(URL);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
