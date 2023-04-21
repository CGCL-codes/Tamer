package ch.serva.pages.elements;

import ch.serva.config.Config;
import ch.serva.db.Domain;
import ch.serva.localization.Dictionary;

/**
 * An embedable element which lists the users related to a domain.
 * 
 * @author Lukas Blunschi
 * 
 */
public class DomainRelationshipList implements Embedable {

    private final Domain domain;

    public DomainRelationshipList(Domain domain) {
        this.domain = domain;
    }

    public void appendEmbedableHtml(StringBuffer html, Config config, Dictionary dict) {
        html.append("<div>");
        html.append("H: " + domain.getHolder().toShortString()).append(", ");
        html.append("B: " + domain.getBillingcontact().toShortString()).append(", ");
        html.append("T: " + domain.getTechnicalcontact().toShortString()).append(", ");
        html.append("S: " + domain.getHostingcontact().toShortString());
        html.append("</div>");
    }
}
