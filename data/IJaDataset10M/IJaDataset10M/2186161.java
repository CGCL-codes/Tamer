package com.gr.staffpm.pages.error;

import javax.servlet.http.HttpServletResponse;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.annotation.strategy.MountHybrid;
import com.gr.staffpm.StaffPMWebPage;

@MountHybrid
@MountPath(path = "secure/InternalError")
public class InternalErrorPage extends StaffPMWebPage {

    /**
     * Constructor.
     */
    public InternalErrorPage() {
        add(homePageLink("homePageLink"));
    }

    /**
     * @see org.apache.wicket.markup.html.WebPage#configureResponse()
     */
    @Override
    protected void configureResponse() {
        super.configureResponse();
        getWebRequestCycle().getWebResponse().getHttpServletResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * @see org.apache.wicket.Component#isVersioned()
     */
    @Override
    public boolean isVersioned() {
        return false;
    }

    /**
     * @see org.apache.wicket.Page#isErrorPage()
     */
    @Override
    public boolean isErrorPage() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Oops..";
    }
}
