package uk.ac.ebi.metabolights.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.metabolights.dao.StableIdDAO;
import uk.ac.ebi.metabolights.model.StableId;

/**
 * Created by IntelliJ IDEA.
 * User: kenneth
 * Date: 20/09/2011
 * Time: 18:25
 */
@Service
public class AccessionServiceImpl implements AccessionService {

    private static Logger logger = Logger.getLogger(AccessionServiceImpl.class);

    @Autowired
    public StableIdDAO stableIdDAO;

    private StableId stableId;

    private String defaultPrefix;

    /**
	 * Default prefix to use in case of creating the table from scratch
	 * @return String with the default prefix
	 */
    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }

    /**
	 * It has the logic concatenating the Id and the prefix. It also increase the Id by one,
	 * and invoke persistance of the new value to the database with the new Id increased
	 * @return String with the concatenated Accession number
	 */
    @Transactional
    public String getAccessionNumber() {
        String accession;
        logger.info("Getting accession number");
        stableId = getNextStableId();
        accession = stableId.getPrefix() + stableId.getSeq();
        logger.info("New Accession number is " + accession);
        return accession;
    }

    /**
	 * Returns the stableId object.
	 * If the stableId is already instantiated it will return it, avoiding connection to the database (concurrency is not considered).
	 * If there isn't any it will create a default one using 1 as Id and the defaultPrefix as prefix.
	 * If there is more than one row an exception will be thrown.
	 * If there is only one (expected) it will return it.
	 * @return Unique StableId row in the database
	 * @throws ExceptionInInitializerError
	 */
    @Transactional
    private StableId getNextStableId() throws ExceptionInInitializerError {
        logger.info("Asking hibernate session for the StableId object.");
        StableId stableId = stableIdDAO.getNextStableId();
        if (stableId == null) {
            logger.info("There isn't a StableId record. Creating a new default id.");
            stableId = new StableId();
            stableId.setId(1);
            stableId.setSeq(0);
            stableId.setPrefix(defaultPrefix);
        }
        stableId.setSeq(stableId.getSeq() + 1);
        stableIdDAO.update(stableId);
        logger.info("Got a new (Accession number) stable id " + stableId.getSeq());
        return stableId;
    }
}
