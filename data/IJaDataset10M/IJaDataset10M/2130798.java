package edu.chop.bic.cnv.ui;

import edu.chop.bic.cnv.ui.header.TabbedHeader;
import edu.chop.bic.cnv.CnvApplication;
import edu.chop.bic.cnv.domain.CustomParameters;
import org.apache.wicket.security.components.SecureWebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SavedSearches extends SecureWebPage {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private CustomParameters customParameters;

    public SavedSearches() {
        Label title = new Label("title", "Saved Searches - " + customParameters.getCustomApplicationName());
        title.setRenderBodyOnly(true);
        add(title);
        add(new TabbedHeader("savedSearchesTabbedHeader"));
        add(new SavedSearchesPanel("savedSearchesPanel"));
        add(new Footer("footer"));
    }

    public CustomParameters getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(CustomParameters customParameters) {
        this.customParameters = customParameters;
    }
}
