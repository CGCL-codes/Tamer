package userInterface;

import javax.servlet.http.HttpSession;
import userInterface.Contact.ContactPageLayout;
import userInterface.Events.EventsPageLayout;
import userInterface.Messages.MessagesPageLayout;
import userInterface.Networks.NetworksPageLayouts;
import userInterface.Networks.NetworksPanelLayout;
import userInterface.Profile.Profile;
import utils.Constant;
import utils.Log;
import utils.UserPropertiesControl;
import utils.Utilities;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.FileResource;
import com.itmill.toolkit.terminal.gwt.server.WebApplicationContext;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.CustomComponent;
import com.itmill.toolkit.ui.CustomLayout;
import com.itmill.toolkit.ui.Embedded;
import com.itmill.toolkit.ui.Window;
import com.itmill.toolkit.ui.Button.ClickEvent;
import com.itmill.toolkit.ui.Button.ClickListener;

/**
 * @author Noel-Admin
 *
 */
public class Header extends CustomComponent implements ClickListener, Constant {

    private Window masterWindow;

    public CustomLayout headerMenuLayout = new CustomLayout("headerMenuLayout");

    public CustomLayout frontPageLayout = new CustomLayout("frontPageLayout");

    public CustomLayout masterPageLayout = new CustomLayout("MasterPageLayout");

    public ApplicationContext ctx;

    public WebApplicationContext webCtx;

    public HttpSession session;

    public String username;

    private Button btHome = new Button("Home", this);

    private Button btGroups = new Button("Groups", this);

    private Button btNetworks = new Button("Network", this);

    private Button btProfile = new Button("Profile", this);

    private Button btEvents = new Button("Events", this);

    private Button btGallery = new Button("Gallery", this);

    private Button btMessages = new Button("Messages", this);

    private Button btAdministrator = new Button("Administrator", this);

    private Button btContacts = new Button("Contact", this);

    private Button btLogout = new Button("Logout", this);

    private Embedded logo;

    public Header(CustomLayout mainLayout, Window mainWindow) {
        Log.debug(Log.GENERAL, "HeaderLayout: called, now initializing");
        masterWindow = mainWindow;
        ctx = masterWindow.getApplication().getContext();
        webCtx = (WebApplicationContext) ctx;
        session = webCtx.getHttpSession();
        username = (String) session.getAttribute("UserName");
        btHome.setStyleName("link");
        btGroups.setStyleName("link");
        btNetworks.setStyleName("link");
        btProfile.setStyleName("link");
        btEvents.setStyleName("link");
        btGallery.setStyleName("link");
        btMessages.setStyleName("link");
        btAdministrator.setStyleName("link");
        btContacts.setStyleName("link");
        btLogout.setStyleName("link");
        java.io.File file = new java.io.File(Utilities.imagePath() + "/images/header.jpg");
        FileResource fileResource = new FileResource(file, masterWindow.getApplication());
        logo = new Embedded("", fileResource);
        headerMenuLayout.addComponent(logo, "logo");
        headerMenuLayout.addComponent(btHome, "btHome");
        headerMenuLayout.addComponent(btEvents, "btEvents");
        headerMenuLayout.addComponent(btGallery, "btGallery");
        headerMenuLayout.addComponent(btContacts, "btContacts");
        headerMenuLayout.addComponent(btNetworks, "btNetworks");
        if (!username.equalsIgnoreCase("UserName") && !username.equals("") && !username.equalsIgnoreCase("Anonymous")) {
            headerMenuLayout.addComponent(btGroups, "btGroups");
            headerMenuLayout.addComponent(btProfile, "btProfile");
            headerMenuLayout.addComponent(btMessages, "btMessages");
            if (UserPropertiesControl.inGroup(username, USER_GROUP_ADMIN)) {
                headerMenuLayout.addComponent(btAdministrator, "btAdministrator");
            }
            headerMenuLayout.addComponent(btNetworks, "btNetworks");
            headerMenuLayout.addComponent(btLogout, "btLogout");
        }
        setCompositionRoot(headerMenuLayout);
        masterPageLayout = mainLayout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (!username.equalsIgnoreCase("UserName") && !username.equals("") && !username.equalsIgnoreCase("Anonymous")) {
            if (event.getSource() == btProfile) {
                masterPageLayout.removeComponent("bodyLayout");
                masterPageLayout.addComponent(new Profile(masterPageLayout, masterWindow, username), "bodyLayout");
            } else if (event.getSource() == btContacts) {
                masterPageLayout.removeComponent("bodyLayout");
                masterPageLayout.addComponent(new ContactPageLayout(masterPageLayout, masterWindow), "bodyLayout");
            } else if (event.getSource() == btNetworks) {
                masterPageLayout.removeComponent("bodyLayout");
                masterPageLayout.addComponent(new NetworksPageLayouts(masterPageLayout, masterWindow, username), "bodyLayout");
            } else if (event.getSource() == btMessages) {
                masterPageLayout.removeComponent("bodyLayout");
                masterPageLayout.addComponent(new MessagesPageLayout(masterPageLayout, masterWindow), "bodyLayout");
            } else if (event.getSource() == btLogout) {
                session.removeAttribute("UserName");
                session.setAttribute("UserName", "UserName");
                masterPageLayout.removeAllComponents();
                masterPageLayout.addComponent(new Header(masterPageLayout, masterWindow), "headerMenu");
                masterPageLayout.addComponent(new FrontPageLayout(masterPageLayout, masterWindow), "bodyLayout");
            }
        } else if (username.equalsIgnoreCase("UserName") || username.equals("") || username.equalsIgnoreCase("Anonymous")) {
            if (event.getSource() == btHome) {
                masterPageLayout.removeComponent("bodyLayout");
                masterPageLayout.addComponent(new FrontPageLayout(masterPageLayout, masterWindow), "bodyLayout");
            } else if (event.getSource() == btEvents) {
                masterPageLayout.removeComponent("bodyLayout");
                masterPageLayout.addComponent(new EventsPageLayout(masterPageLayout, masterWindow), "bodyLayout");
            } else if (event.getSource() == btNetworks) {
                masterPageLayout.removeComponent("bodyLayout");
                masterPageLayout.addComponent(new NetworksPanelLayout(masterPageLayout, masterWindow), "bodyLayout");
            } else if (event.getSource() == btContacts) {
                masterPageLayout.removeComponent("bodyLayout");
                masterPageLayout.addComponent(new ContactPageLayout(masterPageLayout, masterWindow), "bodyLayout");
            }
            Log.debug(Log.GENERAL, "Logging class was logged");
        }
    }
}
