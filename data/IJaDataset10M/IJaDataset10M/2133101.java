package org.saintandreas.serket.impl.av;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.saintandreas.serket.didl.DIDLHelper;
import org.saintandreas.serket.didl.types.Object;
import org.saintandreas.serket.impl.didl.SerketBase;
import org.saintandreas.serket.impl.didl.SerketContainer;
import org.saintandreas.serket.impl.didl.misc.RootContainer;
import org.saintandreas.serket.scpd.ContentDirectory;
import org.saintandreas.serket.service.ServiceType;
import org.saintandreas.util.XmlUtil;

public class SerketContentDirectory extends ContentDirectory {

    private static final Log LOG = LogFactory.getLog(SerketContentDirectory.class);

    private RootContainer rootContainer = new RootContainer();

    public SerketContentDirectory(String controlURL, String eventURL) {
        super(SERVICE_ID_PREFIX + "ContentDirectory", controlURL, eventURL);
    }

    @Override
    public ServiceType getServiceTypeEnum() {
        return ServiceType.ContentDirectory;
    }

    @Override
    public BrowseResponse browse(BrowseRequest input) throws IOException {
        LOG.debug(input.objectID + " : " + input.browseFlag);
        SerketBase item = rootContainer.findById(input.objectID);
        SerketContainer<?> container = null;
        if (item instanceof SerketContainer<?>) {
            container = ((SerketContainer<?>) item);
        }
        BrowseResponse response = new BrowseResponse();
        List<SerketBase> retVal = new ArrayList<SerketBase>();
        switch(input.browseFlag) {
            case BrowseMetadata:
                retVal.add(item);
                response.numberReturned = response.totalMatches = 1;
                break;
            case BrowseDirectChildren:
                if (container != null) {
                    container.refreshChildren();
                    retVal.addAll(container.getChildren(input.startingIndex, input.requestedCount));
                    response.numberReturned = retVal.size();
                    response.totalMatches = container.getChildren(0, Integer.MAX_VALUE).size();
                }
                break;
        }
        response.updateID = container != null ? container.getUpdateId() : 0;
        response.result = XmlUtil.formatXmlDocument(DIDLHelper.createDocument(retVal));
        return response;
    }

    @Override
    public CreateObjectResponse createObject(CreateObjectRequest input) {
        return null;
    }

    @Override
    public CreateReferenceResponse createReference(CreateReferenceRequest input) {
        return null;
    }

    @Override
    public DeleteResourceResponse deleteResource(DeleteResourceRequest input) {
        return null;
    }

    @Override
    public DestroyObjectResponse destroyObject(DestroyObjectRequest input) {
        return null;
    }

    @Override
    public ExportResourceResponse exportResource(ExportResourceRequest input) {
        return null;
    }

    @Override
    public GetSearchCapabilitiesResponse getSearchCapabilities(GetSearchCapabilitiesRequest input) {
        return null;
    }

    @Override
    public GetSortCapabilitiesResponse getSortCapabilities(GetSortCapabilitiesRequest input) {
        return null;
    }

    @Override
    public GetSystemUpdateIDResponse getSystemUpdateID(GetSystemUpdateIDRequest input) {
        GetSystemUpdateIDResponse retVal = new GetSystemUpdateIDResponse();
        retVal.id = rootContainer.getUpdateId();
        return retVal;
    }

    @Override
    public GetTransferProgressResponse getTransferProgress(GetTransferProgressRequest input) {
        return null;
    }

    @Override
    public ImportResourceResponse importResource(ImportResourceRequest input) {
        return null;
    }

    @Override
    public SearchResponse search(SearchRequest input) {
        return null;
    }

    @Override
    public StopTransferResourceResponse stopTransferResource(StopTransferResourceRequest input) {
        return null;
    }

    @Override
    public UpdateObjectResponse updateObject(UpdateObjectRequest input) {
        return null;
    }

    public RootContainer getRootContainer() {
        return rootContainer;
    }
}
