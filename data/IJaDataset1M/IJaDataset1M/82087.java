package net.sf.lightbound.demos;

import java.util.Locale;
import java.util.ResourceBundle;
import net.sf.lightbound.Page;
import net.sf.lightbound.PageOutput;
import net.sf.lightbound.Request;
import net.sf.lightbound.WebsiteContext;
import net.sf.lightbound.annotations.BeanProperty;
import net.sf.lightbound.annotations.RenderMethod;
import net.sf.lightbound.annotations.StorageScope;
import net.sf.lightbound.components.links.Link;
import net.sf.lightbound.components.links.ListenerActionLink;
import net.sf.lightbound.components.links.MethodActionLink;
import net.sf.lightbound.components.links.PageLink;
import net.sf.lightbound.components.links.WebLink;
import net.sf.lightbound.events.RedirectEvent;
import net.sf.lightbound.events.SessionClosedEvent;
import net.sf.lightbound.exceptions.InitializationException;

public class RestrictedPageDemoPage extends Page {

    @BeanProperty(storageScope = StorageScope.SESSION)
    private String userName;

    private String foobarText;

    @Override
    public void init(WebsiteContext context) throws InitializationException {
        super.init(context);
        ResourceBundle textBundle = ResourceBundle.getBundle("net.sf.lightbound.demos.restrictedPageTexts", Locale.ENGLISH);
        setAssociatedObjects(textBundle);
        setAssociatedObject("testParam", "");
        setAssociatedObject("actionText", "");
    }

    @Override
    public void onRequest(Request request) {
        if (!isAuthorized()) {
            System.out.println("SESSION NOT OPEN!");
            throw new RedirectEvent(new WebLink("needToLogin.html"));
        }
        System.out.println("SESSION IS OPEN!");
        String testParam = getRequest().getParameters().getString("testParam");
        if (testParam != null) {
            getRequest().setAssociatedObject("testParam", testParam);
        }
    }

    @Override
    public void onSessionEnd(Request request) {
        System.out.println("## Session ended!");
    }

    public boolean isAuthorized() {
        return getRequest().isUserSessionOpen();
    }

    public Link getPageLink() {
        return new PageLink(BasicsDemoPage.class);
    }

    @RenderMethod(id = "justOutput")
    public void renderjustOutput(PageOutput output) {
        output.outputTag();
    }

    public Link getActionLink() {
        return new MethodActionLink(getClass(), "doSomething", "Booyah!!");
    }

    public Link getListenerActionLink() {
        ListenerActionLink link = new ListenerActionLink(new LinkClickedActionListener());
        link.getRequestParameters().add("testParam", "Test parameter set!");
        return link;
    }

    public void doSomething(String foobar) {
        foobarText = "You clicked the special link! This text was passed to the " + "method: '" + foobar + "'";
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFoobarText() {
        return foobarText;
    }

    public Link getLogoutLink() {
        return new MethodActionLink(getClass(), "logout");
    }

    public void logout() {
        throw new SessionClosedEvent(LoginDemoPage.class);
    }
}
