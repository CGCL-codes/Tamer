package com.vo.universalworker.storage;

import com.vo.universalworker.common.application.ContextAttributes;
import java.util.ArrayList;
import java.util.List;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.ext.wadl.ApplicationInfo;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.ext.wadl.ParameterInfo;
import org.restlet.ext.wadl.RepresentationInfo;
import org.restlet.ext.wadl.RequestInfo;
import org.restlet.ext.wadl.ResourceInfo;
import org.restlet.ext.wadl.ResourcesInfo;
import org.restlet.ext.wadl.WadlApplication;
import org.restlet.representation.Representation;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

/**
 * Public storage application
 * @author malapert
 */
public class Storage extends WadlApplication {

    private String storageDirectory;

    private String storageURL;

    /**
     * Constructor
     * @param context Context
     */
    public Storage(Context context) {
        super(context);
        this.storageURL = (String) context.getAttributes().get(ContextAttributes.APP_STORE_DIR);
        this.storageDirectory = (String) context.getAttributes().get(ContextAttributes.ROOT_DIRECTORY) + context.getAttributes().get(ContextAttributes.APP_STORE_DIR);
        setAuthor("Jean-Christophe Malapert");
        setName("Public storage application");
        setDescription("Application to access to processing results");
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        Directory appStorage = new Directory(getContext(), "file://" + this.storageDirectory);
        appStorage.setListingAllowed(true);
        appStorage.setDeeplyAccessible(true);
        router.attach(storageURL, appStorage);
        return router;
    }

    /**
     * Get the storage directory
     * @return Retursn the storageDirectory
     */
    public String getStorageDirectory() {
        return storageDirectory;
    }

    @Override
    protected Representation createWadlRepresentation(ApplicationInfo applicationInfo) {
        ResourceInfo resourceInfo = new ResourceInfo();
        List<MethodInfo> methods = new ArrayList<MethodInfo>();
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setName(Method.GET);
        RequestInfo requestInfo = new RequestInfo();
        List<RepresentationInfo> listRepInfo = new ArrayList<RepresentationInfo>();
        RepresentationInfo repInfo = new RepresentationInfo();
        repInfo.setMediaType(MediaType.TEXT_HTML);
        repInfo.setDocumentation("HTML representation of the Storage content");
        listRepInfo.add(repInfo);
        repInfo = new RepresentationInfo();
        repInfo.setMediaType(MediaType.TEXT_URI_LIST);
        repInfo.setDocumentation("URI-LIST representation of the Storage content");
        listRepInfo.add(repInfo);
        requestInfo.setRepresentations(listRepInfo);
        methodInfo.setRequest(requestInfo);
        methods.add(methodInfo);
        resourceInfo.setMethods(methods);
        resourceInfo.setPath("data");
        ParameterInfo paramInfo = new ParameterInfo();
        paramInfo.setPath("{data-id}");
        paramInfo.setRequired(false);
        List<ParameterInfo> paramsInfo = new ArrayList<ParameterInfo>();
        paramsInfo.add(paramInfo);
        resourceInfo.setParameters(paramsInfo);
        ResourcesInfo resourcesInfo = new ResourcesInfo();
        resourcesInfo.getResources().add(resourceInfo);
        applicationInfo.setResources(resourcesInfo);
        return super.createWadlRepresentation(applicationInfo);
    }
}
