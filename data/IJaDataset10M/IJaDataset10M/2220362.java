package fr.cnes.sitools.applications;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.ext.wadl.ApplicationInfo;
import org.restlet.routing.Router;
import fr.cnes.sitools.common.application.SitoolsApplication;
import fr.cnes.sitools.common.model.Category;
import fr.cnes.sitools.proxy.DirectoryProxy;

/**
 * Application to expose and upload resources files in a Directory.
 * 
 * @author jp.boignard (AKKA Technologies)
 * 
 */
public final class UploadApplication extends SitoolsApplication {

    /** directory path for upload files */
    private String appPath;

    /**
   * Constructor with folder of files exposed
   * 
   * @param context
   *          Restlet {@code Context}
   * @param appPath
   *          Directory
   */
    public UploadApplication(Context context, String appPath) {
        super(context);
        this.appPath = appPath;
    }

    @Override
    public void sitoolsDescribe() {
        setCategory(Category.USER);
        setName("UploadApplication");
        setDescription("Upload pictures, logos for using in datasets, projects, ..." + "-> administrator user can have GET/POST/PUT/DELETE authorizations to access, add or delete files or folder\n" + "-> public must have GET/POST authorization to access and upload the files\n" + "In order to upload files using the ExtJs client, the HTTP-BASIC credentials can be sended in the cookie");
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        DirectoryProxy dp = new DirectoryProxy(getContext(), LocalReference.createFileReference(appPath), getAttachementRef());
        dp.setNegotiatingContent(true);
        this.getMetadataService().addCommonExtensions();
        dp.setDeeplyAccessible(true);
        dp.setListingAllowed(true);
        dp.setModifiable(true);
        this.getTunnelService().setEnabled(true);
        this.getTunnelService().setMethodTunnel(true);
        this.getTunnelService().setMethodParameter("method");
        router.attachDefault(dp);
        return router;
    }

    @Override
    public ApplicationInfo getApplicationInfo(Request request, Response response) {
        ApplicationInfo appInfo = super.getApplicationInfo(request, response);
        appInfo.setDocumentation("Upload pictures, logos for using in datasets, projects, ...");
        return appInfo;
    }
}
