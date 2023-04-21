package fr.cnes.sitools.plugins.filters;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.ext.wadl.ApplicationInfo;
import org.restlet.ext.wadl.DocumentationInfo;
import org.restlet.routing.Router;
import fr.cnes.sitools.common.application.SitoolsApplication;
import fr.cnes.sitools.common.model.Category;

/**
 * Application handling dynamic resources
 * @author m.marseille (AKKA Technologies)
 */
public final class FilterClassPluginApplication extends SitoolsApplication {

    /**
   * Constructor with context
   * @param context the restlet context
   */
    public FilterClassPluginApplication(Context context) {
        super(context);
    }

    @Override
    public void sitoolsDescribe() {
        setCategory(Category.ADMIN);
        setName("ApplicationFilterClassPluginApplication");
        setDescription("Exposition of dynamic filters classes");
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attachDefault(FilterClassPluginCollectionResource.class);
        router.attach("/{pluginClass}", FilterClassPluginResource.class);
        return router;
    }

    @Override
    public ApplicationInfo getApplicationInfo(Request request, Response response) {
        ApplicationInfo result = super.getApplicationInfo(request, response);
        DocumentationInfo docInfo = new DocumentationInfo("Filter plug-in management.");
        docInfo.setTitle("API documentation.");
        result.getDocumentations().add(docInfo);
        return result;
    }
}
