package se.biobanksregistersyd.boakeity.session;

import javax.ejb.Remote;
import se.biobanksregistersyd.boakeity.entity.Biobank;
import se.biobanksregistersyd.boakeity.entity.Sample;

/**
 * Remote interface for <code>PopulateBean</code>. 
 *
 * @author johant
 */
@Remote
public interface PopulateRemote {

    /**
     * Populate the database with example data.
     */
    void populate();

    /**
     * Find a biobank.
     *
     * @param pKey the key of the biobank.
     * @return the <code>Biobank</code> matching the request.
     */
    Biobank findBiobank(final String pKey);

    /**
     * Find a sample given its key.
     *
     * @param pKey the key of the sample.
     * @return the <code>Sample</code> matching the request.
     */
    Sample findSample(final int pKey);

    /**
     * Find the number of consent decision that belongs to a specific
     * sample, given its key.
     *
     * @param pKey the key of the sample.
     * @return the number of consent decisions for the sample matching the
     *         request.
     */
    int nrOfConsentDecisionsForSample(final int pKey);
}
