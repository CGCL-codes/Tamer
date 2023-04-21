package edu.ucla.mbi.imex.imexcentral.persistence.orm;

import edu.ucla.mbi.imex.imexcentral.persistence.facade.ObjectCreationException;
import edu.ucla.mbi.imex.imexcentral.persistence.facade.NoSuchObjectException;
import edu.ucla.mbi.imex.imexcentral.persistence.facade.UnsafeStateException;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Sep 8, 2006
 * Time: 2:37:54 PM
 */
public class PublicationPublicationStatusBuilder extends AbstractHibernateBuilder {

    PublicationPublicationStatus publicationPublicationStatus;

    PublicationPublicationStatusWorker publicationPublicationStatusWorker = new PublicationPublicationStatusWorker();

    final PublicationPublicationStatusAttributes publicationPublicationStatusAttributes = (PublicationPublicationStatusAttributes) this.attributes;

    private PublicationPublicationStatusBuilder() {
        super(null);
    }

    PublicationPublicationStatusBuilder(AbstractAttributes attributes) {
        super(attributes);
    }

    void buildPart() throws ObjectCreationException {
        publicationPublicationStatus = new PublicationPublicationStatus();
        publicationPublicationStatus.setStatus(publicationPublicationStatusAttributes.getPublicationStatus());
    }

    void postBuildInit() throws ObjectCreationException {
        try {
            publicationPublicationStatusWorker.retrieve(publicationPublicationStatus.getUniqueSearchKey());
            throw new ObjectCreationException(publicationPublicationStatus.getUniqueSearchKey() + " already exists in the database.");
        } catch (NoSuchObjectException e) {
            try {
                publicationPublicationStatusWorker.save(publicationPublicationStatus);
            } catch (UnsafeStateException e1) {
                throw new ObjectCreationException(e1.getMessage());
            }
        }
    }

    PublicationPublicationStatus getResult() {
        PublicationPublicationStatus temp = publicationPublicationStatus;
        publicationPublicationStatus = null;
        return publicationPublicationStatus;
    }

    public static class PublicationPublicationStatusAttributes implements AbstractAttributes {

        public String publicationStatus;

        public PublicationPublicationStatusAttributes(String publicationStatus) {
            this.publicationStatus = publicationStatus;
        }

        public String getPublicationStatus() {
            return publicationStatus;
        }
    }
}
