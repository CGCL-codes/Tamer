package org.openremote.beehive.rest;

import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.openremote.beehive.api.service.RemoteSectionService;
import org.openremote.beehive.utils.StringUtil;

/**
 * Exports restful service of LIRC config file export
 * 
 * @author allen.wei 2009-2-15
 */
@Path("/lirc.conf")
public class LIRCConfigFileRESTService extends RESTBaseService {

    /**
    * Shows lirc config file according to vendor name and model name Visits @ url "/{vendor_name}/{model_name}"
    * 
    * @param sectionIds
    * @return content of lirc configuration file
    */
    @GET
    @Produces("text/plain")
    public String getLIRCConfigFile(@QueryParam("ids") String sectionIds) {
        ArrayList<Long> ids = StringUtil.parseStringIds(sectionIds, ",");
        if (ids.size() == 0) {
            return "";
        }
        StringBuffer lircStr = new StringBuffer();
        for (long id : ids) {
            lircStr.append(getRemoteSectionService().exportText(id));
            lircStr.append(System.getProperty("line.separator"));
        }
        return lircStr.toString();
    }

    /**
    * Retrieves instance of RemoteSectionService from spring IOC container
    * 
    * @return RemoteSectionService instance
    */
    private RemoteSectionService getRemoteSectionService() {
        return (RemoteSectionService) getSpringContextInstance().getBean("remoteSectionService");
    }
}
