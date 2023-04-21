package userInterface.Resources;

import javax.servlet.http.HttpSession;
import userInterface.LoginLayoutClass;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.gwt.server.WebApplicationContext;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.CustomComponent;
import com.itmill.toolkit.ui.CustomLayout;
import com.itmill.toolkit.ui.Window;

/**
 * @author Noel-Admin
 *
 */
public class ResourcesPageLayout extends CustomComponent {

    public Window main;

    public CustomLayout resourcesPageLayout = new CustomLayout("resourcesPageLayout");

    public CustomLayout masterPageLayout;

    public ApplicationContext ctx;

    public WebApplicationContext webCtx;

    public HttpSession session;

    public String username;

    public ResourcesPageLayout(CustomLayout mainLayout, Window mainWindow) {
        main = mainWindow;
        masterPageLayout = mainLayout;
        ctx = main.getApplication().getContext();
        webCtx = (WebApplicationContext) ctx;
        session = webCtx.getHttpSession();
        username = (String) session.getAttribute("UserName");
        resourcesPageLayout.addComponent(new Button("Testing if it works"), "btTest");
        setCompositionRoot(resourcesPageLayout);
    }
}
