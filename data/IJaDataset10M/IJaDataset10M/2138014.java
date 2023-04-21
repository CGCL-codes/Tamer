package ch.serva.pages;

import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import ch.serva.config.Config;
import ch.serva.db.Domain;
import ch.serva.db.Instance;
import ch.serva.db.User;
import ch.serva.db.collections.Users;
import ch.serva.localization.Dictionary;
import ch.serva.pages.elements.DomainContactBox;
import ch.serva.pages.elements.DomainRelationshipUser;
import ch.serva.pages.elements.DomainRelationshipsBox;
import ch.serva.pages.elements.DomainServicesBox;
import ch.serva.pages.elements.selectors.UserSelector;

/**
 * Main page for the users which are not admins.
 * 
 * @author Lukas Blunschi
 * 
 */
public class HomePage extends AbstractUserPage {

    public static final String NAME = "home";

    public String getUserContent(HttpServletRequest req, EntityManager em, Dictionary dict) {
        StringBuffer html = new StringBuffer();
        Config config = new Config();
        Instance instance = new Instance(em);
        User userLoggedIn = Users.getUserFromSession(req, em);
        User userSel = null;
        if (userLoggedIn.getIsAdmin()) {
            String selUserIdStr = req.getParameter("user_" + User.F_ID);
            if (selUserIdStr == null) {
                userSel = userLoggedIn;
            } else {
                userSel = em.find(User.class, Long.valueOf(selUserIdStr));
            }
            new UserSelector(userSel, HomePage.NAME, instance).appendHtml(html, config, dict);
        } else {
            userSel = userLoggedIn;
        }
        List<Domain> domains = userSel.getDomains();
        html.append("<!-- title -->\n");
        html.append("<h1 class='content'>").append(dict.yourDomains() + ":").append("</h1>\n\n");
        for (Domain domain : domains) {
            final String domainName = domain.getDomainname();
            html.append("<!-- " + domainName + " -->\n");
            html.append("<div class='domain content'>\n\n");
            html.append("<!-- relationship -->\n");
            html.append("<div class='floatright'>\n");
            new DomainRelationshipUser(domain, userSel).appendEmbedableHtml(html, config, dict);
            html.append("</div>\n\n");
            html.append("<!-- show info -->\n");
            html.append("<div class='infolink'>");
            html.append("<a href='javascript:toggleDisplay(\"domain" + domain.getId() + "\")'>");
            html.append(domainName);
            html.append("</a>");
            html.append("</div>\n\n");
            html.append("<!-- info div -->\n");
            html.append("<div id='domain" + domain.getId() + "' style='display:none'>\n\n");
            new DomainRelationshipsBox(domain).appendHtml(html, config, dict);
            boolean showCost = userSel.getAsBillingContact(domain);
            new DomainServicesBox(domain, showCost).appendHtml(html, config, dict);
            new DomainContactBox(domain).appendHtml(html, config, dict);
            html.append("<div style='clear:both'></div>\n\n");
            html.append("<!-- end of info div -->\n");
            html.append("</div>\n\n");
            html.append("<!-- end of " + domainName + " -->\n");
            html.append("</div>\n\n");
        }
        return html.toString();
    }
}
