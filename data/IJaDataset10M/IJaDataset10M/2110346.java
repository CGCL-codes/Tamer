package org.openimmunizationsoftware.dqa;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openimmunizationsoftware.dqa.db.model.Organization;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.manager.OrganizationManager;

/**
 * Homepage
 */
public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;

    /**
   * Constructor that is invoked when page is invoked without a session.
   * 
   * @param parameters
   *          Page parameters
   */
    public HomePage(final PageParameters parameters) {
        SessionFactory factory = OrganizationManager.getSessionFactory();
        Session session = factory.openSession();
        Organization organization = (Organization) session.get(Organization.class, 1);
        SubmitterProfile submitterProfile = organization.getPrimaryProfile();
        add(new Label("messageField", "Hello, primary organization = " + organization.getOrgLabel() + ""));
        session.close();
    }
}
