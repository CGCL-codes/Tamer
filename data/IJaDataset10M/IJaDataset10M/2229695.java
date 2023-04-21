package uk.ac.osswatch.simal.wicket.doap;

import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Creates a page which contains an <a
 * href="http://simile.mit.edu/wiki/Exhibit">Exhibit 2.0</a> Browser. This is a
 * faceted browser for Projects.
 */
public class ExhibitProjectBrowserPage extends BasePage {

    private static final long serialVersionUID = 2675836864409849552L;

    private static final CompressedResourceReference EXHIBIT_CSS = new CompressedResourceReference(BasePage.class, "style/exhibit.css");

    public ExhibitProjectBrowserPage() {
        add(CSSPackageResource.getHeaderContribution(EXHIBIT_CSS));
        add(JavascriptPackageResource.getHeaderContribution("http://static.simile.mit.edu/exhibit/api-2.0/exhibit-api.js"));
        StringBuilder jsonLink = new StringBuilder("<link href=\"");
        jsonLink.append(UserApplication.get().getServletContext().getContextPath());
        jsonLink = jsonLink.append("/simal-rest/allProjects/json\" rel=\"exhibit/data");
        jsonLink.append(" rel=\"exhibit/data\" />");
        add(new StringHeaderContributor(jsonLink.toString()));
    }
}
