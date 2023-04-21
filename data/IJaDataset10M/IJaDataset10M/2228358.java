package userInterface.Events;

import javax.servlet.http.HttpSession;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.gwt.server.WebApplicationContext;
import com.itmill.toolkit.ui.CustomComponent;
import com.itmill.toolkit.ui.CustomLayout;
import com.itmill.toolkit.ui.DateField;
import com.itmill.toolkit.ui.OrderedLayout;
import com.itmill.toolkit.ui.Panel;
import com.itmill.toolkit.ui.Window;

/**
 * @author Noel-Admin
 *
 */
public class EventsPageLayout extends CustomComponent {

    public Window main;

    public CustomLayout eventsPageLayout = new CustomLayout("eventsPageLayout");

    public CustomLayout masterPageLayout;

    public Panel calendarViewPanel = new Panel("Calendar of Events");

    public Panel eventsViewPanel = new Panel("Public Events");

    public ApplicationContext ctx;

    public WebApplicationContext webCtx;

    public HttpSession session;

    public String username;

    private DateField df = new DateField();

    public EventsPageLayout(CustomLayout mainLayout, Window mainWindow) {
        main = mainWindow;
        masterPageLayout = mainLayout;
        ctx = main.getApplication().getContext();
        webCtx = (WebApplicationContext) ctx;
        session = webCtx.getHttpSession();
        username = (String) session.getAttribute("UserName");
        df.setResolution(DateField.RESOLUTION_DAY);
        calendarViewPanel.addComponent(df);
        eventsPageLayout.addComponent(calendarViewPanel, "calendarViewPanel");
        eventsPageLayout.addComponent(eventsViewPanel, "eventsViewPanel");
        setCompositionRoot(eventsPageLayout);
    }
}
