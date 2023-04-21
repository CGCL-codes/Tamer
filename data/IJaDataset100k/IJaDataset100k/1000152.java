package net.sf.sail.webapp.dao.sds;

import net.sf.sail.webapp.domain.sds.SdsJnlp;
import net.sf.sail.webapp.domain.webservice.http.HttpPutRequest;

/**
 * Interface for the command which updates a jnlp for the Sail Data Service.
 * 
 * @author Laurel Williams
 * 
 * @version $Id: SdsJnlpUpdateCommand.java 361 2007-05-03 18:25:06Z laurel $
 * 
 */
public interface SdsJnlpUpdateCommand extends SdsCommand<SdsJnlp, HttpPutRequest> {

    /**
	 * @param sdsJnlp
	 *            the sdsJnlp to set
	 */
    public void setSdsJnlp(SdsJnlp sdsJnlp);
}
