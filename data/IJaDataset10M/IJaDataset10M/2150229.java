package uk.ac.osswatch.simal.wicket.foaf;

import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Creates a page which contains an <a
 * href="http://simile.mit.edu/wiki/Exhibit">Exhibit 2.0</a> Browser. This is a
 * faceted browser for People.
 */
public class ExhibitPersonBrowserPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private static final CompressedResourceReference EXHIBIT_CSS = new CompressedResourceReference(BasePage.class, "style/exhibit.css");

    public ExhibitPersonBrowserPage() {
        add(CSSPackageResource.getHeaderContribution(EXHIBIT_CSS));
        add(JavascriptPackageResource.getHeaderContribution("http://static.simile.mit.edu/exhibit/api-2.0/exhibit-api.js"));
        StringBuilder jsonLink = new StringBuilder("<link href=\"");
        jsonLink.append(UserApplication.get().getServletContext().getContextPath());
        jsonLink = jsonLink.append("/simal-rest/allPeople/json\" rel=\"exhibit/data");
        jsonLink.append(" rel=\"exhibit/data\" />");
        add(new StringHeaderContributor(jsonLink.toString()));
    }
}
