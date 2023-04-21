package org.dspace.app.xmlui.aspect.swordclient;

import org.apache.cocoon.environment.Request;
import org.apache.log4j.Logger;
import org.dspace.app.xmlui.aspect.administrative.FlowResult;
import org.dspace.core.Context;
import org.dspace.sword.client.DSpaceSwordClient;
import org.dspace.sword.client.ServiceDocumentHelper;
import org.purl.sword.base.Collection;
import org.purl.sword.base.ServiceDocument;

/**
 * User: Robin Taylor
 * Date: 21/03/11
 * Time: 22:12
 */
public class SelectCollectionAction {

    private static Logger log = Logger.getLogger(SelectCollectionAction.class);

    public FlowResult processSelectCollection(Context context, Request request, DSpaceSwordClient DSClient) {
        FlowResult result = new FlowResult();
        result.setContinue(false);
        String location = request.getParameter("location");
        ServiceDocument serviceDoc = (ServiceDocument) request.getAttribute("serviceDoc");
        log.info("Collection selected is " + location);
        log.info("Service Doc reference is " + serviceDoc);
        DSClient.setCollection(location);
        String[] fileTypes = ServiceDocumentHelper.getCommonFileTypes(serviceDoc, location);
        String[] packageFormats = ServiceDocumentHelper.getCommonPackageFormats(serviceDoc, location);
        result.setParameter("location", location);
        result.setParameter("serviceDoc", serviceDoc);
        result.setParameter("fileTypes", fileTypes);
        result.setParameter("packageFormats", packageFormats);
        result.setContinue(true);
        result.setOutcome(true);
        return result;
    }
}
