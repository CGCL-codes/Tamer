package net.sf.sail.webapp.dao.sds;

import net.sf.sail.webapp.domain.sds.SdsOffering;
import net.sf.sail.webapp.domain.webservice.http.HttpPostRequest;

/**
 * Interface for the command which creates an offering for the Sail Data
 * Service.
 * 
 * @author Laurel Williams
 * 
 * @version $Id: SdsOfferingCreateCommand.java 278 2007-04-10 19:40:58Z cynick $
 * 
 */
public interface SdsOfferingCreateCommand extends SdsCommand<SdsOffering, HttpPostRequest> {

    /**
     * @param SdsOffering
     *            the SdsOffering to set
     */
    public void setSdsOffering(SdsOffering sdsOffering);
}
