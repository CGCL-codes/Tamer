package hr.veleri.pages;

import hr.veleri.data.AppConfiguration;
import org.apache.wicket.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 */
public class HomePage extends AuthenticatedPage {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private AppConfiguration appConfiguration;

    public HomePage(final PageParameters parameters) {
        init(HomePage.this);
    }
}
