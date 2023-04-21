package net.sf.sail.webapp.dao.sds;

import net.sf.sail.webapp.domain.sds.SdsUser;
import net.sf.sail.webapp.domain.webservice.http.HttpPutRequest;

/**
 * Interface for the command which updates a user for the Sail Data Service.
 * 
 * @author Laurel Williams
 * 
 * @version $Id: SdsUserUpdateCommand.java 355 2007-04-30 19:21:36Z laurel $
 * 
 */
public interface SdsUserUpdateCommand extends SdsCommand<SdsUser, HttpPutRequest> {

    /**
     * @param sdsUser
     *            the sdsUser to set
     */
    public void setSdsUser(SdsUser sdsUser);
}
