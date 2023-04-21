package edu.ucla.mbi.imex.imexcentral.persistence.orm;

import edu.ucla.mbi.imex.imexcentral.persistence.facade.PublicationIdentifier;
import edu.ucla.mbi.imex.imexcentral.persistence.facade.NoSuchObjectException;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Sep 15, 2006
 * Time: 4:05:01 AM
 */
public abstract class AbstractPublicationIdentifierWorker extends AbstractHibernateWorker {

    abstract PublicationIdentifier getPubmedId(String pubmedId) throws NoSuchObjectException;

    abstract PublicationIdentifier getDoiNumber(String doiNumber) throws NoSuchObjectException;

    abstract PublicationIdentifier getImexId(String imexId) throws NoSuchObjectException;

    abstract PublicationIdentifier getJournalSpecificId(String journalSpecificId) throws NoSuchObjectException;
}
