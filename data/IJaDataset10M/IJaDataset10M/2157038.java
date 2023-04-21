package net.sf.sail.webapp.dao.sds;

import net.sf.sail.webapp.domain.sds.SdsWorkgroup;
import net.sf.sail.webapp.domain.webservice.http.HttpGetRequest;

/**
 * @author Laurel Williams
 * 
 * @version $Id: SdsWorkgroupGetCommand.java 1208 2007-09-21 17:45:17Z laurel $
 * 
 */
public interface SdsWorkgroupGetCommand extends SdsCommand<SdsWorkgroup, HttpGetRequest> {

    public void setSdsWorkgroup(SdsWorkgroup sdsWorkgroup);
}
