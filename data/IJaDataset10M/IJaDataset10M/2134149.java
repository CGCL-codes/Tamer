package edu.ucla.mbi.imex.imexcentral.persistence.orm;

import edu.ucla.mbi.imex.imexcentral.persistence.facade.IMExCentralSecurityException;
import edu.ucla.mbi.imex.imexcentral.persistence.facade.ObjectCreationException;
import edu.ucla.mbi.imex.imexcentral.persistence.facade.AbstractPerson;
import java.util.GregorianCalendar;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Sep 10, 2006
 * Time: 12:58:33 AM
 */
public class SecureBuilderFacade {

    private AbstractSecureBuilder builder;

    public Person createPerson(String requestorLoginId, String loginId, String displayName, String email, String password) throws IMExCentralSecurityException, ObjectCreationException {
        PersonBuilder.PersonAttributes attrs = new PersonBuilder.PersonAttributes(displayName, email, loginId, password);
        builder = new SecurePersonBuilder(requestorLoginId, attrs);
        Person person = (Person) builder.build();
        builder = null;
        return person;
    }

    public Publication createPublication(String requestorLoginId, String pubmedId, String doiNumber, String journalSpecific, String imexNumber, String title, String author, GregorianCalendar releaseDate, GregorianCalendar expectedPublicationDate, GregorianCalendar publicationDate, AbstractPerson person) throws IMExCentralSecurityException, ObjectCreationException {
        PublicationBuilder.PublicationAttributes publicationAttributes = new PublicationBuilder.PublicationAttributes(pubmedId, doiNumber, journalSpecific, imexNumber, title, author, releaseDate, expectedPublicationDate, publicationDate, person);
        builder = new SecurePublicationBuilder(requestorLoginId, publicationAttributes);
        Publication publication = (Publication) builder.build();
        builder = null;
        return publication;
    }

    public Organization createOrganization(String requestorLoginId, String organizationName, String websiteURL, String comment, String contactPersonLoginId, String organizationAdminLoginId) throws IMExCentralSecurityException, ObjectCreationException {
        OrganizationBuilder.OrganizationAttributes organizationAttributes = new OrganizationBuilder.OrganizationAttributes(organizationName, websiteURL, comment, contactPersonLoginId, organizationAdminLoginId);
        builder = new SecureOrganizationBuilder(requestorLoginId, organizationAttributes);
        Organization Organization = (Organization) builder.build();
        builder = null;
        return Organization;
    }

    public Journal createJournal(String requestorLoginId, String name, String websiteURL, String comment) throws IMExCentralSecurityException, ObjectCreationException {
        JournalBuilder.JournalAttributes journalAttributes = new JournalBuilder.JournalAttributes(name, websiteURL, comment);
        builder = new SecureJournalBuilder(requestorLoginId, journalAttributes);
        Journal journal = (Journal) builder.build();
        builder = null;
        return journal;
    }
}
