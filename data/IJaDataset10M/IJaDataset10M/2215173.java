package lt.bsprendimai.ddesk.jsf;

import lt.bsprendimai.ddesk.ProjectAccessor;
import lt.bsprendimai.ddesk.TicketAccessor;
import lt.bsprendimai.ddesk.UserHandler;

/**
 * ONLY FOR MYFACES 1.1.2
 *
 * JSF Hcks to create multiline table entries, in dataTabe ({@link javax.faces.component.html.HtmlDataTable})
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class JSFHacks {

    private ProjectAccessor pr;

    private UserHandler uh;

    private TicketAccessor ti;

    /** Creates a new instance of JSFHacks */
    public JSFHacks() {
    }

    public String getPrettyTicket() {
        return "tiektNr";
    }

    public String getRowTerminator() {
        return "</span></td></tr><span>";
    }

    public String getSixRowCell() {
        return "</span></td></tr><tr><td class=\"dataHighlightSmall\" colspan=\"6\"><span>";
    }

    public String getFiveRowCell() {
        return "</span></td></tr><tr><td class=\"dataHighlightSmall\" colspan=\"5\"><span>";
    }

    public Integer getDummyInt() {
        return null;
    }

    public void setDummyInt(Integer dummyInt) {
    }

    public UserHandler getUh() {
        return uh;
    }

    public void setUh(UserHandler uh) {
        this.uh = uh;
    }

    public TicketAccessor getTi() {
        return ti;
    }

    public void setTi(TicketAccessor ti) {
        this.ti = ti;
    }

    public ProjectAccessor getPr() {
        return pr;
    }

    public void setPr(ProjectAccessor pr) {
        this.pr = pr;
    }
}
